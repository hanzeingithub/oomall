package xmu.oomall.domain.goods;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:产品对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Product extends ProductPo {
    private GoodsPo goodsPo;

    public Product(){
        super();
    }
    public Product(ProductPo productPo){
        this.setBeDeleted(productPo.getBeDeleted());
        this.setGmtCreate(productPo.getGmtCreate());
        this.setGmtModified(productPo.getGmtModified());
        this.setGoodsId(productPo.getGoodsId());
        this.setId(productPo.getId());
        this.setPicUrl(productPo.getPicUrl());
        this.setPrice(productPo.getPrice());
        this.setSafetyStock(productPo.getSafetyStock());
        this.setSpecifications(productPo.getSpecifications());
    }

}
