package com.service;

import com.core.Expression;
import com.core.SortBy;
import com.enums.DirectSort;
import com.enums.LogicalConnective;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvertService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Bson sortByListToBson(List<SortBy> sortByList) {
        List<Bson> orderBySorts = sortByList.stream()
                .map(sortBy -> sortBy.getDirectSort().equals(DirectSort.ASC) ?
                        Sorts.ascending(sortBy.getField()) :
                        Sorts.descending(sortBy.getField()))
                .collect(Collectors.toList());
        return Sorts.orderBy(orderBySorts);
    }

    public String documentsToString(FindIterable<Document> documents) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (Document document : documents){
            String str = document.toJson();
            result.append(str).append(",");
        }
        result.deleteCharAt(result.length() - 1).append("]");
        logger.info("Result query: " + result);
        return result.toString();
    }

    public Bson expressionsToBson(LinkedList<Expression> expressions, MongoCollection<Document> collection) {
        Iterator<Expression> expressionIterator = expressions.iterator();
        if (expressionIterator.hasNext()) {
            Expression expressionLeft = expressionIterator.next();
            Bson bsonLeft = createInequalityFilter(expressionLeft, collection);
            Bson bsonRight = null;
            while (expressionIterator.hasNext()) {
                Expression expressionRight = expressionIterator.next();
                bsonRight = createInequalityFilter(expressionRight, collection);
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

    private Bson createInequalityFilter(Expression expression, MongoCollection<Document> collection) {
        String field = expression.getCondition().getField();
        String value = expression.getCondition().getValue();
        Object forType = collection.find().first().get(field);
        switch (expression.getCondition().getInequality()) {
            case EQUALITY: {
                return Filters.eq(field, convertToMongoType(value, forType));
            }
            case NOT_EQUALS: {
                return Filters.ne(field, convertToMongoType(value, forType));
            }
            case LESS_THAN: {
                return Filters.lt(field, convertToMongoType(value, forType));
            }
            case LESS_THAN_EQUALS: {
                return Filters.lte(field, convertToMongoType(value, forType));
            }
            case GREATER_THAN: {
                return Filters.gt(field, convertToMongoType(value, forType));
            }
            case GREATER_THAN_EQUALS: {
                return Filters.gte(field, convertToMongoType(value, forType));
            }
            default: {
                throw new IllegalArgumentException("Error createInequalityFilter");
            }
        }
    }

    public Object convertToMongoType(String value, Object forType) {
        if (forType instanceof Double) {
            return Double.parseDouble(value);
        }
        else if (forType instanceof Integer) {
            return Integer.parseInt(value);
        }
        else if (forType instanceof String) {
            return value;
        }
        else if (forType instanceof List) {
            return Arrays.stream(value.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        return value;
    }

}