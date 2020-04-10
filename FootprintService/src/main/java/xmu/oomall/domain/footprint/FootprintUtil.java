package xmu.oomall.domain.footprint;
import xmu.oomall.domain.FootprintItem;
import xmu.oomall.domain.FootprintItemPo;
import xmu.oomall.domain.Goods;
import xmu.oomall.domain.GoodsPo;
import xmu.oomall.util.JacksonUtil;
import java.util.LinkedList;
import java.util.List;

/**
 * FootprintUtil
 *
 * @author YangHong
 * @date 2019-12-17
 */
public class FootprintUtil {
    public static List<FootprintItem> linkFootprint(List<GoodsPo> goodsPos, List<FootprintItemPo> footprintItemPos){
        List<FootprintItem> footprintItems = new LinkedList<>();
        for(int i=0; i<footprintItemPos.size(); i++){
            FootprintItem footprintItem = new FootprintItem();
            footprintItem.setGoodsPo(goodsPos.get(i));
            footprintItem.setUserId(footprintItemPos.get(i).getId());
            footprintItem.setGoodsId(footprintItemPos.get(i).getGoodsId());
            footprintItem.setId(footprintItemPos.get(i).getId());
            footprintItems.add(footprintItem);
        }
        return footprintItems;
    }

    public static GoodsPo goodsTogoodsPo(Goods goods){
        if (goods == null){
            return null;
        }
        GoodsPo goodsPo = new GoodsPo();
        goodsPo.setName(goods.getName());
        goodsPo.setBrandId(goods.getBrandId());
        return goodsPo;
    }

    public static<T> T getBack(Object result,Class<T>clazz){
        String theResult= JacksonUtil.toJson(result);
        String errno=JacksonUtil.parseString(theResult,"errno");
        String success="0";
        if(!success.equals(errno)){
            return null;
        }
        return JacksonUtil.parseObject(theResult,"data",clazz);
    }
}
