package com.example.infrastructure.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.inject.Singleton;

@Singleton
public class MongoCollectionAccessWrapper {

    private final MongoClient mongoClient;

    public MongoCollectionAccessWrapper(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoCollection getMainCollection(){
        return this.mongoClient.getDatabase("mydatabase").getCollection("main-collection");
    }


}
