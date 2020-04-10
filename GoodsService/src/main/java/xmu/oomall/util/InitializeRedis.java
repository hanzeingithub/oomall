package xmu.oomall.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xmu.oomall.dao.GoodsDao;
import xmu.oomall.dao.ProductsDao;
import xmu.oomall.domain.goods.GoodsPo;
import xmu.oomall.mapper.GoodsMapper;

import java.util.List;

/**
 * @author hanzelegend
 * 用于启动程序的时候将所需要初始化的数据放入redis中
 */
@Component
public class InitializeRedis {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ProductsDao productsDao;

    void putGoodsIntoRedis(){
        List<Integer>goodsIds=goodsDao.initializingRedis();
        if(!goodsIds.isEmpty()){
            for(Integer goodsId:goodsIds){
                productsDao.listProductsByGoodsId(goodsId);
            }
        }
    }
}
