package xmu.oomall.domain.cart;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xmu.oomall.domain.goods.Product;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:购物车明细对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class  CartItem extends CartItemPo {

    private Product product;

    public CartItem(){
        super();
    }
    public CartItem(CartItemPo cartItem){
        this.setBeCheck(cartItem.getBeCheck());
        this.setGmtCreate(cartItem.getGmtCreate());
        this.setGmtModified(cartItem.getGmtModified());
        this.setId(cartItem.getId());
        this.setNumber(cartItem.getNumber());
        this.setProductId(cartItem.getProductId());
        this.setUserId(cartItem.getUserId());
    }
}
