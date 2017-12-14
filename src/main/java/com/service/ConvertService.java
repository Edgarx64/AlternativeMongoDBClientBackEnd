package com.service;

import com.bean.QuerySQLBean;
import com.core.Expression;
import com.core.SortBy;
import com.enums.DirectSort;
import com.enums.LogicalConnective;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConvertService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MongoCollection<Document> collection = null;

    private MongoConnectionService mongoConnectionService;
    private ParsingService parsingService;

    @Autowired
    public ConvertService(MongoConnectionService mongoConnectionService, ParsingService parsingService) {
        this.mongoConnectionService = mongoConnectionService;
        this.parsingService = parsingService;
    }

    public String query(String querySQL) {
        QuerySQLBean querySQLBean = parsingService.parseQuerySQL(querySQL);

        collection = mongoConnectionService.getCollectionFromDatabase(querySQLBean.getFrom());

        FindIterable<Document> documents = buildQueryToMongoDB(querySQLBean);

        return documentsToString(documents);
    }

    private FindIterable<Document> buildQueryToMongoDB(QuerySQLBean querySQLBean) {
        FindIterable<Document> result;

        if (querySQLBean.existWhere()) {
            LinkedList<Expression> expressions = parsingService.parseExpression(querySQLBean.getWhere());
            Bson filter = expressionsToBson(expressions);
            result = collection.find(filter);
        }
        else {
            result = collection.find();
        }

        List<String> selects = parsingService.parseSelect(querySQLBean.getSelect());
        result.projection(Projections.include(selects));

        if (querySQLBean.existOrderBy()) {
            List<SortBy> sortByList = parsingService.parseSort(querySQLBean.getOrderBy());
            Bson sort = sortByListToBson(sortByList);
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

    private Bson sortByListToBson(List<SortBy> sortByList) {
        List<Bson> orderBySorts = sortByList.stream()
                .map(sortBy -> sortBy.getDirectSort().equals(DirectSort.ASC) ?
                        Sorts.ascending(sortBy.getField()) :
                        Sorts.descending(sortBy.getField()))
                .collect(Collectors.toList());
        return Sorts.orderBy(orderBySorts);
    }

    private String documentsToString(FindIterable<Document> documents) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (Document document : documents){
            String str = document.toJson();
            result.append(str).append(",");
        }
        result.deleteCharAt(result.length() - 1).append("]");
        logger.info("Collection count: " + collection.count());
        logger.info("Result query: " + result);
        return result.toString();
    }

    private Bson expressionsToBson(LinkedList<Expression> expressions) {
        Iterator<Expression> expressionIterator = expressions.iterator();
        if (expressionIterator.hasNext()) {
            Expression expressionLeft = expressionIterator.next();
            Bson bsonLeft = createInequalityFilter(expressionLeft);
            Bson bsonRight = null;
            while (expressionIterator.hasNext()) {
                Expression expressionRight = expressionIterator.next();
                bsonRight = createInequalityFilter(expressionRight);
                bsonLeft = createLogicalConnectiveFilter(expressionLeft.getLogicalConnective(), bsonLeft, bsonRight);
                expressionLeft = expressionRight;
            }
            return bsonLeft;
        }
        return new Document();
    }

    private Bson createLogicalConnectiveFilter(LogicalConnective logicalConnective, Bson bsonLeft, Bson bsonRight) {
        switch (logicalConnective) {
            case AND: {
                return Filters.and(bsonLeft, bsonRight);
            }
            case OR: {
                return Filters.or(bsonLeft, bsonRight);
            }
            default: {
                throw new IllegalArgumentException("Error createLogicalConnectiveFilter");
            }
        }
    }

    private Bson createInequalityFilter(Expression expression) {
        String field = expression.getCondition().getField();
        String value = expression.getCondition().getValue();
        switch (expression.getCondition().getInequality()) {
            case EQUALITY: {
                return Filters.eq(field, convertToMongoType(field, value));
            }
            case NOT_EQUALS: {
                return Filters.ne(field, convertToMongoType(field, value));
            }
            case LESS_THAN: {
                return Filters.lt(field, convertToMongoType(field, value));
            }
            case LESS_THAN_EQUALS: {
                return Filters.lte(field, convertToMongoType(field, value));
            }
            case GREATER_THAN: {
                return Filters.gt(field, convertToMongoType(field, value));
            }
            case GREATER_THAN_EQUALS: {
                return Filters.gte(field, convertToMongoType(field, value));
            }
            default: {
                throw new IllegalArgumentException("Error createInequalityFilter");
            }
        }
    }

    private Object convertToMongoType(String field, String value) {
        Object o = collection.find().first().get(field);
        if (o instanceof Double) {
            return Double.parseDouble(value);
        }
        else if (o instanceof Integer) {
            return Integer.parseInt(field);
        }
        else if (o instanceof String) {
            return value;
        }
        else if (o instanceof ArrayList) {
            return Arrays.stream(value.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return value;
    }

}