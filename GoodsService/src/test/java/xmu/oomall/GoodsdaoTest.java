//package xmu.oomall;
//import xmu.oomall.dao.GoodsDao;
//import xmu.oomall.domain.goods.Goods;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import xmu.oomall.domain.goods.GoodsPo;
//
//import java.util.List;
//
///**
// * GoodsMapperTest
// *
// * @author YangHong
// * @date 2019-12-06
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GoodsdaoTest {
//    private static final Logger logger = LoggerFactory.getLogger(GoodsdaoTest.class);
//    @Autowired
//    private GoodsDao goodsDao;
//    @Test
//    public void listGoods() throws Exception {
//        List<GoodsPo>goods=goodsDao.listGoodsByBrand(100600,1,3);
//        for(GoodsPo good:goods)
//            logger.info(good.toString());
//
//
//    }
//    public void insertTest(){
//        Goods goods = new Goods();
//        goods.setBrandId(1);
//        goods.setGoodsCategoryId(1);
//        goods.setName("uziyyyy");
//        goodsMapper.insertGoods(goods);
//        logger.info(goods.toString());
//    }
//    @Test
//    public void updateTest(){
//        Goods goods = new Goods();
//        goods.setGmtCreate(LocalDateTime.now());
//        goods.setBrandId(1);
//        goods.setId(1);
//        goods.setGoodsCategoryId(1);
//        goods.setName("uzizzzz");
//        goodsMapper.updateGoodsById(1,goods);
//        logger.info(goods.toString());
//    }
//    @Test
//    public void selectTest() {
//        logger.info(goodsMapper.getGoodsById(1).toString());
//    }
//
//    @Test
//    public void selectByQueryTest() {
//        List<Goods> goods = goodsMapper.getGoodsByQuery(1,null,null);
//        logger.info("brand\n"+goods.toString());
//        goods = goodsMapper.getGoodsByQuery(null,1,null);
//        logger.info("categoryId\n"+goods.toString());
//        goods = goodsMapper.getGoodsByQuery(null,null,"uzi");
//        logger.info("name\n"+goods.toString());
//        goods = goodsMapper.getGoodsByQuery(null,null,null);
//        logger.info("all\n"+goods.toString());
//    }
//}
