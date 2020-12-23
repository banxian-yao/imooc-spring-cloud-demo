package com.imooc.platform.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RedisLimiterConfiguration {

    // 使用remoteaddress作为key
    @Bean
    @Primary
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
        );
    }

    @Bean("restroomRateLimiter")
    @Primary
    public RedisRateLimiter redisLimiterUser() {
        return new RedisRateLimiter(10, 20);
    }

}
