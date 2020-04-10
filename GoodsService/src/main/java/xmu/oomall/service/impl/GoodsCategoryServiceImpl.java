package xmu.oomall.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.oomall.dao.GoodsCategoryDao;
import xmu.oomall.domain.goods.Goods;
import xmu.oomall.domain.goods.GoodsCategory;
import xmu.oomall.domain.goods.GoodsCategoryPo;
import xmu.oomall.mapper.GoodsCategoryMapper;
import xmu.oomall.service.GoodsCategoryService;

import java.util.List;

/**
 * @author yh
 * @date 2019/12/14 13:06
 */
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryDao goodsCategoryDao;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public GoodsCategoryPo insertGoodsCategory(GoodsCategoryPo goodsCategory) {
        GoodsCategoryPo goodsCategoryPo = goodsCategoryDao.insertGoodsCategory(goodsCategory);
        if(goodsCategoryPo==null){
            return null;
        }else {
           return goodsCategoryPo;
        }
    }

    @Override
    public int deleteGoodsCategory(Integer id) {
        return goodsCategoryDao.deleteGoodsCategory(id);
    }

    @Override
    public GoodsCategoryPo moveL2Category(GoodsCategoryPo goodsCategoryPo){
        GoodsCategoryPo goodsCategoryPoResult = goodsCategoryDao.getGoodsCategoryById(goodsCategoryPo.getId());
        // 父分类无法修改
        if (goodsCategoryPoResult == null || goodsCategoryPoResult.getPid()==null){
            goodsCategoryPo.setId(0);
            return goodsCategoryPo;
        }else{
            GoodsCategoryPo goodsCategoryExist = goodsCategoryDao.getGoodsCategoryById(goodsCategoryPo.getPid());
            if (goodsCategoryExist.getId() > 0){
                goodsCategoryPo =  goodsCategoryDao.updateGoodsCategory(goodsCategoryPo);
                if (goodsCategoryPo!= null && goodsCategoryPo.getId() >=0 ){
                    return goodsCategoryDao.getGoodsCategoryById(goodsCategoryPo.getId());
                }
            }else{
                goodsCategoryPo.setId(0);
            }
            return goodsCategoryPo;
        }
    }
    @Override
    public GoodsCategoryPo updateGoodsCategory(GoodsCategoryPo goodsCategory) {
       return goodsCategoryDao.updateGoodsCategory(goodsCategory);
    }

    @Override
    public List<GoodsCategoryPo> listGoodsCategories(Integer page, Integer limit, Integer pid) {
        return goodsCategoryDao.listGoodsCategories(page, limit, pid);
    }

    @Override
    public GoodsCategoryPo getGoodsCategoryById(Integer id){
        return goodsCategoryDao.getGoodsCategoryById(id);
    }

    @Override
    public int updateLevel2(Integer id) {
        try {
            return goodsCategoryMapper.updateLevel2(id);
        } catch (Exception e) {
            return -1;
        }
    }
}
