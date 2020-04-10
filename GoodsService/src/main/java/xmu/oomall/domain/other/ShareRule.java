package xmu.oomall.domain.other;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 数据库与对象模型标准组
 * @Description:分享规则对象
 * @Data:Created in 14:50 2019/12/11
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ShareRule extends ShareRulePo {
    @Getter
    @Setter
    public static class Strategy{
        private Integer lowerBound;
        private Integer upperBound;
        private BigDecimal discountRate;
    }
    private static class MyStrategy{
        private Integer lowerbound;
        private Integer upperbound;
        private BigDecimal rate;
    }
    private List<Strategy> strategyList;
    private Integer shareType;}