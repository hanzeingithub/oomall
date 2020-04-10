package xmu.oomall.controller.collect.vo;
import xmu.oomall.domain.collectitem.CollectItem;
import xmu.oomall.domain.goods.Goods;

/**
 *  CollectItemVo
 * @author hanzelegend
 */
public class CollectItemVo {

    /**
     * 收藏
     */
    private CollectItem collectItem;

    /**
     * 商品
     */
    private Goods goods;

    public CollectItem getCollectItem() {
        return collectItem;
    }

    public void setCollectItem(CollectItem collectItem) {
        this.collectItem = collectItem;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}