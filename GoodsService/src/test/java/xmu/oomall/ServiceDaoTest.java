//package xmu.oomall;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import xmu.oomall.dao.ProductsDao;
//import xmu.oomall.domain.goods.Product;
//import xmu.oomall.domain.goods.ProductPo;
//import xmu.oomall.service.ProductService;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ServiceDaoTest {
//    @Autowired
//    private ProductsDao productsDao;
//
////    @Autowired
////    private ProductService productService;
//    @Test
//    public void test1(){
//        List<ProductPo>productPos=productsDao.listProductsByGoodsId(1006002);
//        for(ProductPo productPo:productPos){
//            System.out.println(productPo);
//        }
//    }
//
//    @Test
//    public void test2(){
//        ProductPo productPos=productsDao.getProduct(1);
//        System.out.println(productPos);
//    }
//    @Test
//    public void test3() throws Exception {
//        ProductPo productPo=new ProductPo();
//        productPo.setGoodsId(10086);
//        productPo.setPrice(new BigDecimal(100));
//        productPo.setSafetyStock(100);
//        productPo.setSpecifications("小自然的搬运工");
//        int productPos=productsDao.insertProduct(productPo);
//        System.out.println(productPos);
//    }
////    @Test
//    public void test4(){
//        int productPos=productsDao.deleteProductByProductId(1);
//        System.out.println(productPos);
//    }
////    @Test
////    public void fuck() throws Exception {
////        List<Product> products=productService.listProductByGoodsId(10086);
////        for(Product product:products){
////            System.out.println(product.getId());
////        }
////    }
//}
