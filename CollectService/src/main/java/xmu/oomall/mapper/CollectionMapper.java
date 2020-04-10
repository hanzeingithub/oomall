package xmu.oomall.mapper;

import xmu.oomall.domain.collectitem.CollectItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.collectitem.CollectItemPo;

import java.util.List;

/**
 * @author hanzelegend
 */
@Mapper
@Repository
public interface CollectionMapper {

    /**
     * 根据用户ID和商品ID找到该用户的收藏夹
     * @param userId 收藏夹所属于的用户ID
     * @param goodsId 商品的id
     * @param start 页数
     * @param limit 每页的个数
     * @return collectItem列表
     */
    List<CollectItemPo> findCollect(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 添加一条收藏记录
     * @param collectItem CollectItem类的实例
     * @return 增加的行数
     */
    int addCollectItem(@Param("collectItem") CollectItemPo collectItem);

    /**
     * 根据收藏夹id删除收藏信息
     * @param id 收藏元素的id
     * @return 是否删除成功
     */
    int deleteCollectionById(@Param("id") Integer id);
}
