//package xmu.oomall.Mapper;
//
//import xmu.oomall.domain.goods.ProductPo;
//import xmu.oomall.mapper.ProductMapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.LinkedList;
//import java.util.List;
//
//
///**
// * ProductMapperTest
// *
// * @author YangHong
// * @date 2019-12-07
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ProductMapperTest {
//    private static final Logger logger = LoggerFactory.getLogger(ProductMapperTest.class);
//    @Autowired
//    private ProductMapper productMapper;
//    @Test
//    public void insertTest(){
//        ProductPo product = new ProductPo();
//
//        product.setGoodsId(1);
//        product.setGmtCreate(LocalDateTime.now());
//        product.setSpecifications("有种原因是因为产生了一个*map.out.xml空文件,删掉就好");
//        List<ProductPo> products = new LinkedList<>();
//        products.add(product);
//        products.add(product);
//
//        // 一次插两条
//        productMapper.insertProduct(products);
//        logger.info(products.toString());
//
//        // 一次插一条
//        products = new LinkedList<>();
//        product.setId(1);
//        products.add(product);
//        productMapper.insertProduct(products);
//        logger.info(product.toString());
//    }
//
//    @Test
//    public void updateTest(){
//        ProductPo product = new ProductPo();
//        product.setGmtModified(LocalDateTime.now());
//        product.setGoodsId(1);
//        product.setSpecifications("this is a update test");
//        product.setId(1);
//        productMapper.updateProduct(product);
//        logger.info(product.toString());
//    }
//
//   @Test
//    public void getTest(){
//        //根据id查product
//       List<ProductPo> products = productMapper.listProductsByGoodsId("id", 1);
//       logger.info(products.toString());
//
//       //根据商品id查products
//       products = productMapper.listProductsByGoodsId("goods_id", 1);
//       logger.info(products.toString());
//   }
//   @Test
//    public void deleteTest(){
//        // 批量删除，一次删一条
//        List<Integer> ids = new LinkedList<>();
//        ids.add(1);
//        productMapper.deleteProductById(ids);
//        logger.info("delete 1");
//   }
//}
