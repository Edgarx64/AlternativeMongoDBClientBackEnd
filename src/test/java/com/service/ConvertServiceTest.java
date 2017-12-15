package com.service;

import com.core.Condition;
import com.core.Expression;
import com.core.SortBy;
import com.enums.DirectSort;
import com.enums.Inequality;
import com.enums.LogicalConnective;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConvertServiceTest {

    private ConvertService convertService;

    @Before
    public void before() {
        convertService = new ConvertService();
    }

    @Test
    public void sortByListToBson() {
        List<Bson> bsonList = Arrays.asList(
                Sorts.ascending("a"),
                Sorts.descending("b")
        );
        List<SortBy> sortByList = Arrays.asList(
                new SortBy("a", DirectSort.ASC),
                new SortBy("b", DirectSort.DESC)
        );
        assertEquals(Sorts.orderBy(bsonList).toString(), convertService.sortByListToBson(sortByList).toString());
    }

    class MongoCursorForTest implements MongoCursor<Document> {
        Iterator<Document> iterator = Arrays.asList(
                new Document("key1", "value1"),
                new Document("key2", "value2")
        ).iterator();
        @Override
        public void close() {

        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Document next() {
            return iterator.next();
        }

        @Override
        public Document tryNext() {
            return null;
        }

        @Override
        public ServerCursor getServerCursor() {
            return null;
        }

        @Override
        public ServerAddress getServerAddress() {
            return null;
        }
    }

    @Test
    public void documentsToString() {
        MongoCursor<Document> cursor = new MongoCursorForTest();

        FindIterable<Document> documents = mock(FindIterable.class);
        when(documents.iterator()).thenReturn(cursor);

        String expected = "[{ \"key1\" : \"value1\" },{ \"key2\" : \"value2\" }]";
        assertEquals(expected, convertService.documentsToString(documents));
    }

    @Test
    public void expressionsToBson() {
        LinkedList<Expression> expressions = new LinkedList<>();
        expressions.add(new Expression(new Condition("a", Inequality.EQUALITY, "asd"), LogicalConnective.AND));
        expressions.add(new Expression(new Condition("b", Inequality.LESS_THAN, "8"), LogicalConnective.NONE));

        MongoCollection<Document> collection = mock(MongoCollection.class);
        when(collection.find()).thenReturn(mock(FindIterable.class));
        when(collection.find().first()).thenReturn(mock(Document.class));
        when(collection.find().first().get("a")).thenReturn("");
        when(collection.find().first().get("b")).thenReturn(1);

        Bson actual = convertService.expressionsToBson(expressions, collection);
        String expected = "And Filter{filters=[Filter{fieldName='a', value=asd}, Operator Filter{fieldName='b', operator='$lt', value=8}]}";
        assertEquals(expected, actual.toString());
    }

    @Test
    public void convertToMongoTypeForDouble() {
        assertEquals(5.2, convertService.convertToMongoType("5.2", 2.2));
    }

    @Test
    public void convertToMongoTypeForInteger() {
        assertEquals(5, convertService.convertToMongoType("5", 2));
    }

    @Test
    public void convertToMongoTypeForString() {
        assertEquals("test", convertService.convertToMongoType("test", "forType"));
    }

    @Test
    public void convertToMongoTypeForArray() {
        assertEquals(Arrays.asList("one", "two"), convertService.convertToMongoType("one, two", Arrays.asList("one", "two")));
    }
}