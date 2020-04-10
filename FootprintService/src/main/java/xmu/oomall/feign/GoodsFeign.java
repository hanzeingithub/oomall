package xmu.oomall.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * FootprintDao
 *
 * @author YangHong
 * @date 2019-12-11
 */
@Component
@FeignClient(value = "goodsServices")
public interface GoodsFeign {
    /**
     * goods的接口，获取goods的信息
     * @param id goods的id
     * @return 一个封装好的信息
     */
    @RequestMapping(value = "inner/goods/{id}", method = RequestMethod.GET)
    Object getGoodsById(@PathVariable("id") Integer id);

}
