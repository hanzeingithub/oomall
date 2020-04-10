package xmu.oomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.oomall.dao.BrandDao;
import xmu.oomall.dao.GoodsCategoryDao;
import xmu.oomall.dao.GoodsDao;
import xmu.oomall.dao.ProductsDao;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.domain.goods.GoodsPo;
import xmu.oomall.domain.goods.ProductPo;
import xmu.oomall.domain.other.GrouponRule;
import xmu.oomall.domain.other.PresaleRule;
import xmu.oomall.domain.other.ShareRule;
import xmu.oomall.feign.DiscountFeign;
import xmu.oomall.feign.ShareFeign;
import xmu.oomall.service.GoodsService;
import xmu.oomall.util.JacksonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanzelegend
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ProductsDao productsDao;
    @Autowired
    GoodsCategoryDao goodsCategoryDao;
    @Autowired
    BrandDao brandDao;
    @Resource
    DiscountFeign discountFeign;
    @Resource
    ShareFeign shareFeign;

    private static<T> T getBack(Object result,Class<T>clazz){
        String theResult= JacksonUtil.toJson(result);
        Integer errno= JacksonUtil.parseInteger(theResult,"errno");
        Integer success=0;
        if(!success.equals(errno)){
            return null;
        }

        return JacksonUtil.parseObject(theResult,"data",clazz);
    }

    private Goods packageGoods(GoodsPo goodsPo){
        Goods good=new Goods(goodsPo);
        Integer id=goodsPo.getId();
        List<ProductPo>productList=productsDao.listProductsByGoodsId(id);
        good.setProductPoList(productList);
        if(good.getBrandId()!=null) {
            good.setBrandPo(brandDao.getBrandById(good.getBrandId()));
        }
        if(good.getGoodsCategoryId()!=null) {
            good.setGoodsCategoryPo(goodsCategoryDao.getGoodsCategoryById(good.getGoodsCategoryId()));
        }
        try {
            ShareRule shareRule=getBack(shareFeign.getShareRule(id),ShareRule.class);
            good.setShareRule(shareRule);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            GrouponRule grouponRule = getBack(discountFeign.getGrouponRule(id), GrouponRule.class);
            good.setGrouponRule(grouponRule);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            PresaleRule presaleRule=getBack(discountFeign.getPresaleRule(id),PresaleRule.class);
            good.setPresaleRule(presaleRule);
        }catch (Exception e){
            e.printStackTrace();
        }
        return good;
    }
    /**
     * 根据id获取Goods信息
     * @param id goods的id
     * @param type 判断是否需要打包，1是要打包，2是不需要打包
     * @return Goods
     */
    @Override
    public Goods getGoodsById(Integer id,Integer type) {
        GoodsPo goodsPo=goodsDao.getGoodsById(id);
        if(goodsPo==null){
            return null;
        }
        if(type.equals(1)) {
            return packageGoods(goodsPo);
        }
        else{
            return new Goods(goodsPo);
        }
    }

    /**
     * 用户查找商品列表
     * @param name  商品名称
     * @param page  分页数
     * @param limit 分页大小
     * @return Goods的列表
     */
    @Override
    public List<Goods> listGoodsForUser(String name, Integer page, Integer limit) {
        List<GoodsPo>goodsPos=goodsDao.listGoodsByName(name,page,limit);
        if(goodsPos==null){
            return null;
        }
        List<Goods>goods=new ArrayList<>();
        for(GoodsPo goodsPo:goodsPos){
            goods.add(packageGoods(goodsPo));
        }
        return goods;
    }

    /**
     * 根据brand查询商品
     * @param brandId 品牌的id
     * @param limit
     * @param page
     * @return 查询到的商品列表
     */
    @Override
    public List<Goods> listGoodsByBrand(Integer brandId, Integer limit, Integer page){
        List<GoodsPo>goodsPos=goodsDao.listGoodsByBrand(brandId,page,limit);
        if(goodsPos==null){
            return null;
        }
        List<Goods>goods=new ArrayList<>();
        for(GoodsPo goodsPo:goodsPos){
            goods.add(packageGoods(goodsPo));
        }
        return goods;
    }

    /**
     * 根据category来查找goods
     * @param categoryId category的id
     * @return Goods的列表
     */
    @Override
    public List<Goods> listGoodsByCategory(Integer categoryId, Integer limit, Integer page){
        List<GoodsPo>goodsPos=goodsDao.listGoodsByCategory(categoryId,page,limit);
        if(goodsPos==null){
            return null;
        }
        List<Goods>goods=new ArrayList<>();
        for(GoodsPo goodsPo:goodsPos){
            goods.add(packageGoods(goodsPo));
        }
        return goods;
    }

    @Override
    public void changeBrandOrCategory(Integer brandId, Integer categoryId) {
        goodsDao.changeBrandOrCategory(brandId, categoryId);
    }

    /**
     * 管理员查找商品的信息
     * @param page    分页数
     * @param limit   分页大小
     * @param name    商品名
     * @param goodsSn 商品序列
     * @return GoodsPo的列表
     */
    @Override
    public List<GoodsPo> listGoodsPo(Integer page, Integer limit, String name, String goodsSn) {
        return goodsDao.listGoodsByNameOrGoodsSn(goodsSn,name,limit,page);
    }

    /**
     * 增加一件商品
     * @param goodsPo 商品的po
     * @return 增加后的商品
     */
    @Override
    public GoodsPo insertGoods(GoodsPo goodsPo) {
        return goodsDao.insertGoodsPo(goodsPo);
    }

    /**
     * 管理员更新商品
     * @param goodsPo 商品的对象
     * @return 更新后的goodsPo
     */
    @Override
    public GoodsPo updateGoods(GoodsPo goodsPo) {
        GoodsPo good = goodsDao.updateGoodsPo(goodsPo);
        if (good.getId() > 0) {
            if (good.getStatusCode() != null && !good.getStatusCode().equals(0)) {
                productsDao.removeProductFromRedisByGoodsId(good.getId());
            } else if (good.getStatusCode() != null && good.getStatusCode() > 1) {
                productsDao.putProductsIntoRedisByGoodsId(good.getId());
            }
        }
        return good;
    }

    /**
     * 管理员删除商品，需要将该goods的所有product删除，以及删除评论（可以放在deleteProduct做）
     * @param id 商品id
     * @return 删除结果
     */
    @Override
    public int deleteGoods(Integer id) {
        GoodsPo goodsPo=goodsDao.getGoodsById(id);
        if(goodsPo==null){
            return -1;
        }
        if(!goodsPo.getStatusCode().equals(0)){
            return 0;
        }
        int i=goodsDao.deleteGoodsPo(id);
        if(i>0) {
            productsDao.removeProductFromRedisByGoodsId(id);
            List<ProductPo> productPos = productsDao.listProductsByGoodsId(id);
            if(productPos!=null){
                for(ProductPo productPo:productPos){
                    int n=productsDao.deleteProductByProductId(productPo.getId());
                    if(n==-1){
                        return -1;
                    }
                }
            }
        }
        return i;
    }
}