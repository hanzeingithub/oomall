package xmu.oomall.dao;

import xmu.oomall.domain.collectitem.CollectItem;
import xmu.oomall.domain.collectitem.CollectItemPo;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.feign.GoodsFeign;
import xmu.oomall.mapper.CollectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xmu.oomall.util.JacksonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * CollectDao
 *
 * @author YangHong
 * @date 2019-12-04
 */
@Repository
public class CollectDao {
    @Autowired
    private CollectionMapper collectionMapper;
    @Resource
    private GoodsFeign goodsFeign;
    private Boolean isOk(CollectItemPo collectItem){
        Object result=goodsFeign.getGoodsById(collectItem.getGoodsId());
        String results= JacksonUtil.toJson(result);
        Integer errno= JacksonUtil.parseInteger(results,"errno");
        Integer success=0;
        return success.equals(errno);
    }
    private CollectItem packagingCollectItem(CollectItemPo collectItemPo){
        Object result=goodsFeign.getGoodsById(collectItemPo.getGoodsId());
        String results= JacksonUtil.toJson(result);
        Goods goods=JacksonUtil.parseObject(results,"data",Goods.class);
        CollectItem collectItem=new CollectItem(collectItemPo);
        collectItem.setGoodsPo(goods);
        return collectItem;
    }
    public CollectItemPo insertCollectItem(CollectItemPo collectItem){
        try{
            if(isOk(collectItem)) {
                int i = collectionMapper.addCollectItem(collectItem);
                if (i == 0) {
                    collectItem.setId(0);
                }
                return collectItem;
            }
            collectItem.setId(0);
            return collectItem;
        }catch (Exception e){
            return null;
        }
    }

    public List<CollectItem> listCollectItem(Integer userId, Integer goodsId , Integer page, Integer limit){
        Integer start = (page - 1) * limit;
        try {
            List<CollectItemPo>collectItemPos=collectionMapper.findCollect(userId,goodsId,start,limit);
            List<CollectItem>collectItems=new ArrayList<>();
            if(collectItemPos.isEmpty()){
                return collectItems;
            }
            for(CollectItemPo collectItemPo:collectItemPos){
                collectItems.add(packagingCollectItem(collectItemPo));
            }
            return collectItems;
        }catch (Exception e){
            return null;
        }
    }

    public int deleteCollectItem(Integer id){
       try{
           return collectionMapper.deleteCollectionById(id);
       }catch (Exception e)
       {
           return -1;
       }
    }
}
