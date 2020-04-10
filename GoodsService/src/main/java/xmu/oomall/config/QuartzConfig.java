package xmu.oomall.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xmu.oomall.util.ProductTask;

/**
 * @author hanzelegend
 * 用来实现定期将redis里面的数据更新到数据库中的配置
 */
@Configuration
public class QuartzConfig {

    private static final String PRODUCT_TASK_IDENTITY = "ProductTaskQuartz";

    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(ProductTask.class).withIdentity(PRODUCT_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(10)  //设置时间周期单位秒
//                .withIntervalInHours(2)  //两个小时执行一次
                  .withIntervalInMinutes(30)
//                .withIntervalInSeconds(60)
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(PRODUCT_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}