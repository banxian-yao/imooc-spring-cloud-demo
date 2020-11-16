package com.imooc.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc"})
@EnableFeignClients(basePackages = {"com.imooc"})
public class EmployeeApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    feign.Logger.Level feignLoggerInfo() {
        return feign.Logger.Level.FULL;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }
}
