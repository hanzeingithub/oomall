package xmu.oomall.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.BrandPo;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

/**
 * @author fairy
 */
@Repository
public interface BrandService {
    /**
     * 添加一个品牌
     * @param brand Brand类的实例
     * @return 插入的行数
     */
    BrandPo insertBrand(@Param("brand") BrandPo brand);

    /**
     * 删除一个品牌
     * @param id BrandId
     * @return 是否删除成功
     */
    int deleteBrand(@Param("id") Integer id);

    /**
     * 更新品牌
     * @param brand 品牌实体类
     * @param id 品牌的id
     * @return 成功更新的数目
     */
    BrandPo updateBrand(@Param("id") Integer id, @Param("brand") BrandPo brand);

    /**
     * 根据品牌Id和品牌Name查看品牌
     * @param id 品牌Id
     * @param name 品牌Name
     * @param start 查询的开始索引
     * @param limit 分页大小
     * @return 品牌实体类
     */
    List<BrandPo> listBrands(@Param("id") Integer id, @Param("name") String name,
                             @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * 根据品牌Id查看品牌
     * @param id 品牌Id
     * @return 品牌实体类
     */
    BrandPo getBrandById(@Param("id") Integer id);

    /**
     * 根据brandId查看相应商品
     * @param id 品牌Id
     * @return 商品列表
     */
    List<GoodsPo> listGoodsByBrandId(@Param("id") Integer id);
}
