package xmu.oomall.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

/**
 * @author hanzelegend
 */
@Service
@Repository
public interface GoodsService {

    /**
     * 根据id获取Goods信息
     * @param id goods的id
     * @param type 决定是否需要打包goods
     * @return Goods
     */
    Goods getGoodsById(Integer id,Integer type);

    /**
     * 用户查找商品列表
     * @param name 商品名称
     * @param page 分页数
     * @param limit 分页大小
     * @return Goods的列表
     */
    List<Goods> listGoodsForUser(String name, Integer page, Integer limit);

    /**
     * 管理员查找商品的信息
     * @param page 分页数
     * @param limit 分页大小
     * @param name 商品名
     * @param goodsSn 商品序列
     * @return GoodsPo的列表
     */
    List<GoodsPo> listGoodsPo(Integer page, Integer limit, String name, String goodsSn);

    /**
     * 增加一件商品
     * @param goodsPo 商品的po
     * @return 增加后的商品
     */
    GoodsPo insertGoods(GoodsPo goodsPo);

    /**
     * 管理员更新商品
     * @param goodsPo 商品的对象
     * @return 更新后的goodsPo
     */
    GoodsPo updateGoods(GoodsPo goodsPo);

    /**
     * 管理员删除商品，需要将该goods的所有product删除，以及删除评论（可以放在deleteProduct做）
     * @param id 商品id
     * @return 删除结果
     */
    int deleteGoods(Integer id);

    /**
     * 根据brand查询商品
     * @param brandId 品牌的id
     * @param limit 限制个数
     * @param page 页数
     * @return 查询到的商品列表
     */
    List<Goods>listGoodsByBrand(Integer brandId, Integer limit, Integer page);

    /**
     * 根据category来查找goods
     * @param categoryId category的id
     * @param limit 限制个数
     * @param page 页数
     * @return Goods的列表
     */
    List<Goods>listGoodsByCategory(Integer categoryId, Integer limit, Integer page);


    /**
     * 当删除某一个品牌或者商品种类的时候需要更改对应商品的属性
     * @param categoryId category的id
     * @param brandId 品牌的id
     */
    void changeBrandOrCategory(Integer brandId, Integer categoryId);

}
