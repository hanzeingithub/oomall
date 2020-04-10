package xmu.oomall.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.GoodsPo;
import xmu.oomall.feign.FreightFeign;
import xmu.oomall.mapper.BrandMapper;
import xmu.oomall.mapper.GoodsCategoryMapper;
import xmu.oomall.mapper.GoodsMapper;
import xmu.oomall.util.JacksonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author hanzelegend
 */
@Repository
public class GoodsDao {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    private RedisTemplate<String, GoodsPo> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FreightFeign freightFeign;

    /**
     * 用来判断要更新的数据或者要插入的数据是否合法
     */
    private Boolean isOk(GoodsPo goodsPo){
        try {
            if (goodsPo.getGoodsCategoryId() != null && !goodsPo.getGoodsCategoryId().equals(0) && goodsCategoryMapper.getGoodsCategoryById(goodsPo.getGoodsCategoryId()) == null) {
                return false;
            }
            if (goodsPo.getBrandId() != null && !goodsPo.getGoodsCategoryId().equals(0)&& brandMapper.getBrandById(goodsPo.getBrandId()) == null) {
                return false;
            }
            if(goodsPo.getBeSpecial()!=null&&goodsPo.getBeSpecial()){
                try {
                    Object result = freightFeign.getSpecialFreights(goodsPo.getSpecialFreightId());
                    String body= JacksonUtil.toJson(result);
                    Integer success=0;
                    Integer errno= JacksonUtil.parseInteger(body,"errno");
                    return success.equals(errno);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }catch (Exception e){
            return null;
        }
    }

    private List<GoodsPo> listBySomething(Integer id, Integer page, Integer limit, Integer type){
        String key;
        if(type.equals(1)){
            key="category"+id;
        }
        else {
            key="brand"+id;
        }
        Integer start = (page - 1) * limit;
        Boolean hasKey=stringRedisTemplate.hasKey(key);
        /*这种是redis中已经存有此商品信息的情况*/
        if(hasKey!=null&&hasKey){
            BoundZSetOperations<String,String> boundSetOperations= stringRedisTemplate.boundZSetOps(key);
            Integer size= Math.toIntExact(boundSetOperations.size());
            /*这是要查询的范围已经超出了redis的存储范围的情况*/
            if(size<=start) {
                if(type.equals(1)){
                    return putGoodsIntoRedis(key,start,limit,null,id);
                }
                else {
                    return putGoodsIntoRedis(key,start,limit,id,null);
                }

            }
            /*这是查询范围没有超过redis的存储范围的情况*/
            else if(size>=start+limit){
                Set<String>goodsKeys=boundSetOperations.range(start,start+limit);
                return redisTemplate.opsForValue().multiGet(goodsKeys);
            }
            else{
                Set<String>goodsKeys=boundSetOperations.range(start,-1);
                Integer rest= Math.toIntExact(limit - (size - start));
                List<GoodsPo>theGoods=redisTemplate.opsForValue().multiGet(goodsKeys);
                List<GoodsPo>restGoods=new ArrayList<>();
                if(type.equals(1)){
                    restGoods=putGoodsIntoRedis(key,size,rest,null,id);
                }
                else {
                    restGoods=putGoodsIntoRedis(key,size,rest,id,null);
                }
                theGoods.addAll(restGoods);
                return theGoods;
            }
        }
        else {
            if(type.equals(1)) {
                return putGoodsIntoRedis(key, start, limit, null, id);
            }
            else
            {
                return putGoodsIntoRedis(key, start, limit, id, null);
            }
        }
    }

    private List<GoodsPo> putGoodsIntoRedis(String key, Integer start, Integer limit, Integer brandId, Integer categoryId){
        List<GoodsPo>goods=goodsMapper.listGoodsByBrandAndCategory(brandId,categoryId,start,limit);
        for(GoodsPo good:goods){
            stringRedisTemplate.opsForZSet().add(key,"goods"+good.getId(),good.getId());
            redisTemplate.opsForValue().set("goods"+good.getId(),good);
        }
        return goods;
    }

    private void putGoodIntoRedis(GoodsPo good){
        String goodsKey="goods"+good.getId();
        String categoryKey="category"+good.getGoodsCategoryId();
        String brandKey="brand"+good.getBrandId();
        redisTemplate.opsForValue().set(goodsKey,good);
        Boolean hasKey=stringRedisTemplate.hasKey(categoryKey);
        if(hasKey!=null&&hasKey){
            stringRedisTemplate.opsForZSet().add(categoryKey,goodsKey,good.getId());
        }
        hasKey=stringRedisTemplate.hasKey(brandKey);
        if(hasKey!=null&&hasKey){
            stringRedisTemplate.opsForZSet().add(brandKey,goodsKey,good.getId());
        }
    }

    private void removeSomething(String key,Integer type){
        BoundZSetOperations<String,String> boundSetOperations= stringRedisTemplate.boundZSetOps(key);
        Set<String>keys=boundSetOperations.range(0,-1);
        List<GoodsPo>goodsPos=redisTemplate.opsForValue().multiGet(keys);
        for(GoodsPo goodsPo:goodsPos){
            if(type.equals(1)){
                goodsPo.setBrandId(0);
            }
            else {
                goodsPo.setGoodsCategoryId(0);
            }
            redisTemplate.opsForValue().getAndSet("goods"+goodsPo.getId(),goodsPo);
        }
        stringRedisTemplate.delete(key);
    }

    private void removeGoodsFromRedis(String goodsKey) {
        GoodsPo oldGood = redisTemplate.opsForValue().get(goodsKey);
        redisTemplate.delete(goodsKey);
        String categoryKey = "category" + oldGood.getGoodsCategoryId();
        String brandKey = "brand" + oldGood.getBrandId();
        Boolean hasKey = stringRedisTemplate.hasKey(categoryKey);
        if (hasKey != null && hasKey) {
            redisTemplate.opsForZSet().remove(categoryKey, oldGood);
        }
        hasKey = stringRedisTemplate.hasKey(brandKey);
        if (hasKey != null && hasKey) {
            redisTemplate.opsForZSet().remove(brandKey, oldGood);
        }
    }

    public List<Integer>initializingRedis(){
        List<GoodsPo>goodsPos=goodsMapper.initializeRedis();
        List<Integer>goodsIds=new ArrayList<>();
        for(GoodsPo goodsPo:goodsPos){
            goodsIds.add(goodsPo.getId());
            putGoodIntoRedis(goodsPo);
        }
        return goodsIds;
    }

    public List<GoodsPo>listGoodsByName(String name, Integer page, Integer limit){
        try {
            Integer start = (page - 1) * limit;
            return goodsMapper.listGoodsByName(name, start, limit);
        }catch (Exception e){
            return null;
        }
    }

    public List<GoodsPo>listGoodsByCategory(Integer categoryId, Integer page, Integer limit){
        try {
            return listBySomething(categoryId, page, limit, 1);
        }catch (Exception e){
            return null;
        }
    }

    public List<GoodsPo>listGoodsByBrand(Integer brandId, Integer page, Integer limit){
        try {
            return listBySomething(brandId, page, limit, 2);
        }catch (Exception e){
            return null;
        }
    }

    public GoodsPo getGoodsById(Integer id){
        try {
            String gKey = "goods" + id;
            Boolean hasKey = redisTemplate.hasKey(gKey);
            if (hasKey != null && hasKey) {
                return redisTemplate.opsForValue().get(gKey);
            }
            else {
                GoodsPo good = goodsMapper.getGoodsById(id);
                if(good==null){
                    GoodsPo goodsPo=new GoodsPo();
                    goodsPo.setId(0);
                    return goodsPo;
                }
                if (good.getStatusCode()!=null&&!good.getStatusCode().equals(0)) {
                    putGoodIntoRedis(good);
                }
                return good;
            }
        }catch (Exception e){
            return null;
        }
    }

    public List<GoodsPo> listGoodsByNameOrGoodsSn(String goodsSn, String name, Integer limit, Integer page){
        try {
            Integer start = (page - 1) * limit;
            return goodsMapper.listGoodsByQuery(goodsSn, name, start, limit);
        }catch (Exception e){
            return null;
        }
    }

    public void changeBrandOrCategory(Integer brandId,Integer categoryId){
        if(brandId!=null){
            removeSomething("brand"+brandId,1);
            goodsMapper.changeBrand(brandId);
        }
        if(categoryId!=null){
            removeSomething("category"+categoryId,2);
            goodsMapper.changeCategory(categoryId);
        }
    }

    public GoodsPo updateGoodsPo(GoodsPo goodsPo){
        try {
            Boolean isOk=isOk((goodsPo));
            if(isOk==null){
                return null;
            }
            if(isOk) {
                int i = goodsMapper.updateGoods(goodsPo);
                if (i != 0) {
                    GoodsPo newGood = goodsMapper.getGoodsById(goodsPo.getId());
                    String goodsKey = "goods" + goodsPo.getId();
                    Boolean hasKey = redisTemplate.hasKey(goodsKey);
                    if (hasKey != null && hasKey) {
                        removeGoodsFromRedis(goodsKey);
                        if (!newGood.getStatusCode().equals(0)) {
                            putGoodIntoRedis(newGood);
                        }
                    } else if (newGood.getStatusCode() > 1) {
                        putGoodIntoRedis(newGood);
                    }
                    return newGood;
                }
                /*更新失败*/
                else {
                    goodsPo.setId(0);
                    return goodsPo;
                }
            }
            goodsPo.setId(0);
            return goodsPo;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 新加入goods的时候该goods并不存在product，因此不能放在redis层
     */
    public GoodsPo insertGoodsPo(GoodsPo goodsPo){
        try {
            Boolean isOk=isOk((goodsPo));
            if(isOk==null){
                return null;
            }
            if(isOk) {
                int i = goodsMapper.insertGoods(goodsPo);
                if (i == 0) {
                    goodsPo.setId(0);
                }
                return goodsPo;
            }
            goodsPo.setId(0);
            return goodsPo;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 由于删除商品时需要先把商品下架，因此此时商品不可能存在内存中
     * */
    public int deleteGoodsPo(Integer goodsId){
        try {
            return goodsMapper.deleteGoodsById(goodsId);
        }catch (Exception e){
            return -1;
        }
    }
}