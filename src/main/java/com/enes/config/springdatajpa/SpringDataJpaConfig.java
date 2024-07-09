package com.enes.config.springdatajpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {
        "org.springframework.statemachine.data.jpa",
        "com.enes.repository"
})
@EntityScan(basePackages = {
        "org.springframework.statemachine.data.jpa",
        "com.enes.model.entity"
})
@EnableTransactionManagement
public class SpringDataJpaConfig {
}
