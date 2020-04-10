package xmu.oomall.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xmu.oomall.domain.FootprintItem;
import xmu.oomall.domain.FootprintItemPo;
import xmu.oomall.service.FootPrintService;
import xmu.oomall.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
/**
 * FootprintController
 *
 * @author YangHong
 * @date 2019-12-10
 */
@RequestMapping("")
@RestController
public class FootprintController {
    @Autowired
    private FootPrintService footPrintService;

    private Integer getUserId(HttpServletRequest request){
        String userId=request.getHeader("userId");
        if(userId==null){
            return null;
        }
        return Integer.valueOf(userId);
    }

    @GetMapping("/footprints")
    public Object getWxFootptintItems( @RequestParam("page") Integer page,
                                       @RequestParam("limit") Integer limit,
                                       HttpServletRequest request){
        Integer userId=getUserId(request);
        if(userId==null){
            return ResponseUtil.fail(660,"用户未登陆");
        }
        boolean legal;
        if (page == null || limit == null){
            return ResponseUtil.fail(740,"足迹不存在");
        }else{
            legal = page>0 && limit > 0;
        }
        if (legal){
            List<FootprintItem> footPrintsItems = footPrintService.listFootprintItem(userId,null,page,limit);
            if(footPrintsItems == null){
                return  ResponseUtil.fail(740,"足迹不存在");
            }else if (footPrintsItems.size() == 0){
                return  ResponseUtil.ok(new LinkedList<>());
            }else{
                return  ResponseUtil.ok(footPrintsItems);
            }
        }else{
            return  ResponseUtil.fail(740,"足迹不存在");
        }
    }

    /**
     * 查询footprintItem记录，若是userId和goodsId都是空，则默认都查
     * @param userId 用户id
     * @param goodsId 商品id
     * @param page 页数
     * @param limit 分页大小
     * @return 足迹信息
     */
    @GetMapping("/admin/footprints")
    public Object getFootPrintItems(@RequestParam(value = "userId", required = false)Integer userId,
                                    @RequestParam(value = "goodsId",required = false) Integer goodsId,
                                    @RequestParam(value = "page",defaultValue = "1") Integer page,
                                    @RequestParam(value = "limit",defaultValue = "10") Integer limit,
                                    HttpServletRequest request){
        Object retFootprint;
        boolean legal;
        if (page == null || limit == null){
            return ResponseUtil.fail(740,"足迹不存在");
        }else{
            legal = page>0 && limit > 0;
        }
        if (userId != null){
            legal = legal && userId>0;
        }
        if (goodsId != null){
            legal = legal && goodsId>0;
        }
        if(legal){
            List<FootprintItem> footPrintsItems = footPrintService.listFootprintItem(userId, goodsId, page, limit);
            if (footPrintsItems == null){
                return  ResponseUtil.ok();
            }else {
                return  ResponseUtil.ok(footPrintsItems);
            }
        }else{
            return  ResponseUtil.fail(740,"足迹不存在");
        }
    }

    /**
     *内部接口:添加足迹信息/add
     *@param footprintItemPo 管理员足迹记录
     */
    @PostMapping("/footprints")
    public Object addFootPrint(@RequestBody FootprintItemPo footprintItemPo){
        // 判断参数是否合法
        boolean legal = footprintItemPo.getUserId() != null && footprintItemPo.getGoodsId() != null;
        if (legal){
            if(footprintItemPo.getGoodsId() != null && footprintItemPo.getUserId() != null){
                legal = footprintItemPo.getGoodsId()>0 && footprintItemPo.getUserId()>0;
            }else{
                legal = false;
            }
        }
        if (legal){
            FootprintItemPo footPrintItemResult = footPrintService.insertFootPrint(footprintItemPo);
            if (footPrintItemResult == null){
                return  ResponseUtil.fail(741,"足迹添加失败");
            } else{
                return  ResponseUtil.ok();
            }
        }else{
            return  ResponseUtil.fail(741,"足迹添加失败");
        }
    }
}
