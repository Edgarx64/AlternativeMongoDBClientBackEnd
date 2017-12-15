package com.manager;

import com.bean.QuerySQLBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.service.BuildQueryService;
import com.service.ConvertService;
import com.service.MongoConnectionService;
import com.service.ParsingService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SQLQueryToMongoDBManager implements QueryManager {

    private MongoCollection<Document> collection = null;

    private ConvertService convertService;
    private ParsingService parsingService;
    private MongoConnectionService mongoConnectionService;
    private BuildQueryService buildQueryService;

    @Autowired
    public SQLQueryToMongoDBManager(ConvertService convertService, ParsingService parsingService, MongoConnectionService mongoConnectionService, BuildQueryService buildQueryService) {
        this.convertService = convertService;
        this.parsingService = parsingService;
        this.mongoConnectionService = mongoConnectionService;
        this.buildQueryService = buildQueryService;
    }

    @Override
    public String getDataByQuery(String querySQL) {
        QuerySQLBean querySQLBean = parsingService.parseQuerySQL(querySQL);

        collection = mongoConnectionService.getCollectionFromDatabase(querySQLBean.getFrom());

        FindIterable<Document> documents = buildQueryService.buildQueryToMongoDB(querySQLBean, collection);

        return convertService.documentsToString(documents);
    }
}
