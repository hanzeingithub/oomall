package xmu.oomall.domain.goods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import xmu.oomall.domain.other.GrouponRule;
import xmu.oomall.domain.other.PresaleRule;
import xmu.oomall.domain.other.ShareRule;

import java.util.List;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:商品对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Goods extends GoodsPo {
    private BrandPo brandPo;
    private GoodsCategoryPo goodsCategoryPo;
    private List<ProductPo> productPoList;
    private GrouponRule grouponRule;
    private ShareRule shareRule;
    private PresaleRule presaleRule;

    public Goods(){
        super();
    }
    public Goods(GoodsPo goodsPo){
        this.setBeDeleted(goodsPo.getBeDeleted());
        this.setBeSpecial(goodsPo.getBeSpecial());
        this.setBrandId(goodsPo.getBrandId());
        this.setBrief(goodsPo.getBrief());
        this.setDescription(goodsPo.getDescription());
        this.setDetail(goodsPo.getDetail());
        this.setGallery(goodsPo.getGallery());
        this.setGmtCreate(goodsPo.getGmtCreate());
        this.setGmtModified(goodsPo.getGmtModified());
        this.setGoodsCategoryId(goodsPo.getGoodsCategoryId());
        this.setGoodsSn(goodsPo.getGoodsSn());
        this.setId(goodsPo.getId());
        this.setName(goodsPo.getName());
        this.setPicUrl(goodsPo.getPicUrl());
        this.setPrice(goodsPo.getPrice());
        this.setShareUrl(goodsPo.getShareUrl());
        this.setShortName(goodsPo.getShortName());
        this.setSpecialFreightId(goodsPo.getSpecialFreightId());
        this.setStatusCode(goodsPo.getStatusCode());
        this.setVolume(goodsPo.getVolume());
        this.setWeight(goodsPo.getWeight());
    }
}
