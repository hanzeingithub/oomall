package xmu.oomall.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import xmu.oomall.domain.goods.BrandPo;
import xmu.oomall.domain.goods.GoodsPo;
import xmu.oomall.mapper.BrandMapper;

import java.util.List;

/**
 * @author fairy
 */
@Repository
public class BrandDao {
    @Autowired
    private BrandMapper brandMapper;

    public List<BrandPo> listBrands(Integer brandId, String brandName, Integer page, Integer limit){
        try {
            Integer start = (page - 1) * limit;
            return brandMapper.listBrands(brandId, brandName, start, limit);
        }catch (Exception e){
            return null;
        }
    }

    public BrandPo insertBrand(BrandPo brand){
        try{
            int i=brandMapper.insertBrand(brand);
            if(i==0){
                brand.setId(0);
            }
            return brand;
        }catch (Exception e){
            return null;
        }
    }

    public int deleteBrand(Integer id){
        try {
            return brandMapper.deleteBrand(id);
        } catch (Exception e) {
            return -1;
        }
    }

    public BrandPo updateBrand(BrandPo brandPo){
        try {
            int num = brandMapper.updateBrand(brandPo);
            if(num==0){
                brandPo.setId(0);
            }else {
                brandPo=brandMapper.getBrandById(brandPo.getId());
            }
            return brandPo;
        } catch (Exception e) {
            return null;
        }
    }

    public BrandPo getBrandById(Integer id){
        try {
            BrandPo brandPo=brandMapper.getBrandById(id);
            if(brandPo==null){
                brandPo=new BrandPo();
                brandPo.setId(0);
            }
            return brandPo;
        } catch (Exception e) {
            return null;
        }
    }

    public List<GoodsPo> listGoodsByBrandId(Integer id){
        try {
            return brandMapper.listGoodsByBrandId(id);
        } catch (Exception e) {
            return null;
        }
    }
}
