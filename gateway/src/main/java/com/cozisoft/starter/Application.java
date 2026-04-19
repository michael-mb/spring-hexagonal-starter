package com.cozisoft.starter;

import com.cozisoft.starter.config.MasterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MasterConfiguration.class})
public class Application {
    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
