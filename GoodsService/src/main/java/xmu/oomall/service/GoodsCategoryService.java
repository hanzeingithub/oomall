package xmu.oomall.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import xmu.oomall.domain.goods.GoodsCategoryPo;

import java.util.List;

/**
 * @author y
 * @date 2019/12/14 13:06
 */
@Repository
@Service
public interface GoodsCategoryService {
    /**
     * 根据字段column选择goodsCatrgory
     * @param page 分页数
     * @param limit 分页大小
     * @param pid 父类id
     * @return goodsCategory列表
     */
    List<GoodsCategoryPo> listGoodsCategories(Integer page, Integer limit, Integer pid);

    /**
     * 批量插入类别
     * @param goodsCategory 商品的类别列表
     * @return 插入的行数
     */
    GoodsCategoryPo insertGoodsCategory(@Param("goodsCategories") GoodsCategoryPo goodsCategory);

    /**
     * 删除某个商品分类
     * @param id 分类id
     * @return 删除结果
     */
    int deleteGoodsCategory(@Param("id") Integer id);

    /**
     * 更新商品类别
     * @param goodsCategory 要修改的商品类别
     * @return 修改后的商品类别
     */
    GoodsCategoryPo updateGoodsCategory(@Param("goodsCategory") GoodsCategoryPo goodsCategory);

    /**
     * 移动二级分类
     * @param goodsCategory 二级分类实体
     * @return 返回更新后的goodsCategory
     */
    GoodsCategoryPo moveL2Category(@Param("goodsCategory") GoodsCategoryPo goodsCategory);
    /**
     * 根据id查看商品分类
     * @param id 商品分类id
     * @return 查到的商品分类信息
     */
    GoodsCategoryPo getGoodsCategoryById(Integer id);

    /**
     * 删除一级分类时更新二级分类
     * @param id 一级分类的id
     * @return 更新的行数
     */
    int updateLevel2(Integer id);


}
