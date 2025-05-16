package org.jboss.as.quickstarts.kitchensink.test.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@TestConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.jboss.as.quickstarts.kitchensink")
@EnableMongoRepositories(basePackages = "org.jboss.as.quickstarts.kitchensink.repository")
@EnableMongoAuditing
public class TestConfig {

    @LocalServerPort
    private Integer port;

    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }
}
