package org.crm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "org.crm","org.crm.controller" })
@EntityScan(basePackages = { "org.crm.model.entity" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "org.crm.model.repository" })
@EnableCaching
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class);
    }

}