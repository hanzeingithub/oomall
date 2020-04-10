package xmu.oomall.domain.footprintitem;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xmu.oomall.domain.footprintitem.FootprintItemPo;
import xmu.oomall.domain.goods.GoodsPo;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:足迹明细对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class FootprintItem extends FootprintItemPo {

    private GoodsPo goodsPo;
}
