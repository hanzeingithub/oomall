package xmu.oomall.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xmu.oomall.util.InitializeRedis;

/**
 * @author hanzelegend
 * 启动程序时将数据初始化到redis的实现类
 */
@Component
public class Runner implements CommandLineRunner {
    @Autowired
    private InitializeRedis initializeRedis;

    @Override
    public void run(String... args) throws Exception {
    	initializeRedis.putGoodsIntoRedis();
    }
}