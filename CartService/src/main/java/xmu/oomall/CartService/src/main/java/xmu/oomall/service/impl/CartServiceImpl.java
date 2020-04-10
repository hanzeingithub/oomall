package xmu.oomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.oomall.dao.CartDao;
import xmu.oomall.domain.cart.CartItem;
import xmu.oomall.domain.cart.CartItemPo;
import xmu.oomall.mapper.CartMapper;
import xmu.oomall.service.CartService;

import java.util.List;

/**
 * @author hanzelegend
 */
@Service

public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao myCartDao;
    @Autowired
    private CartMapper cartMapper;
    /**
     * 根据用户ID返回该用户的购物车
     * 需要调用user模块来查看是否存在该用户
     * @param userId 购物车所属于的用户ID
     * @return cartItem列表
     */
    @Override
    public List<CartItem> listCartItem(Integer userId) {
        return myCartDao.listCartItem(userId, null);
    }

    /**
     * 根据用户ID返回该用户的购物车
     * @param id 购物车的ID
     * @return cartItem列表
     */
    @Override
    public CartItem getCartItem(Integer id) {
        return myCartDao.getCartItem(id);
    }

    /**
     * 根据购物车的ID来删除该条购物车记录
     * @param cartId 该条购物车的id
     * @return 删除数据的条数
     */
    @Override
    public int deleteCartItem(Integer cartId) {
        return myCartDao.deleteCartItem(cartId);
    }

    /**
     * 向数据库中添加一条新的购物车记录
     * 需要调用goods模块来查看是否存在该product,并且该goods是否已经下架
     * @param cartItem 新的购物车记录
     * @return 添加数据的条数
     */
    @Override
    public CartItem insertCartItem(CartItem cartItem){
            List<CartItemPo> cartItems = cartMapper.listCartItem(cartItem.getUserId(), cartItem.getProductId());
            if(cartItems==null){
                return null;
            }
            if (cartItems.size() != 0&&!cartItem.getBeCheck()) {
                int n = cartItem.getNumber() + cartItems.get(0).getNumber();
                cartItem.setNumber(n);
                cartItem.setId(cartItems.get(0).getId());
                return myCartDao.updateCartItem(cartItem);
            }
            else {
                return myCartDao.insertCartItem(cartItem);
            }
    }

    /**
     * 向数据库中更新一条新的购物车记录
     * 需要调用goods模块查看更新后的product_id是否和原product属于同一个good
     * @param cartItem 新的购物车记录
     * @return 更改数据的条数
     */
    @Override
    public CartItem updateCartItem(CartItem cartItem) {
        return myCartDao.updateCartItem(cartItem);
    }
}