package com.example;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import io.micronaut.configuration.mongo.core.DefaultMongoClientSettingsFactory;
import io.micronaut.configuration.mongo.core.DefaultMongoConfiguration;
import io.micronaut.configuration.mongo.core.NamedMongoConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest(environments = "test")
@Testcontainers
class DemoTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    MongoClientSettings mongoClientSettings;

    @Inject
    MongoClient mongoClient;

    @Test
    void testItWorks() {
        mongoDBContainer.getConnectionString();
        var existingSettings = mongoClientSettings;

        var appConfig = new ApplicationConfiguration();
        var config = new NamedMongoConfiguration("localhost", null);
        var mongoDriverInformation = MongoDriverInformation.builder()
                .driverName("sync")
                .build();
        MongoClient client = new MongoClientImpl(mongoClientSettings, mongoDriverInformation);
        Assertions.assertTrue(application.isRunning());
    }

}
