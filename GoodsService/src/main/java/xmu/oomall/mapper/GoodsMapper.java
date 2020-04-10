package xmu.oomall.mapper;
import xmu.oomall.domain.goods.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

/**
 * GoodsMapper
 *
 * @author YangHong
 * @date 2019-12-06
 */
@Mapper
@Repository
public interface GoodsMapper {
    /**
     * 根据商品id返回商品
     * @param id 商品的id
     * @return 商品信息
     */
    GoodsPo getGoodsById(@Param("id") Integer id);

    /**
     * 启动项目的时候初始化Redis，将热销、新品放到redis里面
     * @return 商品信息
     */
    List<GoodsPo> initializeRedis();

    /**
     * 通过brand或category的id或者商品关键词查找商品，给user用的
     * @param name 商品的名称开头
     * @param start 页数
     * @param limit 每页的个数
     * @return 商品的列表
     */
    List<GoodsPo> listGoodsByName(@Param("name") String name,@Param("start") Integer start,@Param("limit") Integer limit);

    /**
     * 通过brand或category的id或者商品关键词查找商品，不可同时查询
     * @param brandId 商品所属的brand
     * @param categoryId 商品所属的种类
     * @param start 页数
     * @param limit 每页的个数
     * @return 商品的列表
     */
    List<GoodsPo> listGoodsByBrandAndCategory(@Param("brandId") Integer brandId,@Param("categoryId") Integer categoryId,@Param("start") Integer start,@Param("limit") Integer limit);

    /**
     * 通过brand或category的id或者商品关键词查找商品，不可同时查询
     * @param name 商品的名称开头
     * @param goodsSn goods的流水
     * @param start 页数
     * @param limit 每页的个数
     * @return 商品的列表
     */
    List<GoodsPo> listGoodsByQuery(@Param("goodsSn") String goodsSn,@Param("name") String name,@Param("start") Integer start,@Param("limit") Integer limit);

    /**
     * 根据id更新商品
     * @param goods 商品实体类
     * @return 成功更新的数目
     */
    int updateGoods(@Param("goods") GoodsPo goods);

    /**
     * 根据id删除商品
     * @param id 商品的id
     * @return 删除的行数
     */
    int deleteGoodsById(@Param("id") Integer id);

    /**
     * 新增一条商品记录
     * @param goods 商品实体类
     * @return 新增的商品数
     */
    int insertGoods(@Param("goods") GoodsPo goods);

    /**
     * 当删除brand之后需要修改brand旗下所有商品的brandId
     * @param brandId 品牌的ID
     * @return 修改的商品数
     */
    int changeBrand(@Param("brandId")Integer brandId);

    /**
     * 当删除category后需要修改category旗下所有商品的categoryId
     * @param categoryId 商品种类的ID
     * @return 修改的商品数
     */
    int changeCategory(@Param("categoryId")Integer categoryId);
}
