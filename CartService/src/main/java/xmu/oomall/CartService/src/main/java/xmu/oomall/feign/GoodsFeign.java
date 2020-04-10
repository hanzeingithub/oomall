package xmu.oomall.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author hanzelegend
 */

@FeignClient(value = "goodsServices")
public interface GoodsFeign {

    /**
     * 用于和goods模块连接，用于封装一个cart
     * @param id goods的id
     * @return 目标模块返回的信息
     */
    @RequestMapping(value="/goods/{id}", method=RequestMethod.GET)
    Object getGoodsById(@PathVariable Integer id);

    /**
     * 用于和goods模块连接，用于检查一个cart的合法性
     * @param id product的id
     * @return 目标模块返回的信息
     */
    @RequestMapping(value="/user/product/{id}", method=RequestMethod.GET)
    Object getProductsById(@PathVariable Integer id);
}
