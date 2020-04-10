package xmu.oomall.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.GoodsCategoryPo;
import xmu.oomall.mapper.GoodsCategoryMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author y
 * @date 2019/12/14 12:08
 */
@Repository
public class GoodsCategoryDao {
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    public List<GoodsCategoryPo> listGoodsCategories(Integer page, Integer limit,Integer pid){
        Integer start = (page - 1) * limit;
        try {
            return goodsCategoryMapper.listGoodsCategories(start,limit, pid);
        } catch (Exception e) {
            return null;
        }
    }
    public GoodsCategoryPo getGoodsCategoryById(Integer id){
        try {
            GoodsCategoryPo goodsCategoryPo = goodsCategoryMapper.getGoodsCategoryById(id);
            if (goodsCategoryPo == null){
                GoodsCategoryPo goodsCategoryPo1 = new GoodsCategoryPo();
                goodsCategoryPo1.setId(0);
                return goodsCategoryPo1;
            }
            return goodsCategoryPo;
        } catch (Exception e) {
            return null;
        }
    }
    public GoodsCategoryPo insertGoodsCategory(GoodsCategoryPo goodsCategory){
        try {
            if (goodsCategory.getPid()!=null && goodsCategory.getPid()>0){
                // 找一下是否存在该pid
                GoodsCategoryPo goodsCategoryPo = goodsCategoryMapper.getGoodsCategoryById(goodsCategory.getPid());
                if (goodsCategoryPo.getId() <1){
                    goodsCategory.setId(0);
                }else{
                    int num = goodsCategoryMapper.insertGoodsCategory(goodsCategory);
                    if (num == 0){
                        goodsCategory.setId(0);
                    }
                }
            }else{
                int num = goodsCategoryMapper.insertGoodsCategory(goodsCategory);
                if (num == 0){
                    goodsCategory.setId(0);
                }
            }
            return goodsCategory;
        } catch (Exception e) {
            return null;
        }
    }

    public int deleteGoodsCategory(Integer id){
        try {
            return goodsCategoryMapper.deleteCategory(id);
        } catch (Exception e) {
            return -1;
        }
    }

    public GoodsCategoryPo updateGoodsCategory(GoodsCategoryPo goodsCategory){
        try {
            int num = goodsCategoryMapper.updateGoodsCategory(goodsCategory);
            if (num == 0){
                goodsCategory.setId(0);
                return goodsCategory;
            }else{
                return goodsCategoryMapper.getGoodsCategoryById(goodsCategory.getId());
            }
        } catch (Exception e) {
            return null;
        }
    }
}
