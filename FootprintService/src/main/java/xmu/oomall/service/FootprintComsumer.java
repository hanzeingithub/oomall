package xmu.oomall.service;
import com.alibaba.fastjson.JSONArray;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xmu.oomall.dao.FootprintDao;
import xmu.oomall.domain.FootprintItemPo;
import xmu.oomall.util.RunTimeUtil;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * FootprintComsumer
 *
 * @author YangHong
 * @date 2019-12-13
 */
@Component
public class FootprintComsumer implements CommandLineRunner {
    /**
     * 生产者的组名
     */
    @Value("${rocketmq.consumer.groupName}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private FootprintDao footprintDao;

    private static final Logger logger = LoggerFactory.getLogger(FootprintComsumer.class);

    @PostConstruct
    public void footprintConsumer(){
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(producerGroup);
            consumer.setNamesrvAddr(namesrvAddr);
            // 订阅FootprintTopic的所有tag，从头开始读取
            consumer.subscribe("FootprintTopic", "*");
            consumer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt:msgs){
                        String messageBody = new String(messageExt.getBody());
                        logger.info(messageBody);
                        FootprintItemPo footPrintItemPo = JSONArray.parseObject(messageBody, FootprintItemPo.class);
                        // 去数据库找一下该记录是否存在
                        FootprintItemPo footPrintItemResult = footprintDao.queryFootprintItem(footPrintItemPo.getUserId(),
                                footPrintItemPo.getGoodsId());
                        // todo 查一下商品是否存在，存在才往里加
                        if (footPrintItemResult != null){
                            // 查不到该条记录
                            if (footPrintItemResult.getId() == 0){
                                logger.info("查不到记录");
                                footPrintItemPo.setBirthTime(LocalDateTime.now());
                                footPrintItemPo.setGmtCreate(LocalDateTime.now());
                                footprintDao.insertFootprintItem(footPrintItemPo);
                            }else{
                                logger.info("查到记录了更新时间");
                                footPrintItemPo.setBirthTime(LocalDateTime.now());
                                footPrintItemPo.setId(footPrintItemResult.getId());
                                footprintDao.updateFootprintItem(footPrintItemPo);
                            }
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            logger.info("failure");
        }
    }
    @Override
    public void run(String... strings) throws Exception {
        footprintConsumer();
    }
}
