package xmu.oomall.domain.goods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xmu.oomall.domain.goods.GoodsCategoryPo;
import xmu.oomall.domain.goods.GoodsPo;

import java.util.List;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:二级目录对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class GoodsCategory extends GoodsCategoryPo {
    private List<GoodsPo> goodsPoList;
}
