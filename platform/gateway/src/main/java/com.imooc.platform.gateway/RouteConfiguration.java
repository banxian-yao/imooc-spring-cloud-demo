package com.imooc.platform.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Autowired
    private KeyResolver hostNameResolver;

    @Autowired
    @Qualifier("restroomRateLimiter")
    private RateLimiter rateLimiterUser;

    private PathRoutePredicateFactory sd;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        .path("/toilet-service/**")
                        .filters(f -> f.requestRateLimiter(c -> {
                            c.setKeyResolver(hostNameResolver);
                            c.setRateLimiter(rateLimiterUser);
                        }))
                        .uri("lb://restroom-service")
                )
                .route(route -> route
                        .path("/employee/**")
                        .uri("lb://employee-service")
                )
                .build();
    }

}
