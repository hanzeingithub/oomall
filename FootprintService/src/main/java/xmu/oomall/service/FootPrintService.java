package xmu.oomall.service;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import xmu.oomall.domain.FootprintItem;
import xmu.oomall.domain.FootprintItemPo;

import java.util.List;

/**
 * FootPrintServiceImpl
 *
 * @author YangHong
 * @date 2019-12-10
 */
@Service
@Repository
public interface FootPrintService {

    /**
     * 查询足迹的Vo
     * @param userId 用户名
     * @param goodsId 商品名
     * @param page 第几页
     * @param limit 分页大小
     * @return FootPrintItem的列表，数据库错误返回空
     */
    List<FootprintItem> listFootprintItem(Integer userId, Integer goodsId, Integer page, Integer limit);

    /**
     * 增加一条足迹记录，需要注意到，如果查到userId和goodsId相同的记录就去更新它的birthTime
     * @param footPrintItem FootPrint实体类
     * @return 增加的数目，数据库错误返回-1。
     */
    FootprintItemPo insertFootPrint(FootprintItemPo footPrintItem);

    /**
     * 删除足迹记录
     * todo 足迹要怎么删除
     * @param id 需要删除的id列表
     * @return 删除的数目,如果是0则说明没有删除，数据库错误返回-1。
     */
    int deleteFootPrint(Integer id);
}
