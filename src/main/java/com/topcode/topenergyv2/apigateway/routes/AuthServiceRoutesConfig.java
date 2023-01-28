package com.topcode.topenergyv2.apigateway.routes;

import com.topcode.topenergyv2.apigateway.filters.AuthorizationFilterFactory;
import com.topcode.topenergyv2.apigateway.filters.AuthorizationFilterFactory.Config;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthServiceRoutesConfig {


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthorizationFilterFactory authorizationFilterFactory) {

        return builder.routes()
                .route("auth_service_auth_login", r -> r.path("/auth/login").uri("lb://auth-service/"))
                .route("auth_service_auth_renew", r -> r.path("/auth/renew").uri("lb://auth-service"))
                .route("auth_service_users_user", r -> r.path("/users/**").filters(f->f.rewritePath("/users/(?<segment>.*)","/users/${segment}").filter(authorizationFilterFactory.apply(new Config()))).uri("lb://auth-service/"))
                .route("payments_service_stripe_createCheckoutSession", r -> r.path("/stripe/*/createCheckoutSession").filters(f->f.rewritePath("/stripe/(?<segment>.*)/createCheckoutSession","/stripe/${segment}/createCheckoutSession").filter(authorizationFilterFactory.apply(new Config()))).uri("lb://payments-service/"))
                .build();
    }
}

