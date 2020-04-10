package xmu.oomall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmu.oomall.domain.cart.CartItem;
import xmu.oomall.service.CartService;
import xmu.oomall.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author hanzelegend
 */
@RestController
@RequestMapping(value = "")
public class CartController {
    @Autowired
    private CartService cartService;

    private Integer getUserId(HttpServletRequest request){
        String userId=request.getHeader("userId");
        if(userId==null){
            return null;
        }
        return Integer.valueOf(userId);
    }
    private Object addCartItem( CartItem cartItem,HttpServletRequest request){
        Integer userId = getUserId(request);
        if (userId == null) {
            return ResponseUtil.fail(660,"用户未登录");
        }
        if (cartItem.getUserId() == null) {
            cartItem.setUserId(userId);
        }
        if (cartItem.getUserId() == null ||
                cartItem.getProductId() == null ||
                cartItem.getNumber() == null) {
            return ResponseUtil.fail(500,"查询数据不合法");
        }
        if (cartItem.getUserId() < 1 || cartItem.getProductId() < 1) {
            return ResponseUtil.fail(500,"查询数据不合法");
        }
        if (!cartItem.getUserId().equals(userId)) {
            return ResponseUtil.fail(665,"用户未登录");
        }
        CartItem cart = cartService.insertCartItem(cartItem);
        if(cart==null){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        if(cart.getId().equals(0)){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        return ResponseUtil.ok(cartItem);
    }

    /*内部接口*/
    @RequestMapping(value="/cartItems/{id}",method = RequestMethod.GET)
    public @ResponseBody Object getCart(@PathVariable Integer id, HttpServletRequest request){
        CartItem cartItems=cartService.getCartItem(id);
        if(cartItems==null){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        else if(cartItems.getId().equals(0)){
            return ResponseUtil.fail(730,"购物车记录不存在");
        }
        else{
            return ResponseUtil.ok(cartItems);
        }
    }

    /*非内部接口*/

    @RequestMapping(value="/cartItems",method = RequestMethod.GET)
    public @ResponseBody Object listAllCart(HttpServletRequest request){
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(660,"用户未登录");
        }
        List<CartItem> cartItems=cartService.listCartItem(userId);
        if(cartItems==null){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        else if(cartItems.isEmpty()){
            return ResponseUtil.fail(730,"购物车记录不存在");
        }
        else{
            return ResponseUtil.ok(cartItems);
        }
    }

    @RequestMapping(value="/cartItems/{id}",method = RequestMethod.PUT)
    public @ResponseBody Object updateCart(@PathVariable Integer id,@RequestBody CartItem params, HttpServletRequest request) {
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(660,"用户未登录");
        }
        if(!params.getId().equals(id)){
            return ResponseUtil.fail(500,"查询数据不合法");
        }
        if(params.getUserId()==null){
            params.setUserId(userId);
        }
        if(!params.getUserId().equals(userId)){
            return ResponseUtil.fail(665,"用户无权限操作");
        }
        if(params.getUserId()<1){
            return ResponseUtil.fail(500,"查询数据不合法");
        }
        CartItem cartItem=cartService.updateCartItem(params);
        if(cartItem==null){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        if(cartItem.getId().equals(0)){
            return ResponseUtil.fail(730,"购物车记录不存在");
        }
        return ResponseUtil.ok(cartItem);
    }

    @RequestMapping(value = "/cartItems",method = RequestMethod.POST)
    public @ResponseBody Object addCart(@RequestBody CartItem cartItem,HttpServletRequest request) {
       cartItem.setBeCheck(false);
       return addCartItem(cartItem,request);
    }

    @RequestMapping(value = "/cartItems/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Object deleteCart(@PathVariable Integer id,HttpServletRequest request){
        Integer userId = getUserId(request);
        if (userId == null) {
            return ResponseUtil.fail(660,"用户未登录");
        }
        if(id==null){
            return ResponseUtil.fail(500,"查询数据不合法");
        }
        int i=cartService.deleteCartItem(id);
        if(i==0){
            return ResponseUtil.fail(730,"购物车记录不存在");
        }
        else if(i==-1){
            return ResponseUtil.fail(731,"购物车操作失败");
        }
        else{
            return ResponseUtil.ok(i);
        }
    }

//    @GetMapping("/cartItem/{userId}")
//    public @ResponseBody Object forOrder(@PathVariable Integer userId,HttpServletRequest request){
//        List<CartItem> cartItems=cartService.listCartItem(userId);
//        if(cartItems==null){
//            return ResponseUtil.serious();
//        }
//        else{
//            return ResponseUtil.ok(cartItems);
//        }
//    }

    @RequestMapping(value = "/fastAddCartItems",method = RequestMethod.POST)
    public @ResponseBody Object addFastCart(@RequestBody CartItem cartItem,HttpServletRequest request) {
        cartItem.setBeCheck(true);
        return addCartItem(cartItem, request);
    }
}
