package xmu.oomall.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {
    @Autowired
    ProductService productService;

    @Autowired
    GoodsService goodsService;

    @Test
    public void listByName(){
        List<Goods> goods=goodsService.listGoodsForUser("水",1,1);
        for(Goods goods1:goods){
            System.out.println(goods1);
        }
    }

//    /**
//     * 根据id获取Goods信息
//     * @param id goods的id
//     * @return Goods
//     */
//    Goods getGoodsById(Integer id) throws Exception;
    @Test
    public void getGoods() throws Exception {
        System.out.println(goodsService.getGoodsById(1006002,10086));
    }

//
//    /**
//     * 管理员查找商品的信息
//     * @param page 分页数
//     * @param limit 分页大小
//     * @param name 商品名
//     * @param goodsSn 商品序列
//     * @return GoodsPo的列表
//     */
//    List<GoodsPo> listGoodsPo(Integer page, Integer limit, String name, String goodsSn);
    @Test
    public void getByGoodsSn(){
        List<GoodsPo> goodsPos=goodsService.listGoodsPo(1,2,null,"10");
    }
//
//    /**
//     * 增加一件商品
//     * @param goodsPo 商品的po
//     * @return 增加后的商品
//     */
//    GoodsPo insertGoods(GoodsPo goodsPo);
    @Test
    public void insertGoods(){
        GoodsPo goodsPo=new GoodsPo();
        goodsPo.setGoodsCategoryId(222);
        goodsPo.setStatusCode(1);
        goodsPo.setBeDeleted(false);
        goodsPo.setBrandId(222);
        try{
            System.out.println(goodsService.insertGoods(goodsPo));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//
//    /**
//     * 管理员更新商品
//     * @param goodsPo 商品的对象
//     * @return 更新后的goodsPo
//     */
//    GoodsPo updateGoods(GoodsPo goodsPo);
    @Test
    public void updateGoods(){
        GoodsPo goodsPo=new GoodsPo();
        goodsPo.setId(1006002);
        goodsPo.setStatusCode(2);
        System.out.println(goodsService.updateGoods(goodsPo));
    }
//
//    /**
//     * 管理员删除商品，需要将该goods的所有product删除，以及删除评论（可以放在deleteProduct做）
//     * @param id 商品id
//     * @return 删除结果
//     */
//    int deleteGoods(Integer id);
//
//    /**
//     * 根据brand查询商品
//     * @param brandId 品牌的id
//     * @return 查询到的商品列表
//     */
//    List<Goods>listGoodsByBrand(Integer brandId, Integer limit, Integer page) throws Exception;
//
//    /**
//     * 根据category来查找goods
//     * @param categoryId category的id
//     * @return Goods的列表
//     */
//    List<Goods>listGoodsByCategory(Integer categoryId, Integer limit, Integer page) throws Exception;
}
