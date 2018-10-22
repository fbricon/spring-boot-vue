package com.redhat.vscode.demo;

import com.redhat.vscode.demo.repositories.TodoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TodoApplication {

    private static final Logger LOG = LoggerFactory.getLogger(TodoApplication.class);

    @Autowired
    private TodoRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRepository() {
        if (repository.findAll().isEmpty()) {
            repository.resetData();
            LOG.info("Initialized repository with mock data");
        } else {
            LOG.info("Repository already initialized");
        }
    }
}
