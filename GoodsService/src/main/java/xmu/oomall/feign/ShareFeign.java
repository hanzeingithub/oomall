package xmu.oomall.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hanzelegend
 */
@FeignClient(value = "ShareService")
public interface ShareFeign {

    /**
     *与外部的模块做交互获得对应商品的分享规则
     * @param id 商品id
     * @return 对面返回的东西
     */
    @GetMapping("/goods/{id}/shareRules")
    public Object getShareRule(@PathVariable("id") Integer id);
}
