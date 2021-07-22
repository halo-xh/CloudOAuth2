package com.xh.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.xh.common.feign"})
@EnableDiscoveryClient
@SpringBootApplication
public class SecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauthApplication.class, args);
    }

}
