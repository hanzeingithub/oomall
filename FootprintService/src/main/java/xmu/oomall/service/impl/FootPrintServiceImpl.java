package xmu.oomall.service.impl;
import com.alibaba.fastjson.JSONArray;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xmu.oomall.dao.FootprintDao;
import xmu.oomall.domain.*;
import xmu.oomall.domain.footprint.FootprintUtil;
import xmu.oomall.feign.GoodsFeign;
import xmu.oomall.service.FootPrintService;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.util.RunTimeUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * FootPrintServiceImpl
 *
 * @author YangHong
 * @date 2019-12-11
 */
@Service
@Component
public class FootPrintServiceImpl implements FootPrintService {
    /**
     * 生产者的组名
     */
    @Value("${rocketmq.producer.groupName}")
    private String producerGroup;
    /**
     * NameServer 地址
     */
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private FootprintDao footprintDao;

    @Autowired
    private GoodsFeign goodsFeign;

    private static final Logger logger = LoggerFactory.getLogger(FootPrintServiceImpl.class);

    @Override
    public List<FootprintItem> listFootprintItem(Integer userId, Integer goodsId, Integer page, Integer limit) {
        Integer success = 0;
        List<FootprintItem> footprintItems = new LinkedList<>();
        List<GoodsPo> goodsPos = new LinkedList<>();
        List<FootprintItemPo> footprintItemPos = footprintDao.getFootprintItems(userId, goodsId, page, limit);
        if (footprintItemPos == null){
            return null;
        }
        if (footprintItemPos.size() != 0){
            // 若是查到了用户足迹，封装到footprint中
            if (goodsId != null){
                Object result = goodsFeign.getGoodsById(goodsId);
                Integer errno = JacksonUtil.parseInteger(JacksonUtil.toJson(result),"errno");
                logger.info(errno.toString());
                if (success.equals(errno)){
                    Goods goods = FootprintUtil.getBack(result, Goods.class);
                    logger.info(goods.toString());
                    GoodsPo goodsPo = FootprintUtil.goodsTogoodsPo(goods);
                    for (int i = 0; i<footprintItemPos.size(); i++){
                        goodsPos.add(goodsPo);
                    }
                }
            }else{
                for (int i = 0; i<footprintItemPos.size(); i++){
                    Object result = goodsFeign.getGoodsById(footprintItemPos.get(i).getGoodsId());
                    Integer errno = JacksonUtil.parseInteger(JacksonUtil.toJson(result),"errno");
                    logger.info(errno.toString());
                    if (success.equals(errno)){
                        Goods goods = JacksonUtil.parseObject(JacksonUtil.toJson(result),"data",Goods.class);
                        GoodsPo goodsPo = FootprintUtil.goodsTogoodsPo(goods);
                        logger.info(goods.toString());
                        goodsPos.add(goodsPo);
                    }else{
                        goodsPos.add(new GoodsPo());
                    }
                }
            }
            footprintItems = FootprintUtil.linkFootprint(goodsPos, footprintItemPos);
        }
        return footprintItems;
    }

    @Override
    public FootprintItemPo insertFootPrint(FootprintItemPo footprintItemPo) {
            DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
            // 找一下该记录是否存在
            producer.setNamesrvAddr(namesrvAddr);
            producer.setVipChannelEnabled(false);
            producer.setRetryTimesWhenSendAsyncFailed(10);
            producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
            try {
                producer.start();
                Object obj = JSONArray.toJSON(footprintItemPo);
                String json = obj.toString();
                logger.info("发送消息 Tag1");
                Message message = new Message("FootprintTopic", "Tag1", json.getBytes());
                try {
                    producer.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return footprintItemPo;
            } catch (MQClientException e) {
                e.printStackTrace();
                return null;
            }
    }

    @Override
    public int deleteFootPrint(Integer id) {
        return footprintDao.deleteFootprintItem(id);
    }
}
