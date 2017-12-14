package com.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MongoConnectionService {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String database;

    private MongoDatabase mongoDatabase;

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        if (null == mongoDatabase) {
            mongoDatabase = mongoClient.getDatabase(database);
            return mongoDatabase;
        }
        return mongoDatabase;
    }

    public MongoCollection<Document> getCollectionFromDatabase(String name) {
        return getMongoDatabase().getCollection(name);
    }
}
