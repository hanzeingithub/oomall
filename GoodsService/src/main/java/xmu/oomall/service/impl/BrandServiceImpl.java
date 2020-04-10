package xmu.oomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.oomall.dao.BrandDao;
import xmu.oomall.domain.goods.BrandPo;
import xmu.oomall.domain.goods.GoodsPo;
import xmu.oomall.service.BrandService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fairy
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Override
    public BrandPo insertBrand(BrandPo brand) {
        if(brand==null){
            return null;
        }else{
            List<BrandPo> brands=brandDao.listBrands(null,brand.getName(),1,10);
            //如果数据库中已有该品牌则不插入
            if(brands.size()>0){
                return null;
            }else{
                brand.setGmtCreate(LocalDateTime.now());
                brand.setGmtModified(LocalDateTime.now());
                brand.setBeDeleted(false);
                brandDao.insertBrand(brand);
                return brand;
            }
        }
    }

    @Override
    public int deleteBrand(Integer id) {
        return brandDao.deleteBrand(id);
    }

    @Override
    public BrandPo updateBrand(Integer id, BrandPo brand){
        brand.setId(id);
        brand.setGmtModified(LocalDateTime.now());
        return brandDao.updateBrand(brand);
    }

    @Override
    public List<BrandPo> listBrands(Integer brandId, String brandName, Integer page, Integer limit){
        return brandDao.listBrands(brandId,brandName,page,limit);
    }

    @Override
    public BrandPo getBrandById(Integer id){
        return brandDao.getBrandById(id);
    }

    @Override
    public List<GoodsPo> listGoodsByBrandId(Integer id) {
        return brandDao.listGoodsByBrandId(id);
    }
}
