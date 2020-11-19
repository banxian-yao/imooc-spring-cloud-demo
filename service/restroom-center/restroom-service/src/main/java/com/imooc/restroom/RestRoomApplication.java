package com.imooc.restroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc"})
public class RestRoomApplication {

    /**
     * protobuf 序列化
     */
    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    /**
     * protobuf 反序列化
     */
    @Bean
    RestTemplate restTemplate(ProtobufHttpMessageConverter protobufHttpMessageConverter) {
        return new RestTemplate(Collections.singletonList(protobufHttpMessageConverter));
    }

    public static void main(String[] args) {
        SpringApplication.run(RestRoomApplication.class, args);
    }
}
