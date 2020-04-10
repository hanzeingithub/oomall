package xmu.oomall.controller;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmu.oomall.dao.GoodsCategoryDao;
import xmu.oomall.dao.GoodsDao;
import xmu.oomall.domain.footprintitem.FootprintItemPo;
import xmu.oomall.domain.goods.*;
import xmu.oomall.domain.other.OrderItem;
import xmu.oomall.feign.FootprintsFeign;
import xmu.oomall.mapper.GoodsCategoryMapper;
import xmu.oomall.service.BrandService;
import xmu.oomall.service.GoodsCategoryService;
import xmu.oomall.service.GoodsService;
import xmu.oomall.service.ProductService;
import xmu.oomall.util.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
/**
 * GoodsController
 *
 * @author YangHong
 * @date 2019-12-15
 */
@RequestMapping("")
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private BrandService brandService;
    @Resource
    private FootprintsFeign footprintsFeign;
    /**
     *                                         内部接口
     */

    /**
     * 根据orderItemList来扣库存
     * @param orderItemList orderItem的列表
     * @param operation true表示+库存，false表示扣库存
     * @return  true or false
     */
    @PostMapping("/products/decute")
    public Object updateDecute(@RequestParam("operation") boolean operation,
                                @RequestBody List<OrderItem> orderItemList){
        if (orderItemList == null || orderItemList.size() == 0){
            return ResponseUtil.ok(false);
        }else{ return ResponseUtil.ok(productService.updateDecute(orderItemList,operation));
        }
    }

    private Integer getUserId(HttpServletRequest request){
        String userId=request.getHeader("userId");
        if(userId==null){
            return null;
        }
        return Integer.valueOf(userId);
    }

    @GetMapping(value = "/user/product/{id}")
    public Object getProductsById(@PathVariable Integer id){
        Product product=productService.getProduct(id);
        if(product==null){
            return ResponseUtil.serious();
        }
        if(product.getId().equals(0)){
            return ResponseUtil.badArgumentValue();
        }
        if(product.getGoodsPo().getStatusCode().equals(0)){
            return ResponseUtil.badArgumentValue();
        }
        return ResponseUtil.ok(product);
    }
    @GetMapping(value = "/inner/goods/{id}")
    public Object getGoodById(@PathVariable Integer id){
        Goods goods=goodsService.getGoodsById(id,2);
        if(goods==null){
            return ResponseUtil.serious();
        }
        if(goods.getStatusCode()==null||goods.getStatusCode().equals(0)){
            return ResponseUtil.badArgumentValue();
        }
        return ResponseUtil.ok(goods);
    }

    /**
     * 判断商品是否在售 - 内部
     * @param id 商品id
     * @return 是否在售
     */
    @GetMapping("/goods/{id}/isOnSale")
    public boolean isGoodsOnSale(@PathVariable Integer id){
        Goods goods=goodsService.getGoodsById(id,2);
        return !goods.getStatusCode().equals(0);
    }


    /*Goods的service*/

    /**
     * 根据id找商品信息
     * @param id 商品的id
     * @return 该商品信息
     */
    @GetMapping("/goods/{id}")
    public Object getGoodsById(@PathVariable("id") Integer id,
                               HttpServletRequest request){
        Integer userId=getUserId(request);
        if (id < 0){
            return ResponseUtil.fail(775,"该商品不存在");
        }
        else{
            Goods goods = goodsService.getGoodsById(id,1);
            if (goods == null){
                return ResponseUtil.fail(775,"该商品不存在");
            }
            if(goods.getId().equals(0)){
                return ResponseUtil.fail(775,"该商品不存在");
            }
            if(goods.getStatusCode()!=null&&goods.getStatusCode().equals(0)){
                return ResponseUtil.fail(775,"该商品不存在");
            }
            else {
                if(userId!=null) {
                    FootprintItemPo footprintItemPo = new FootprintItemPo();
                    footprintItemPo.setUserId(userId);
                    footprintItemPo.setGoodsId(goods.getId());
                    footprintItemPo.setBirthTime(LocalDateTime.now());
                    try {
                        footprintsFeign.addFootPrint(footprintItemPo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return ResponseUtil.ok(goods);
            }
        }
    }

    /**
     * 根据商品的信息查询商品
     * @return 商品的列表
     */
    @GetMapping("/goods")
    public Object queryGoods(@RequestParam(value = "goodsName",required = false) String goodsName,
                             @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                             @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit){
        boolean legal;
        if (page == null || limit == null){
            return ResponseUtil.fail(775,"该商品不存在");
        }
        legal = page > 0 && limit > 0;
        if (legal){
            List<Goods> goods = goodsService.listGoodsForUser(goodsName, page, limit);
            if (goods == null){
                return ResponseUtil.fail(775,"该商品不存在");
            }
            else {
                return ResponseUtil.ok(goods);
            }
        }
        else{
            return ResponseUtil.fail(775,"该商品不存在");
        }
    }

    /**
     * 管理员增加商品
     * @param goodsPo 商品的po
     * @return 增加后的商品
     */
    @PostMapping("/goods")
    public Object insertGoods(@RequestBody GoodsPo goodsPo){
//        Integer userId=getUserId(request);
//        if(userId==null){
//            return ResponseUtil.fail(669,"管理员未登陆");
//        }
        boolean legal = goodsPo.getName() != null && goodsPo.getGoodsSn() != null;
        if (legal){
            goodsPo.setBeDeleted(false);
            goodsPo.setStatusCode(0);
            GoodsPo goodsPo1 = goodsService.insertGoods(goodsPo);
            if(goodsPo1==null){
                return ResponseUtil.fail(771,"商品新增失败");
            }
            else if (goodsPo1.getId() <1){
                return ResponseUtil.fail(771,"商品新增失败");
            }
            else{
                return ResponseUtil.ok(goodsPo1);
            }
        }else{
            return ResponseUtil.fail(771,"商品新增失败");
        }
    }

    /**
     * 管理员修改商品
     * @param id 商品的id
     * @param goodsPo 商品Po
     * @return 修改后的对象
     */
    @PutMapping("/goods/{id}")
    public Object putGoods(@PathVariable("id") Integer id,
                           @RequestBody GoodsPo goodsPo,
                           HttpServletRequest request){
        boolean legal = true;
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(669,"管理员未登陆");
        }
        if (id < 0){
            return ResponseUtil.fail(772,"商品修改失败");
        }
        if (goodsPo.getId() != null){
            legal = id.equals(goodsPo.getId());
        }
        // 若是要上架需要有产品
        if(goodsPo.getStatusCode()!=null && goodsPo.getStatusCode()!=0){
            List<ProductPo> productPos=productService.listProducts(id);
            if(productPos==null||productPos.isEmpty()){
                return ResponseUtil.fail(772,"商品修改失败");
            }
        }
        if (legal){
            goodsPo.setId(id);
            GoodsPo goodsPo1 = goodsService.updateGoods(goodsPo);
            if (goodsPo1 == null){
                return ResponseUtil.fail(772,"商品修改失败");
            }else if(goodsPo1.getId()==0){
                return  ResponseUtil.fail(772,"商品修改失败");
            }
            else{
                return ResponseUtil.ok(goodsPo1);
            }
        } else{
            return ResponseUtil.fail(772,"商品修改失败");
        }
    }

    /**
     * 管理员删除商品，这是一个逻辑删除
     * @param id 商品的id
     * @return 删除结果
     */
    @DeleteMapping("/goods/{id}")
    public Object deleteGoods(@PathVariable("id") Integer id){
        if (id < 0){
            return ResponseUtil.fail(773,"商品删除失败");
        }else{
            int num = goodsService.deleteGoods(id);
            if (num == 0){
                return  ResponseUtil.fail(773,"商品删除失败");
            }
            else if(num>0){
                return  ResponseUtil.ok();
            } else {
                return ResponseUtil.fail(773,"商品删除失败");
            }
        }
    }

    /**
     * 管理员查询商品
     * @param page 商品页数
     * @param limit 分页大小
     * @param goodsSn 商品的Sn
     * @param name 商品名
     * @return 商品的列表
     */
    @GetMapping("/admin/goods")
    public Object goodsPoList(@RequestParam("page") Integer page,
                              @RequestParam("limit") Integer limit,
                              @RequestParam("goodsSn") String goodsSn,
                              @RequestParam("name") String name){
        boolean legal = page != null && limit != null;
        if(!legal){
            return ResponseUtil.fail(776,"获取商品列表失败");
        }
        if ((goodsSn == null&&name==null)){
            legal = false;
        }
        if(("".equals(goodsSn)&&"".equals(name))){
            legal=false;
        }
        if("".equals(goodsSn)){
            goodsSn=null;
        }
        if("".equals(name)){
            name=null;
        }
        if (legal){
            List<GoodsPo> goodsPos = goodsService.listGoodsPo(page, limit, name, goodsSn);
            if(goodsPos==null){
                return ResponseUtil.fail(776,"获取商品列表失败");
            }
            else{
                return  ResponseUtil.ok(goodsPos);
            }
        }
        else {
            return  ResponseUtil.fail(776,"获取商品列表失败");
        }
    }

    /**
     * 管理员可以查看的信息更多
     * @param id 商品的id
     * @return 管理员看到的商品的信息
     */
    @GetMapping("/admin/goods/{id}")
    public Object getAdminGoods(@PathVariable("id") Integer id){
        if (id < 0){
            return ResponseUtil.fail(775,"该商品不存在");
        }else {
            Goods goods = goodsService.getGoodsById(id,1);
            if (goods == null) {
                return ResponseUtil.fail(775,"该商品不存在");
            } else if (goods.getId() < 1) {
                return ResponseUtil.fail(775,"该商品不存在");
            } else {
                return ResponseUtil.ok(goods);
            }
        }
    }

    /**
     * 用户查看brand下的商品
     * @param id 品牌id
     * @param page 分页数
     * @param limit 分页大小
     * @return 查到的商品
     */
    @GetMapping("/brands/{id}/goods")
    public Object listGoodsByBrandId(@PathVariable("id") Integer id,
                                     @RequestParam Integer page,@RequestParam Integer limit){
        if (page == null || limit == null || id<1){
            return ResponseUtil.fail(776,"获取商品列表失败");
        }
        boolean legal = limit > 0 && page > 0;
        if(legal){
            List<Goods> goodsList=goodsService.listGoodsByBrand(id, limit, page);
            if(goodsList==null){
                return ResponseUtil.fail(776,"获取商品列表失败");
            }
            return ResponseUtil.fail(776,"获取商品列表失败");
        }else{
            return ResponseUtil.fail(776,"获取商品列表失败");
        }
    }

    @GetMapping("/categories/{id}/goods")
    public Object listGoodsByCategoryId(@PathVariable("id") Integer id,
                                        @RequestParam Integer page,@RequestParam Integer limit){
        if (page == null || limit == null || id<1){
            return ResponseUtil.badArgumentValue();
        }
        boolean legal = limit > 0 && page > 0;
        if(legal){
            List<Goods> goodsList=goodsService.listGoodsByCategory(id,limit,page);
            if(goodsList==null){
                return ResponseUtil.fail(776,"获取商品列表失败");
            }
            return ResponseUtil.fail(776,"获取商品列表失败");
        }else{
            return ResponseUtil.fail(776,"获取商品列表失败");
        }
    }

    /**
     *                                                  product
     */

    /**
     * 根据商品id查询该商品下所有的product
     * @param id 商品id
     * @return productPo的列表
     */
    @GetMapping("/goods/{id}/products")
    public Object getProductsByGoods(@PathVariable("id") Integer id) {
        if (id < 0) {
            return ResponseUtil.badArgumentValue();
        } else {
            List<ProductPo> productPos = productService.listProducts(id);
            if (productPos == null) {
                return ResponseUtil.fail(784,"该产品不存在");
            }
            else if(productPos.isEmpty()){
                return ResponseUtil.fail(784,"该产品不存在");
            }
            else {
                return ResponseUtil.ok(productPos);
            }
        }
    }

    /**
     * 在某个goods下新增一个product
     * @param id goods的id
     * @param productPo 要增加的产品
     * @return 新增的产品
     */
    @PostMapping("goods/{id}/products")
    public Object insertProduct(@PathVariable("id") Integer id, @RequestBody ProductPo productPo){
        if(productPo==null){
            return ResponseUtil.badArgument();
        }
        if(productPo.getGoodsId()==null||productPo.getId()!=null){
            return ResponseUtil.fail(781,"产品新增失败");
        }
        if(!productPo.getGoodsId().equals(id)){
            return ResponseUtil.fail(781,"产品新增失败");
        }
        ProductPo product=productService.insertProduct(productPo);
        if(product==null){
            return ResponseUtil.fail(781,"产品新增失败");
        }
        if(product.getId().equals(0)){
            return ResponseUtil.fail(781,"产品新增失败");
        }
        return ResponseUtil.ok(product);
    }


    /**
     * 管理员修改某个product
     * @param id product的id
     * @param productPo 要修改的product
     * @return 修改后的product
     */
    @PutMapping("/products/{id}")
    public Object updateProduct(@PathVariable("id") Integer id,@RequestBody ProductPo productPo){
        boolean legal;
        legal = id > 0;
        if (productPo.getId() != null){
            legal = legal && id.equals(productPo.getId());
        }
        if (legal){
            productPo.setId(id);
            try {
                productPo = productService.updateProduct(productPo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (productPo == null){
                return ResponseUtil.fail(782,"产品修改失败");
            }
            else if(productPo.getId()<1){
                return ResponseUtil.fail(782,"产品修改失败");
            }
            else{
                return  ResponseUtil.ok(productPo);
            }
        }else{
            return ResponseUtil.fail(782,"产品修改失败");
        }
    }

    /**
     * 把某个product删了
     * @param id product的id
     * @return 删除结果
     */
    @DeleteMapping("/products/{id}")
    public Object deleteProduct(@PathVariable("id") Integer id){
        if (id < 0){
            return ResponseUtil.fail(783,"产品删除失败");
        }else{
            // 需要删除这个product下的评论
            int num = productService.deleteProduct(id);
            if (num == 0){
                return ResponseUtil.fail(783,"产品删除失败");
            }
            else if(num==-1)
            {
                return ResponseUtil.fail(783,"产品删除失败");
            }
            else{
                return ResponseUtil.ok();
            }
        }
    }

    /**
     *                              Brand
     */
    @GetMapping("/admins/brands")
    public Object listBrands(@RequestParam("brandId") Integer brandId,@RequestParam("brandName") String brandName,
                             @RequestParam("page") Integer page,@RequestParam("limit") Integer limit){
        Object retBrands;
        if (page == null || limit == null){
            return ResponseUtil.badArgument();
        }
        // 判断参数是否合法
        boolean legal = page > 0 && limit > 0;
        if(legal){
            List<BrandPo> brandPoList=brandService.listBrands(brandId,brandName,page,limit);
            if(brandPoList.size()==0){
                retBrands= ResponseUtil.fail(795,"获取品牌列表失败");
            }else{
                retBrands=ResponseUtil.ok(brandPoList);
            }
        }else{
            retBrands=ResponseUtil.fail(795,"获取品牌列表失败");
        }
        return retBrands;
    }

    @PostMapping("/brands")
    public Object insertBrand(@RequestBody BrandPo brand){
        Object retBrand;
        boolean legal= brand!=null && !"".equals(brand.getName());
        if(legal){
            BrandPo newBrand=brandService.insertBrand(brand);
            if(newBrand==null){
                retBrand=ResponseUtil.fail(791,"品牌新增失败");
            }else{
                if(newBrand.getId()!=null){
                    retBrand=ResponseUtil.ok(newBrand);
                }else{
                    retBrand=ResponseUtil.fail(791,"品牌新增失败");
                }
            }
        }else{
            retBrand=ResponseUtil.fail(791,"品牌新增失败");
        }
        return retBrand;
    }

    @GetMapping("/brands/{id}")
    public Object getBrandById(@PathVariable("id") Integer id){
        Object retBrand;
        if(id < 1){
            return ResponseUtil.fail(794,"该品牌不存在");
        }else{
            BrandPo brandPo= brandService.getBrandById(id);
            retBrand=ResponseUtil.ok(brandPo);
        }
        return retBrand;
    }

    @PutMapping("/brands/{id}")
    public Object updateBrandById(@PathVariable("id") Integer id,@RequestBody BrandPo brand){
        Object retBrand;
        if(id<1){
            return ResponseUtil.fail(792,"品牌修改失败");
        }else{
            BrandPo brandPo=brandService.updateBrand(id,brand);
            retBrand=ResponseUtil.ok(brandPo);
        }
        return retBrand;
    }

    @DeleteMapping("/brands/{id}")
    public Object deleteBrandById(@PathVariable("id") Integer id){
        Object retBrand;
        if(id==null||id<1){
            retBrand=ResponseUtil.fail(794,"品牌删除失败");
        }else{
            int num=brandService.deleteBrand(id);
            if(num < 0){
                retBrand=ResponseUtil.fail(793,"品牌删除失败");
            }
            else{
                goodsService.changeBrandOrCategory(id,null);
                retBrand=ResponseUtil.ok();
            }
        }
        return retBrand;
    }

    @GetMapping("/brands")
    public Object userListBrands(@RequestParam(value = "page",defaultValue = "10") Integer page,@RequestParam(value = "limit", defaultValue = "10") Integer limit){
        Object retBrands;
        boolean legal = limit > 0 && page > 0;
        if(legal){
            List<BrandPo> brands=brandService.listBrands(null,"",page,limit);
            if(brands.size()==0){
                retBrands= ResponseUtil.fail(794,"该品牌不存在");
            }else{
                retBrands=ResponseUtil.ok(brands);
            }
        }else{
            retBrands=ResponseUtil.fail(794,"该品牌不存在");
        }
        return retBrands;
    }

    /**
     *                                                      GoodsCategory
     */
    @GetMapping("/categories/l1")
    public Object listCategoriesL1(){
        // pid是null的为父分类
        List<GoodsCategoryPo> goodsCategoryPoList = goodsCategoryService.listGoodsCategories(1,50,null);
        if (goodsCategoryPoList == null){
            return ResponseUtil.fail(805,"获取分类列表失败");
        } else {
            return ResponseUtil.ok(goodsCategoryPoList);
        }
    }

    @GetMapping("/categories/l1/{id}/l2")
    public Object listCategoriesL2(@PathVariable("id") Integer id){
        if (id < 0){
            return ResponseUtil.fail(805,"获取分类列表失败");
        }
        Object retCategory;
        List<GoodsCategoryPo> goodsCategoryPoList=goodsCategoryService.listGoodsCategories(1,50,id);
        if (goodsCategoryPoList == null){
            retCategory = ResponseUtil.fail(805,"获取分类列表失败");
        } else if(goodsCategoryPoList.size() == 0){
            retCategory=ResponseUtil.ok(new LinkedList<>());
        }else {
            retCategory=ResponseUtil.ok(goodsCategoryPoList);
        }
        return retCategory;
    }

    @GetMapping("/categories")
    public Object listGoodsCategories(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "limit", defaultValue = "10") Integer limit){
        Object retCategory;
        boolean legal = page > 0 && limit > 0;
        if(legal){
            List<GoodsCategoryPo> goodsCategoryPoList=goodsCategoryService.listGoodsCategories(page,limit,null);
            if (goodsCategoryPoList == null){
                retCategory = ResponseUtil.fail(805,"获取分类列表失败");
            } else if(goodsCategoryPoList.size() == 0){
                retCategory=ResponseUtil.ok(new LinkedList<>());
            }else {
                retCategory=ResponseUtil.ok(goodsCategoryPoList);
            }
        }else{
            retCategory=ResponseUtil.fail(805,"获取分类列表失败");
        }
        return retCategory;
    }

    @GetMapping("/categories/{id}")
    public Object getGoodsCategoryById(@PathVariable("id") Integer id){
        Object retCategory;
        if(id < 1){
            retCategory=ResponseUtil.fail(804,"该分类不存在");
        }else {
            GoodsCategoryPo goodsCategoryPo = goodsCategoryService.getGoodsCategoryById(id);
            if(goodsCategoryPo == null||goodsCategoryPo.getId()==0){
                retCategory=ResponseUtil.fail(804,"该分类不存在");
            }else {
                retCategory=ResponseUtil.ok(goodsCategoryPo);
            }
        }
        return retCategory;
    }

    @PostMapping("/categories")
    public Object insertGoodsCategory(@RequestBody GoodsCategoryPo goodsCategoryPo){
        Object retCategory;
        boolean legal = goodsCategoryPo.getName()!=null && !"".equals(goodsCategoryPo.getName())
                && goodsCategoryPo.getPicUrl()!=null;
        if (goodsCategoryPo.getPid() != null){
            legal = legal && goodsCategoryPo.getPid() >= 0;
        }
        if(legal){
            GoodsCategoryPo goodsCategoryPoResult=goodsCategoryService.insertGoodsCategory(goodsCategoryPo);
            if(goodsCategoryPoResult==null){
                retCategory = ResponseUtil.fail(801,"分类新增失败");
            }else if(goodsCategoryPoResult.getId()==0){
                retCategory=ResponseUtil.fail(801,"分类新增失败");
            }else{
                retCategory=ResponseUtil.ok(goodsCategoryPoResult);
            }
        }else {
            retCategory=ResponseUtil.fail(801,"分类新增失败");
        }
        return retCategory;
    }

    @PutMapping("/categories/{id}")
    public Object updateCategoryById(@PathVariable("id") Integer id,@RequestBody GoodsCategoryPo goodsCategoryPo){
        Object retCategory;
        if(id < 1){
            return ResponseUtil.fail(802,"分类修改失败");
        }else{
            GoodsCategoryPo goodsCategoryPoNew = new GoodsCategoryPo();
            goodsCategoryPoNew.setName(goodsCategoryPo.getName());
            goodsCategoryPoNew.setPicUrl(goodsCategoryPo.getPicUrl());
            goodsCategoryPoNew.setId(id);
            GoodsCategoryPo goodsCategoryPoResult = goodsCategoryService.updateGoodsCategory(goodsCategoryPoNew);
            if (goodsCategoryPoResult == null || goodsCategoryPoResult.getId() == 0){
                retCategory = ResponseUtil.fail(802,"分类修改失败");
            }else{
                retCategory = ResponseUtil.ok(goodsCategoryPoResult);
            }
        }
        return retCategory;
    }

    @PutMapping("/categories/l2/{id}")
    public Object updateL2Category(@PathVariable("id") Integer id,@RequestBody GoodsCategoryPo goodsCategoryPo){
        Object retCategory;
        if (id < 0 || goodsCategoryPo.getPid() == null || goodsCategoryPo.getPid() < 0){
            return ResponseUtil.fail(802,"分类修改失败");
        }else{
            GoodsCategoryPo goodsCategoryPoNew = new GoodsCategoryPo();
            goodsCategoryPoNew.setId(id);
            goodsCategoryPoNew.setPid(goodsCategoryPo.getPid());
            GoodsCategoryPo goodsCategoryPoResult = goodsCategoryService.moveL2Category(goodsCategoryPoNew);
            if (goodsCategoryPoResult == null || goodsCategoryPoResult.getId() == 0){
                retCategory = ResponseUtil.fail(802,"分类修改失败");
            }else{
                retCategory = ResponseUtil.ok(goodsCategoryPoResult);
            }
        }
        return retCategory;
    }

    @DeleteMapping("/categories/{id}")
    public Object deleteCategoryById(@PathVariable("id") Integer id){
        Object retCategory;
        if(id < 1){
            return ResponseUtil.fail(804,"分类删除失败");
        }else {
            GoodsCategoryPo goodsCategoryPo = goodsCategoryService.getGoodsCategoryById(id);
            if (goodsCategoryPo == null){
                return ResponseUtil.fail(803,"分类删除失败");
            }
            if(goodsCategoryPo.getId()==0){
                return ResponseUtil.fail(804,"不存在");
            }
            // 如果是一级分类，更新二级分类
            if (goodsCategoryPo.getPid() == null){
                goodsCategoryService.updateLevel2(id);
                goodsCategoryService.deleteGoodsCategory(id);
                retCategory = ResponseUtil.ok();
            }else{
                // 二级分类或无分类的话更新商品信息
                goodsService.changeBrandOrCategory(null,id);
                goodsCategoryService.deleteGoodsCategory(id);
                retCategory = ResponseUtil.ok();
            }
        }
        return retCategory;
    }
}
