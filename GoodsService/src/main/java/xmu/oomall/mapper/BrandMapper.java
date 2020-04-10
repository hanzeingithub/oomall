package xmu.oomall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.BrandPo;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

/**
 * @author fairy
 */
@Mapper
@Repository
public interface BrandMapper {

    /**
     * 添加一个品牌
     * @param brand Brand类的实例
     * @return 插入的行数
     * @throws Exception 数据库异常
     */
    int insertBrand(@Param("brand") BrandPo brand) throws Exception;

    /**
     * 删除一个品牌
     * @param id BrandId
     * @return 是否删除成功
     * @throws Exception 数据库异常
     */
    int deleteBrand(@Param("id") Integer id) throws Exception;

    /**
     * 更新品牌
     * @param brand 品牌实体类
     * @return 成功更新的数目
     * @throws Exception 数据库异常
     */
    int updateBrand(@Param("brand") BrandPo brand) throws Exception;

    /**
     * 根据品牌Id和品牌Name查看品牌
     * @param id 品牌Id
     * @param name 品牌Name
     * @param start 查询的开始索引
     * @param limit 分页大小
     * @return 品牌Po实体类
     * @throws Exception 数据库异常
     */
    List<BrandPo> listBrands(@Param("id") Integer id, @Param("name") String name,
                             @Param("start") Integer start, @Param("limit") Integer limit) throws Exception;

    /**
     * 根据品牌Id查看品牌详情
     * @param id 品牌Id
     * @return 品牌Po实体类
     * @throws Exception 数据库异常
     */
    BrandPo getBrandById(@Param("id") Integer id) throws Exception;

    /**
     * 通过brandId查找商品
     * @param brandId 品牌Id
     * @return 商品的列表
     * @throws Exception 数据库异常
     */
    List<GoodsPo> listGoodsByBrandId(@Param("brandId") Integer brandId) throws Exception;
}
