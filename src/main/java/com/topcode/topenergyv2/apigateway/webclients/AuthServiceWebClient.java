package com.topcode.topenergyv2.apigateway.webclients;

import com.topcode.topenergyv2.apigateway.utils.JWTTokenValidationData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import com.topcode.topenergyv2.apigateway.utils.APIResponse;

@Component
public class AuthServiceWebClient {



    private final WebClient client;

    public AuthServiceWebClient(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://auth-service").build();
    }

    public Mono<APIResponse<JWTTokenValidationData>> authorizeRequest(String authorization, String clientid, String clientSecret){
        return this.client.post().uri("/auth/authorizeRequest").headers(headers->{
            headers.set("Authorization",authorization);
            headers.set("clientid",clientid);
            headers.set("clientsecret",clientSecret);
        }).exchangeToMono(res->{
            return res.bodyToMono(new ParameterizedTypeReference<APIResponse<JWTTokenValidationData>>(){}).map(r->{
                return r;
            });
        });
    }

}
