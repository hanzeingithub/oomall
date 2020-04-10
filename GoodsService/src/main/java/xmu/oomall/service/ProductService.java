package xmu.oomall.service;

import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.Product;
import xmu.oomall.domain.goods.ProductPo;
import xmu.oomall.domain.other.OrderItem;

import java.util.List;

/**
 * @author hanzelegend
 */
@Repository

public interface ProductService {
    /**
     *                                         内部接口
     */
    /**
     * 更新库存
     * @param operation 判断是增加库存还是减少库存的操作
     * @param orderItemList orderItem列表
     * @return 更新的结果，为0则更新失败
     */
    boolean updateDecute(List<OrderItem> orderItemList, boolean operation);


    /**
     * 查询Product列表
     * @param goodsId goodsId
     * @return productPo的列表
     */
    List<ProductPo> listProducts(Integer goodsId);

    /**
     * 管理员删除product，需要将该product的评论删除
     * @param id product的id
     * @return 影响的行数
     */
    int deleteProduct(Integer id);


    /**
     * 获取某个product的信息
     * @param id product的id
     * @return Product的信息
     */
    Product getProduct(Integer id);


    /**
     * 修改Product
     * @param productPo 将要修改的product
     * @return 修改后的productPo
     */
    ProductPo updateProduct(ProductPo productPo);


    /**
     * 增加Product
     * @param productPo 将要增加的product
     * @return 是否添加成功
     */
    ProductPo insertProduct(ProductPo productPo);


}
