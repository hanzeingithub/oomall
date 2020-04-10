package xmu.oomall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.cart.CartItem;
import xmu.oomall.domain.cart.CartItemPo;

import java.util.List;

/**
 * @author hanzelegend
 */
@Mapper
@Repository
public interface CartMapper {
    /**
     * 根据用户ID返回该用户的购物车
     * @param userId 购物车所属于的用户ID
     * @param productId 产品的id
     * @return cartItem列表
     */
    List<CartItemPo> listCartItem(@Param("userId")Integer userId,@Param("productId")Integer productId);

    /**
     * 根据用户ID返回该用户的购物车
     * @param id 购物车的ID
     * @return cartItem列表
     */
    CartItemPo getCartItem(@Param("id")Integer id);

    /**
     * 根据购物车的ID来删除该条购物车记录
     * @param cartId 该条购物车的id
     * @return 删除数据的条数
     */
    int deleteCartItem(@Param("cartId")Integer cartId);

    /**
     * 向数据库中添加一条新的购物车记录
     * @param cartItem 新的购物车记录
     * @return 添加数据的条数
     */
    int addCartItem(@Param("cartItem")CartItemPo cartItem);

    /**
     * 向数据库中更新一条新的购物车记录
     * @param cartItem 新的购物车记录
     * @return 更改数据的条数
     */
    int updateCartItem(@Param("cartItem") CartItemPo cartItem);
}
