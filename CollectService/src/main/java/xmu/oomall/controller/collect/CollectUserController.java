package xmu.oomall.controller.collect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmu.oomall.domain.collectitem.CollectItem;
import xmu.oomall.domain.collectitem.CollectItemPo;
import xmu.oomall.service.CollectionService;
import xmu.oomall.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * collect
 *
 * @author YangHong
 * @date 2019-12-06
 */
@RestController
@RequestMapping("/collections")
public class CollectUserController {
    @Autowired
    private CollectionService collectionService;

    private Integer getUserId(HttpServletRequest request){
        String userId=request.getHeader("userId");
        if(userId==null){
            return null;
        }
        return Integer.valueOf(userId);
    }

    @GetMapping("")
    public Object getCollectItems(@RequestParam("page")Integer page,@RequestParam("limit")Integer limit, HttpServletRequest request){
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(660,"用户未登录");
        }
        if (page == null){
            page = 1;
        }
        if (limit == null){
            limit = 10;
        }
        // 判断参数是否合法
        boolean legal = limit > 0 && page > 0;
        if(legal) {
            List<CollectItem> collectItems = collectionService.listCollect(userId, null, page, limit);
            if(collectItems==null){
                return ResponseUtil.fail(711,"收藏操作失败");
            }
            else if(collectItems.isEmpty()){
                return ResponseUtil.fail(700,"收藏记录不存在");
            }
            return ResponseUtil.ok(collectItems);
        }
        else {
            return ResponseUtil.fail(500,"查询数据不合法");
        }
    }

    @PostMapping("")
    public Object submitCollect(@RequestBody CollectItemPo collectItemPo,HttpServletRequest request){
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(660,"用户未登录");
        }
        collectItemPo.setUserId(userId);
        CollectItemPo collectItem=collectionService.addCollectItem(collectItemPo);
        if(collectItem==null){
            return ResponseUtil.fail(711,"收藏操作失败");
        }
        if(collectItem.getId().equals(0)){
            return ResponseUtil.fail(710,"收藏记录不存在");
        }
        return ResponseUtil.ok(collectItem);
    }

    @DeleteMapping("/{id}")
    public Object deleteCollect(@PathVariable("id") Integer id,HttpServletRequest request) {
        Integer userId = getUserId(request);
        if (userId == null) {
            return ResponseUtil.fail(660,"用户未登录");
        }
        int i = collectionService.deleteCollectionById(id);
        if (i <0) {
            return ResponseUtil.fail(711,"收藏操作失败");
        } else if (i == 0) {
            return ResponseUtil.fail(710,"收藏记录不存在");
        } else {
            return ResponseUtil.ok(i);
        }
    }

}
