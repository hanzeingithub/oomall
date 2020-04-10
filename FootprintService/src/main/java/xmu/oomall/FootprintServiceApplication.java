package xmu.oomall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author LXP
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
public class FootprintServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FootprintServiceApplication.class, args);
    }

}
