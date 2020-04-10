package xmu.oomall.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.FootprintItem;
import xmu.oomall.domain.FootprintItemPo;
import xmu.oomall.mapper.FootprintMapper;
import java.util.List;

/**
 * FootprintDao
 *
 * @author YangHong
 * @date 2019-12-11
 */
@Repository
public class FootprintDao {
    @Autowired
    private FootprintMapper footprintMapper;
    public List<FootprintItemPo> getFootprintItems(Integer userId, Integer goodsId, Integer page, Integer limit){
        Integer start = (page - 1) * limit;
        try {
            return footprintMapper.listFootprint(userId, goodsId, start, limit);
        } catch (Exception e) {
           return null;
        }
    }
    public FootprintItemPo queryFootprintItem(Integer userId, Integer goodsId){
        try {
            FootprintItemPo footprintItemPo = footprintMapper.getFootprintByExample(userId,goodsId);
            if (footprintItemPo == null){
                FootprintItemPo footprintItemPo1 = new FootprintItem();
                footprintItemPo1.setId(0);
                return footprintItemPo1;
            }
            return footprintItemPo;
        } catch (Exception e) {
            return null;
        }
    }
    public int insertFootprintItem(FootprintItemPo footPrintItem){
        try {
            return footprintMapper.insertFootprintItem(footPrintItem);
        } catch (Exception e) {
            return -1;
        }
    }
    public int deleteFootprintItem(Integer id){
        try {
            return footprintMapper.deleteFootprintById(id);
        } catch (Exception e) {
            return -1;
        }
    }
    public int updateFootprintItem(FootprintItemPo footPrintItem){
        try {
            return footprintMapper.updateFootprintItem(footPrintItem);
        } catch (Exception e) {
            return -1;
        }
    }
}
