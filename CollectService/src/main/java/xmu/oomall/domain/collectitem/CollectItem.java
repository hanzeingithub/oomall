package xmu.oomall.domain.collectitem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xmu.oomall.domain.goods.GoodsPo;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:收藏夹明细对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CollectItem extends CollectItemPo {

    private GoodsPo goodsPo;

    public CollectItem(){
        super();
    }
    public CollectItem(CollectItemPo collectItemPo){
        this.setGmtCreate(collectItemPo.getGmtCreate());
        this.setGmtModified(collectItemPo.getGmtModified());
        this.setGoodsId(collectItemPo.getGoodsId());
        this.setId(collectItemPo.getId());
        this.setUserId(collectItemPo.getUserId());
    }
}