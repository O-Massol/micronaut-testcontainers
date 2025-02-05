package space.guild.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@MicronautTest(environments = "test", transactional = false)
@Testcontainers
//test instance required for TestPropertyProvider
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class KafkaMongoTest implements TestPropertyProvider {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    static final KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka-native:3.8.0");

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

    private <T> void pushDataToCollection(FileForCollection<T> fileForCollection, MongoSetup mongoSetup) {
        try {
            var content = Files.readAllBytes(fileForCollection.pathToFile());
            var contentElements = (T[])objectMapper.readValue(content, fileForCollection.mongoDocumentListClass.arrayType());
            var documents = Arrays.stream(contentElements).map(ce-> {
                try {
                    return Document.parse(objectMapper.writeValueAsString(ce));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
            mongoClient.getDatabase(mongoSetup.databaseName())
                    .getCollection(fileForCollection.collectionName())
                    .insertMany(documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    void teardown() {
        if (mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
        if (kafkaContainer.isRunning()) {
            kafkaContainer.stop();
        }
    }

    @Inject
    MongoClient mongoClient;

    public abstract MongoSetup mongoSetup();

    public List<FileForCollection<?>> dataForInit() {
        return new ArrayList<>();
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        mongoDBContainer.start();
        kafkaContainer.start();
        return Map.of(
                "mongodb.uri", mongoDBContainer.getConnectionString()+"/SPACEGUILD",
                "kafka.bootstrap.servers", kafkaContainer.getBootstrapServers()
        );
    }

    public record MongoSetup(String databaseName, List<String> collectionNameList) {
    }

    public record FileForCollection<T>(String collectionName, Class<T> mongoDocumentListClass, Path pathToFile) {
    }
}
