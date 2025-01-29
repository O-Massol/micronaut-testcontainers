package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.bson.Document;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MicronautTest(environments = "test")
@Testcontainers
//test instance required for TestPropertyProvider
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MongoTest implements TestPropertyProvider {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");
    static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setup() {
        var mongoSetup = mongoSetup();
        if (mongoSetup != null && mongoSetup.databaseName() != null && mongoSetup.collectionNameList() != null) {
            mongoSetup.collectionNameList().forEach(collectionName ->
                    mongoClient.getDatabase(mongoSetup.databaseName()).createCollection(collectionName)
            );
        }
        var dataForInit = dataForInit();
        if (dataForInit != null) {
            dataForInit.forEach(fileForCollection -> {
                        pushDataToCollection(fileForCollection, mongoSetup);
                    }
            );
        }
    }

    private void pushDataToCollection(FileForCollection fileForCollection, MongoSetup mongoSetup) {
        try {
            var content = Files.readAllBytes(fileForCollection.pathToFile());
            var stringsFromContent = objectMapper.readValue(content, new TypeReference<List<String>>() {});
            var documents = stringsFromContent.stream().map(Document::parse).toList();
            mongoClient.getDatabase(mongoSetup.databaseName())
                    .getCollection(fileForCollection.collectionName())
                    .insertMany(documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void teardown() {
        if (mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }

    @Inject
    MongoClient mongoClient;

    public abstract MongoSetup mongoSetup();

    public List<FileForCollection> dataForInit() {
        return new ArrayList<>();
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        mongoDBContainer.start();
        return Map.of("mongodb.uri", mongoDBContainer.getConnectionString());
    }

    public record MongoSetup(String databaseName, List<String> collectionNameList) {
    }

    public record FileForCollection(String collectionName, Path pathToFile) {
    }
}
