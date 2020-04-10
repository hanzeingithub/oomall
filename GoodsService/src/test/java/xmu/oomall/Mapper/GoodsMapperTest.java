//package xmu.oomall.Mapper;
//import xmu.oomall.dao.GoodsDao;
//import xmu.oomall.domain.goods.Goods;
//import xmu.oomall.mapper.GoodsMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import java.time.LocalDateTime;
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
//public class GoodsMapperTest {
//    private static final Logger logger = LoggerFactory.getLogger(GoodsMapperTest.class);
//    @Autowired
//    private GoodsDao goodsMapper;
//    @Test
//    public void insertTest(){
//        Goods goods = new Goods();
//        goods.setGmtCreate(LocalDateTime.now());
//        goods.setBrandId(1);
//        goods.setBeDeleted(true);
//        goods.setGoodsCategoryId(1);
//        goods.setName("uziyyyy");
//        goodsMapper.insertGoods(goods);
//        goods.setId(3);
//        // 增加一个id为3的
//        goodsMapper.insertGoods(goods);
//        logger.info(goods.toString());
//    }
//    @Test
//    public void updateTest(){
//        Goods goods = new Goods();
//        goods.setGmtModified(LocalDateTime.now());
//        goods.setBrandId(1);
//        goods.setId(1);
//        goods.setGoodsCategoryId(1);
//        goods.setName("uzizzzz");
//        goods.setId(1);
//        goodsMapper.updateGoodsById(goods);
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
//
//    @Test
//    public void deleteById(){
//        // 删除一个id为3的
//        goodsMapper.deleteGoodsById(3);
//
//    }
//}
