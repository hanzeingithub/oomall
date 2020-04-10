package xmu.oomall.mapper;

import org.springframework.web.bind.annotation.PathVariable;
import xmu.oomall.domain.goods.ProductPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductMapper
 *
 * @author YangHong
 * @date 2019-12-07
 */
@Mapper
@Repository
public interface ProductMapper {
    /**
     * 根据商品id获取商品的规格
     * @param goodsId 产品所属商品的id
     * @return 商品规格列表
     */
    List<ProductPo> listProductsByGoodsId(@Param("goodsId") Integer goodsId);

    /**
     * 根据productId获取商品的规格
     * @param id 产品的id
     * @return 商品规格列表
     */
    ProductPo getProductPo(@Param("id") Integer id);

    /**
     * 插入一个Product列表
     * @param product products列表
     * @return 行数
     */
    int insertProduct(@Param("product") ProductPo product);

    /**
     * 根据id批量删除product
     * @param id 要删除的id
     * @param goodsId 根据goodsId来批量删除product
     * @return 行数
     */
    int deleteProductById(@Param("id") Integer id, @Param("goodsId")Integer goodsId);

    /**
     * 根据id更新product
     * @param product product实体类
     * @return 更新的行数
     */
    int updateProduct(@Param("product") ProductPo product);

}
