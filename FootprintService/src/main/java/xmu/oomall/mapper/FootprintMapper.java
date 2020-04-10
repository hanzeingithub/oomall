package xmu.oomall.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.FootprintItemPo;

import java.util.List;

@Mapper
@Repository
/**
 * FootprintDao
 *
 * @author YangHong
 * @date 2019-12-11
 */
public interface FootprintMapper {
    /**
     * 根据用户ID和商品ID找到该用户的足迹
     * @param userId 足迹所属于的用户ID
     * @param goodsId 商品的id
     * @param start 查询的开始索引
     * @param limit 分页大小
     * @throws Exception 数据库错误/
     * @return footprintItem列表
     */
    List<FootprintItemPo> listFootprint(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId,
                                        @Param("start") Integer start, @Param("limit") Integer limit)throws Exception;

    /**
     * 根据userId和goodsId查询数据库，内部使用，不用分页
     * @param userId 用户id
     * @param goodsId 商品id
     * @throws Exception 数据库错误
     * @return 单个footprintItem
     */
    FootprintItemPo getFootprintByExample(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId) throws Exception;

    /**
     * 添加一条足迹
     * @param footprintItemPo FootprintItem类的实例
     * @throws Exception 数据库错误
     * @return 增加的行数
     */
    int insertFootprintItem(@Param("footprintItem") FootprintItemPo footprintItemPo)throws Exception;

    /**
     * 根据足迹id删除足迹
     * @param id 足迹的id
     * @throws  Exception 数据库错误
     * @return 是否删除成功
     */
    int deleteFootprintById(@Param("id") Integer id) throws Exception;

    /**
     * 更新足迹的birthTime其他的都不更新
     * @param footprintItemPo 足迹的实体类,只会更新birthTime
     * @throws Exception 数据库错误
     * @return 返回更新的行数
     */
    int updateFootprintItem(@Param("footprintItem") FootprintItemPo footprintItemPo) throws Exception;
}
