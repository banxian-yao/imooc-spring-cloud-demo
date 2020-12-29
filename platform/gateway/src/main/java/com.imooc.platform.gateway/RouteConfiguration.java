package com.imooc.platform.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RouteConfiguration {

    @Autowired
    private KeyResolver hostNameResolver;

    @Autowired
    @Qualifier("restroomRateLimiter")
    private RateLimiter rateLimiterUser;

    private PathRoutePredicateFactory sd;

    @Bean
    @Order(-1)
    public GlobalFilter a() {
        return (exchange, chain) -> {
            log.info("PRE filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                log.info("POST filter");
            }));
        };
    }

    @RestController
    public class ErrorHandle {

        @RequestMapping("/global-error")
        public String DefaultErrorHandle(){
            return "这是通用错误处理返回的信息。";
        }
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        .path("/toilet-service/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                                c.setKeyResolver(hostNameResolver);
                                c.setRateLimiter(rateLimiterUser);
                                c.setStatusCode(HttpStatus.BAD_GATEWAY);
                            }).hystrix(c -> {
                                c.setName("fallback2");
                                c.setFallbackUri("forward:/global-error");
                            })
                        )
                        .uri("lb://restroom-service")
                )
                .route(route -> route
                        .path("/employee/**")
                        .uri("lb://employee-service")
                )
                .build();
    }

}
