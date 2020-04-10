package xmu.oomall.service;

import xmu.oomall.domain.collectitem.CollectItem;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.collectitem.CollectItemPo;

import java.util.List;

/**
 * @author hanzelegend
 */
@Repository
public interface CollectionService {
    /**
     * 根据用户ID和商品ID找到该用户的收藏夹
     * @param userId 收藏夹所属于的用户ID
     * @param goodsId 商品的id
     * @param page 页数
     * @param limit 每页个数
     * @return collectItem列表
     *
     */
    List<CollectItem> listCollect(Integer userId, Integer goodsId, Integer page, Integer limit);

    /**
     * 添加一条收藏记录
     * @param collectItem CollectItem类的实例
     * @return 插入之后的信息
     */
    CollectItemPo addCollectItem(CollectItemPo collectItem);

    /**
     * 根据收藏夹id删除收藏信息
     * @param id 收藏元素的id
     * @return 是否删除成功
     */
    int deleteCollectionById(Integer id);

}
