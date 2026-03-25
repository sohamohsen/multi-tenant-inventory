package com.task.multitenantinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MultiTenantInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantInventoryApplication.class, args);
    }

}
