package xmu.oomall.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.ProductPo;
import xmu.oomall.feign.CommentFeign;
import xmu.oomall.mapper.ProductMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author hanzelegend
 */
@Repository
public class ProductsDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductsDao.class);
    @Autowired
    private ProductMapper productMapper;
    @Resource
    private RedisTemplate<String, ProductPo> redisTemplate;
    @Resource
    private CommentFeign commentFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据商品id或者productId获取商品的规格
     * @param goodsId 产品所属商品的id
     * @return 商品规格列表
     */
    public List<ProductPo> listProductsByGoodsId(Integer goodsId){
        try {
            String key = "goods-product" + goodsId;
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (hasKey != null && hasKey) {
                BoundZSetOperations<String, String> boundZsetOperations = stringRedisTemplate.boundZSetOps(key);
                Set<String> gKey = boundZsetOperations.range(0, -1);
                return redisTemplate.opsForValue().multiGet(gKey);
            } else {
                List<ProductPo> productPos = productMapper.listProductsByGoodsId(goodsId);
                String goodsKey = "goods" + goodsId;
                Boolean hasGoodsKey = redisTemplate.hasKey(goodsKey);
                /*只有当redis中存在该goods的时候才将它的product存到redis里面*/
                if (hasGoodsKey != null && hasGoodsKey) {
                    for (ProductPo productPo : productPos) {
                        stringRedisTemplate.opsForZSet().add(key, "products" + productPo.getId(), productPo.getId());
                        redisTemplate.opsForValue().set("products" + productPo.getId(), productPo);
                    }
                }
                return productPos;
            }
        }catch (Exception e){
            return null;
        }
    }

    public ProductPo getProduct(Integer id){
        try {
            String productKey = "products" + id;
            Boolean hasKey = redisTemplate.hasKey(productKey);
            if (hasKey != null && hasKey) {
                return redisTemplate.opsForValue().get(productKey);
            } else {
                ProductPo productPo = productMapper.getProductPo(id);
                if(productPo!=null) {
                    String goodsKey = "goods" + productPo.getGoodsId();
                    Boolean hasGoodsKey = stringRedisTemplate.hasKey(goodsKey);
                    if (hasGoodsKey != null && hasGoodsKey) {
                        String key = "goods-product" + productPo.getGoodsId();
                        stringRedisTemplate.opsForZSet().add(key, "products" + productPo.getId(), productPo.getId());
                        redisTemplate.opsForValue().set("products" + productPo.getId(), productPo);
                    }
                    /*
                     * 这里需要使用到good，当内部接口调用查询单个product，而该product的goods没有在redis里面的时候不需要将它放入redis
                     * */
                    return productPo;
                }
                ProductPo newProduct=new ProductPo();
                newProduct.setId(0);
                return newProduct;
            }
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 插入一个Product
     * @param product product的实例化对象
     * @return 插入的product
     */
     public ProductPo insertProduct(ProductPo product){
         int i;
         try {
             i = productMapper.insertProduct(product);
         }catch (Exception e){
             return null;
         }
        if(i>0) {
            String key = "goods-product" + product.getGoodsId();
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (hasKey != null && hasKey) {
                stringRedisTemplate.opsForZSet().add(key, "products" + product.getId(), product.getId());
                redisTemplate.opsForValue().set("products" + product.getId(), product);
            }
            product.setId(i);
        }
        else {
            product.setId(0);
        }
        return product;
    }

    /**
     * 根据id删除product
     * @param id 要删除的id列表
     * @return 行数
     */
    public int deleteProductByProductId(Integer id) {
        try {
            String productKey = "products" + id;
            Boolean hasKey = redisTemplate.hasKey(productKey);
            if (hasKey != null && hasKey) {
                ProductPo productPo = redisTemplate.opsForValue().get(productKey);
                redisTemplate.delete(productKey);
                String key = "goods-product" + productPo.getGoodsId();
                stringRedisTemplate.opsForZSet().remove(key, productPo);
            }
            int i = productMapper.deleteProductById(id, null);
            if (i != 0) {
                try {
                    commentFeign.deleteCommentByProduct(id);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return i;
        }catch (Exception e){
            return -1;
        }
    }

    /**
     * 根据id更新product
     * @param product product实体类
     * @param type 判断是要怎么更新product，
     *             如果是管理员更新product就直接更新数据库，
     *             然后再决定是否放回redis，
     *             否则先直接在redis更改
     *             定义当type=1的时候是用户购买商品
     *             当type=2的时候是管理员购买商品
     * @return 更新的行数
     */
    public ProductPo updateProduct (ProductPo product, int type){
        String key = "products" + product.getId();
        int admin=2,user=1;
        Boolean hasKey = redisTemplate.hasKey(key);
        if (type==user) {
            if (hasKey != null && hasKey) {
                redisTemplate.opsForValue().getAndSet(key, product);
                return redisTemplate.opsForValue().get(key);
            }
            /*由于此时为用户购买商品，此时该产品的商品也不会存在于redis中，所以直接更新数据库中的产品库存即可*/
            else {
                try {
                    int i = productMapper.updateProduct(product);
                    if (i != 0) {
                        return productMapper.getProductPo(product.getId());
                    }
                }catch (Exception e){
                    return null;
                }
            }
        }
        else if(type==admin){
            try {
                int i = productMapper.updateProduct(product);
                if (i != 0) {
                    ProductPo productPo = productMapper.getProductPo(product.getId());
                    if (hasKey != null && hasKey) {
                        redisTemplate.opsForValue().getAndSet(key, productPo);
                    }
                    return productPo;
                }
            }catch (Exception e){
                return null;
            }
        }
        product.setId(0);
        return product;
    }

    /**
     * 当goods被设置为下架的时候会从redis里面将它移出
     * @param goodsId 商品的id
    */
    public void removeProductFromRedisByGoodsId(Integer goodsId){
        String key="goods-product"+goodsId;
        Boolean hasKey=stringRedisTemplate.hasKey(key);
        if(hasKey!=null&&hasKey) {
            BoundZSetOperations<String,String> boundZsetOperations= stringRedisTemplate.boundZSetOps(key);
            Set<String> productKey=boundZsetOperations.range(0,-1);
            redisTemplate.delete(productKey);
            redisTemplate.delete(key);
        }
    }

    /**
     * 当goods被设置为热销或者最新的时候会将它的规格全部放入redis
     * @param goodsId 商品的id
     */
    public void putProductsIntoRedisByGoodsId(Integer goodsId) {
        String key = "goods-product" + goodsId;
        List<ProductPo> productPos = productMapper.listProductsByGoodsId(goodsId);
        for (ProductPo productPo : productPos) {
            stringRedisTemplate.opsForZSet().add(key, "products" + productPo.getId(), productPo.getId());
            redisTemplate.opsForValue().set("products" + productPo.getId(), productPo);
        }
    }

    /**
     * 将redis中的修改之后的product信息批量存入数据库
     */
    public void transProductFromRedis(){
        Set<String>keys=redisTemplate.keys("products*");
        if(keys!=null) {
            List<ProductPo> productPos = redisTemplate.opsForValue().multiGet(keys);
            if (productPos != null) {
                for (ProductPo productPo : productPos) {
                    if(productPo!=null) {
                        try {
                            productMapper.updateProduct(productPo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            /*当某个产品写入数据库失败则需要在日志文件中记录*/
                            logger.info("更新产品"+productPo+"库存时失败");
                        }
                    }
                }
            }
            else{
                redisTemplate.delete(keys);
            }
        }
    }
}
