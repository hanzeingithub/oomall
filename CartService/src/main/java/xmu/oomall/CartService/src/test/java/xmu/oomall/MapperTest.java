//package xmu.oomall;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import xmu.oomall.domain.cart.CartItem;
//import xmu.oomall.mapper.CartMapper;
//
//import java.time.LocalDateTime;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MapperTest {
//    private static final Logger logger = LoggerFactory.getLogger(MapperTest.class);
//    @Autowired
//    private CartMapper cartMapper;
//
//    @Test
//    public void InsertTest() throws Exception{
//        CartItem cartItem=new CartItem();
//        cartItem.setUserId(1226);
//        cartItem.setGmtCreate(LocalDateTime.now());
//        cartItem.setBeCheck(false);
//        cartItem.setNumber(10086);
//        cartItem.setProductId(100);
//        cartItem.setUserId(101);
//        int i=cartMapper.addCartItem(cartItem);
//        logger.info(Integer.toString(i));
//    }
//    @Test
//    public void findTest() throws Exception{
//
//        CartItem i=cartMapper.getCartItem(5);
//        logger.info(i.toString());
//    }
//    @Test
//    public void deleteTest() throws Exception{
//
//        int i=cartMapper.deleteCartItem(5);
//        logger.info(Integer.toString(i));
//    }
//    @Test
//    public void updateTest() throws Exception{
//        CartItem cartItem=new CartItem();
//        cartItem.setId(1);
//        cartItem.setUserId(109);
//        cartItem.setGmtModified(LocalDateTime.now());
//        cartItem.setNumber(16);
//        cartItem.setProductId(1100);
//        int i=cartMapper.updateCartItem(cartItem);
//        logger.info(Integer.toString(i));
//    }
//}
