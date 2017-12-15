package com.service;

import com.bean.QuerySQLBean;
import com.core.Expression;
import com.core.SortBy;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class BuildQueryService {

    private ConvertService convertService;
    private ParsingService parsingService;

    @Autowired
    public BuildQueryService(ConvertService convertService, ParsingService parsingService) {
        this.convertService = convertService;
        this.parsingService = parsingService;
    }

    public FindIterable<Document> buildQueryToMongoDB(QuerySQLBean querySQLBean, MongoCollection<Document> collection) {
        FindIterable<Document> result;

        if (querySQLBean.existWhere()) {
            LinkedList<Expression> expressions = parsingService.parseExpression(querySQLBean.getWhere());
            Bson filter = convertService.expressionsToBson(expressions, collection);
            result = collection.find(filter);
        }
        else {
            result = collection.find();
        }

        List<String> selects = parsingService.parseSelect(querySQLBean.getSelect());
        result.projection(Projections.include(selects));

        if (querySQLBean.existOrderBy()) {
            List<SortBy> sortByList = parsingService.parseSort(querySQLBean.getOrderBy());
            Bson sort = convertService.sortByListToBson(sortByList);
            result.sort(sort);
        }

        if (querySQLBean.existLimit()) {
            int limit = parsingService.parseLimit(querySQLBean.getLimit());
            result.limit(limit);
        }

        if (querySQLBean.existSkip()) {
            int skip = parsingService.parseSkip(querySQLBean.getSkip());
            result.skip(skip);
        }

        return result;
    }

}
