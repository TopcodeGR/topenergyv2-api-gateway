package com.topcode.topenergyv2.apigateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.topcode.topenergyv2.apigateway.utils.APIResponse;
import com.topcode.topenergyv2.apigateway.webclients.AuthServiceWebClient;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

@Component
public class AuthorizationFilterFactory  extends AbstractGatewayFilterFactory<AuthorizationFilterFactory.Config> {

    private final AuthServiceWebClient authServiceWebClient;
    @Autowired
    private ObjectMapper objectMapper;

    public AuthorizationFilterFactory(AuthServiceWebClient authServiceWebClient) {
        super(Config.class);
        this.authServiceWebClient = authServiceWebClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authorization = exchange.getRequest().getHeaders().get("authorization").get(0);
            String clientid = exchange.getRequest().getHeaders().get("clientid").get(0);
            String clientsecret = exchange.getRequest().getHeaders().get("clientsecret").get(0);
            return authServiceWebClient.authorizeRequest(authorization,clientid,clientsecret).flatMap(r->{
                if(!r.isSuccess()){
                    DataBuffer buffer = null;
                    try {
                        buffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsString(r.getAsMap()).getBytes());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().add("Content-Type","application/json");
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                }
                return chain.filter(exchange);
            });

        };

    }

    public static class Config {


        public Config() {
        }


    }
}
