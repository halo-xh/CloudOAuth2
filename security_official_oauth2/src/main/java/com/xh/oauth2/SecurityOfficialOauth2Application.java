package com.xh.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = {"com.xh.common.feign"})
@EnableDiscoveryClient
@SpringBootApplication
public class SecurityOfficialOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOfficialOauth2Application.class, args);
    }

}
