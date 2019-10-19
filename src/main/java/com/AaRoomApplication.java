package com;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@ServletComponentScan
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableCaching
@ComponentScan
public class AaRoomApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        /*ConfigurableApplicationContext context = SpringApplication.run(AaRoomApplication.class, args);
        context.getBean(Databus.class).start();*/
        SpringApplication.run(AaRoomApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AaRoomApplication.class);
    }

    @Autowired
    private RestTemplateBuilder builder;


    @Bean
    public RestTemplate restTemplate() {

        return builder.build();
    }

}
