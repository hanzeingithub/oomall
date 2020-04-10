package xmu.oomall.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hanzelegend
 */
@FeignClient(value = "commentService")
public interface CommentFeign {
    @DeleteMapping("/product/{id}/comments")
    /**
     * 根据品牌删除评论
     * @param id product的id
     * @return 目标模块返回的东西
     */
    Object deleteCommentByProduct(@PathVariable("id") Integer id);
}
