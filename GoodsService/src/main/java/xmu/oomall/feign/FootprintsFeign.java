package xmu.oomall.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xmu.oomall.domain.footprintitem.FootprintItemPo;

/**
 * @author hanzelegend
 */
@FeignClient("footprintService")
public interface FootprintsFeign {
    /**
     * 用来在用户访问商品之后添加足迹
     * @param footprintItemPo 足迹的基本信息
     * @return 是否成功
     */
    @PostMapping("/footprints")
    public Object addFootPrint(@RequestBody FootprintItemPo footprintItemPo);
}
