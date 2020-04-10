package xmu.oomall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.GoodsCategoryPo;

import java.util.List;

/**
 * CategoryMapper
 *
 * @author YangHong
 * @date 2019-12-07
 */
@Mapper
@Repository
public interface GoodsCategoryMapper {
    /**
     * 插入类别
     * @param goodsCategory 商品的类别
     * @throws Exception 数据库错误int
     * @return 插入的行数
     */
     int insertGoodsCategory(@Param("goodsCategory") GoodsCategoryPo goodsCategory) throws Exception;

    /**
     * 根据id查找goods分类
     * @param id goods分类的id
     * @return 空或者查找个数
     * @throws Exception 数据库出错
     */
     GoodsCategoryPo getGoodsCategoryById(Integer id) throws Exception;

    /**
     * 根据goodsCategory类来更新goodsCategory
     * @param goodsCategory 商品分类
     * @throws Exception 数据库错误
     * @return 更新后的数据
     */
    int updateGoodsCategory(@Param("goodsCategory") GoodsCategoryPo goodsCategory) throws Exception;

    /**
     * 逻辑删除
     * @param id category的id
     * @return 逻辑删除的行数
     */
    int deleteCategory(@Param("id") Integer id);

    /**
     * 修改对应二级子类的pid
     * @param id 父类的id
     * @throws Exception 数据库错误
     * @return 修改的行数
     */
    int updateLevel2(@Param("id") Integer id) throws Exception;
    /**
     * 查看商品类目
     * @param start 查询的开始索引
     * @param limit 分页大小
     * @param pid 父类的id
     * @throws Exception 数据库错误
     * @return 分页的列表
     */
    List<GoodsCategoryPo> listGoodsCategories(@Param("start") Integer start, @Param("limit") Integer limit, @Param("pid") Integer pid) throws Exception;
}
