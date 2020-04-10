package xmu.oomall.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.cart.CartItem;
import xmu.oomall.domain.cart.CartItemPo;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.domain.goods.Product;
import xmu.oomall.domain.goods.ProductPo;
import xmu.oomall.feign.GoodsFeign;
import xmu.oomall.mapper.CartMapper;

import xmu.oomall.util.JacksonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanzelegend
 */
@Repository
public class CartDao {

    @Autowired
    private CartMapper cartMapper;
    @Resource
    private GoodsFeign goodsFeign;

    private Boolean isOk(CartItem cartItem){
        Object result=goodsFeign.getGoodsById(cartItem.getProduct().getGoodsId());
        String body= JacksonUtil.toJson(result);
        Integer errno=JacksonUtil.parseInteger(body,"errno");
        Integer success=0;
        Goods goods=JacksonUtil.parseObject(body,"data",Goods.class);
        //此时不存在该商品或者商品已下架
        if(success.equals(errno)||goods==null){
            return false;
        }
        for(ProductPo productPo:goods.getProductPoList()){
            if(productPo.getId().equals(cartItem.getProductId())&&cartItem.getNumber()<=productPo.getSafetyStock()){
                return true;
            }
        }
        return false;
    }
    private CartItem packagingCartItem(CartItemPo cartItem){
        Object result=goodsFeign.getProductsById(cartItem.getProductId());
        String body= JacksonUtil.toJson(result);
        Integer errno=JacksonUtil.parseInteger(body,"errno");
        Integer success=0;
        CartItem cart=new CartItem(cartItem);
        if(success.equals(errno)){
            Product product=JacksonUtil.parseObject(body,"data",Product.class);
            cart.setProduct(product);
        }
        return cart;
    }

    /**
     * 根据用户ID返回该用户的购物车
     * @param userId 购物车所属于的用户ID
     * @return cartItem列表
     */
    public List<CartItem> listCartItem(Integer userId,Integer productId){
        try {
            List<CartItemPo>cartItemPos=cartMapper.listCartItem(userId, productId);
            List<CartItem>cartItems=new ArrayList<>();
            for(CartItemPo cartItemPo:cartItemPos){
                cartItems.add(packagingCartItem(cartItemPo));
            }
            return cartItems;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据ID返回该用户的购物车
     * @param id 购物车的ID
     * @return cartItem列表
     */
    public CartItem getCartItem(Integer id){
        try{
            CartItemPo cartItem=cartMapper.getCartItem(id);
            if(cartItem==null){
                CartItem theCart=new CartItem();
                theCart.setId(0);
                return theCart;
            }
            return packagingCartItem(cartItem);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据购物车的ID来删除该条购物车记录
     * @param cartId 该条购物车的id
     * @return 删除数据的条数
     */
    public int deleteCartItem(Integer cartId){
        try{
            return cartMapper.deleteCartItem(cartId);
        }catch (Exception e){
            return -1;
        }

    }

    /**
     * 向数据库中添加一条新的购物车记录
     * @param cartItem 新的购物车记录
     * @return 添加数据的条数
     */
    public CartItem insertCartItem(CartItem cartItem){
        try {
            if(isOk(cartItem)) {
                int i = cartMapper.addCartItem(cartItem);
                if (i == 0) {
                    cartItem.setId(0);
                }
                return cartItem;
            }
            cartItem.setId(0);
            return cartItem;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 向数据库中更新一条新的购物车记录
     * @param cartItem 新的购物车记录
     * @return 更改数据的条数
     */
    public CartItem updateCartItem(CartItem cartItem){
        try{
            if(isOk(cartItem)) {
                int num = cartMapper.updateCartItem(cartItem);
                if (num == 0) {
                    cartItem.setId(0);
                    return cartItem;
                }
                CartItemPo cartItemPo=cartMapper.getCartItem(cartItem.getId());
                CartItem newCartItem=new CartItem(cartItemPo);
                newCartItem.setProduct(cartItem.getProduct());
                return newCartItem;
            }
            cartItem.setId(0);
            return cartItem;
        }catch (Exception e){
            return null;
        }
    }
}
