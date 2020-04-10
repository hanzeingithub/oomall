package xmu.oomall.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author hanzelegend
 */

@FeignClient(value = "goodsService")
public interface GoodsFeign {

    /**
     * 用于与goods模块之间进行交互，确认评论的合法性
     * @param id goods的id
     * @return 目标模块的返回值
     */
    @RequestMapping(value="/goods/{id}", method=RequestMethod.GET)
    Object getGoodsById(@PathVariable Integer id);

}
