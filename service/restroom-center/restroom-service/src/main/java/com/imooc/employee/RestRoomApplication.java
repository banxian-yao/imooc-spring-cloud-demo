package com.imooc.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableFeignClients(basePackages = {"com.broadview"})
@EnableDiscoveryClient
//@EnableCircuitBreaker
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc"})
public class RestRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestRoomApplication.class, args);
    }
}
