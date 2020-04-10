package xmu.oomall.service.impl;

import xmu.oomall.dao.CollectDao;
import xmu.oomall.domain.collectitem.CollectItem;
import xmu.oomall.domain.collectitem.CollectItemPo;
import xmu.oomall.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hanzelegend
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectDao collectDao;


    @Override
    public List<CollectItem> listCollect(Integer userId, Integer goodsId, Integer page, Integer limit){
        return collectDao.listCollectItem(userId, goodsId, page, limit);

    }

    @Override
    public CollectItemPo addCollectItem(CollectItemPo collectItem){
        /*该用户是否已经收藏过该商品*/
        List<CollectItem>collectItemPos=collectDao.listCollectItem(collectItem.getUserId(),collectItem.getGoodsId(),1,1);
        if(collectItemPos.size()!=0){
            collectItem.setId(0);
            return collectItem;
        }
            return collectDao.insertCollectItem(collectItem);
    }

    @Override
    public int deleteCollectionById(Integer id){
        return collectDao.deleteCollectItem(id);
    }


}
