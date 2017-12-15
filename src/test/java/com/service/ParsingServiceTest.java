package com.service;

import com.bean.QuerySQLBean;
import com.core.Condition;
import com.core.Expression;
import com.core.SortBy;
import com.enums.DirectSort;
import com.enums.Inequality;
import com.enums.LogicalConnective;
import com.exception.ParsingRuntimeException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ParsingServiceTest {

    private ParsingService parsingService;

    @Before
    public void createParsingService() {
        parsingService = new ParsingService();
    }

    @Test
    public void parseExpression() throws Exception {
        List<Expression> expected = new LinkedList<>();
        expected.add(new Expression(new Condition("a", Inequality.LESS_THAN, "g"), LogicalConnective.AND));
        expected.add(new Expression(new Condition("f", Inequality.EQUALITY, "8"), LogicalConnective.OR));
        expected.add(new Expression(new Condition("y", Inequality.NOT_EQUALS, "asd"), LogicalConnective.NONE));
        List<Expression> actual = parsingService.parseExpression("a < g AND f = 8 OR y <> asd");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEqualsExpression(expected.get(i), actual.get(i));
        }
    }

    private void assertEqualsExpression(Expression expected, Expression actual) {
        assertEqualsCondition(expected.getCondition(), actual.getCondition());
        assertEquals(expected.getLogicalConnective(), actual.getLogicalConnective());
    }

    @Test
    public void parseConditionEquality() throws Exception {
        Condition expected = new Condition("a", Inequality.EQUALITY, "5");
        Condition actual = parsingService.parseCondition("a = 5");
        assertEqualsCondition(expected, actual);
    }

    @Test
    public void parseConditionNotEquals() throws Exception {
        Condition expected = new Condition("a", Inequality.NOT_EQUALS, "5");
        Condition actual = parsingService.parseCondition("a <> 5");
        assertEqualsCondition(expected, actual);
    }

    @Test
    public void parseConditionLessThan() throws Exception {
        Condition expected = new Condition("a", Inequality.LESS_THAN, "5");
        Condition actual = parsingService.parseCondition("a < 5");
        assertEqualsCondition(expected, actual);
    }

    @Test
    public void parseConditionLessThanEquals() throws Exception {
        Condition expected = new Condition("a", Inequality.LESS_THAN_EQUALS, "5");
        Condition actual = parsingService.parseCondition("a <= 5");
        assertEqualsCondition(expected, actual);
    }

    @Test
    public void parseConditionGreaterThan() throws Exception {
        Condition expected = new Condition("a", Inequality.GREATER_THAN, "5");
        Condition actual = parsingService.parseCondition("a > 5");
        assertEqualsCondition(expected, actual);
    }

    @Test
    public void parseConditionGreaterThanEquals() throws Exception {
        Condition expected = new Condition("a", Inequality.GREATER_THAN_EQUALS, "5");
        Condition actual = parsingService.parseCondition("a >= 5");
        assertEqualsCondition(expected, actual);
    }

    @Test(expected = ParsingRuntimeException.class)
    public void parseConditionException() throws Exception {
        parsingService.parseCondition("a ^ b");
    }

    private void assertEqualsCondition(Condition expected, Condition actual) {
        assertEquals(expected.getField(), actual.getField());
        assertEquals(expected.getInequality(), actual.getInequality());
        assertEquals(expected.getValue(), actual.getValue());
    }

    @Test
    public void parseQuerySQLFull() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post")
                .setWhere("likes > 10")
                .setGroupBy("a")
                .setOrderBy("a ASC")
                .setSkip("2")
                .setLimit("5");
        String query = "SELECT title, description, likes FROM post WHERE likes > 10 GROUP BY a ORDER BY a ASC SKIP 2 LIMIT 5";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    @Test
    public void parseQuerySQLWithoutOne() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post")
                .setWhere("likes > 10")
                .setGroupBy("a")
                .setOrderBy("a ASC")
                .setSkip("2");
        String query = "SELECT title, description, likes FROM post WHERE likes > 10 GROUP BY a ORDER BY a ASC SKIP 2";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    @Test
    public void parseQuerySQLWithoutTwo() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post")
                .setWhere("likes > 10")
                .setOrderBy("a ASC")
                .setSkip("2");
        String query = "SELECT title, description, likes FROM post WHERE likes > 10 ORDER BY a ASC SKIP 2";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    @Test
    public void parseQuerySQLWithoutThree() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post")
                .setWhere("likes > 10")
                .setSkip("2");
        String query = "SELECT title, description, likes FROM post WHERE likes > 10 SKIP 2";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    @Test
    public void parseQuerySQLWithoutFour() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post")
                .setWhere("likes > 10");
        String query = "SELECT title, description, likes FROM post WHERE likes > 10";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    @Test
    public void parseQuerySQLWithoutFive() throws Exception {
        QuerySQLBean expected = QuerySQLBean.build()
                .setSelect("title, description, likes")
                .setFrom("post");
        String query = "SELECT title, description, likes FROM post";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);

        assertEqualsQuerySQLBean(expected, actual);
    }

    private void assertEqualsQuerySQLBean(QuerySQLBean expected, QuerySQLBean actual) {
        assertEquals(expected.getSelect(), actual.getSelect());
        assertEquals(expected.getFrom(), actual.getFrom());
        assertEquals(expected.getWhere(), actual.getWhere());
        assertEquals(expected.getGroupBy(), actual.getGroupBy());
        assertEquals(expected.getOrderBy(), actual.getOrderBy());
        assertEquals(expected.getSkip(), actual.getSkip());
        assertEquals(expected.getLimit(), actual.getLimit());
    }

    @Test(expected = ParsingRuntimeException.class)
    public void parseQuerySQLException() throws Exception {
        String query = "select * fr om a";
        QuerySQLBean actual = parsingService.parseQuerySQL(query);
    }

    @Test
    public void parseSelect() throws Exception {
        List<String> expected = Arrays.asList("a", "b", "c");
        List<String> actual = parsingService.parseSelect("a, b, c.*");
        assertEquals(expected, actual);
    }

    @Test
    public void parseSelectStar() throws Exception {
        List<String> expected = Collections.emptyList();
        List<String> actual = parsingService.parseSelect(" * ");
        assertEquals(expected, actual);
    }

    @Test
    public void parseSort() throws Exception {
        List<SortBy> expected = Arrays.asList(
                new SortBy("a", DirectSort.ASC),
                new SortBy("b", DirectSort.ASC),
                new SortBy("c", DirectSort.DESC)
        );
        List<SortBy> actual = parsingService.parseSort("a, b ASC, c DESC");
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getField(), actual.get(i).getField());
            assertEquals(expected.get(i).getDirectSort(), actual.get(i).getDirectSort());
        }
    }

    @Test
    public void parseSkip() throws Exception {
        Integer expected = 50;
        Integer actual = parsingService.parseSkip(" 50 ");
        assertEquals(expected, actual);
    }

    @Test(expected = ParsingRuntimeException.class)
    public void parseSkipException() throws Exception {
        parsingService.parseSkip("a");
    }

    @Test
    public void parseLimit() throws Exception {
        Integer expected = 50;
        Integer actual = parsingService.parseLimit(" 50 ");
        assertEquals(expected, actual);
    }

    @Test(expected = ParsingRuntimeException.class)
    public void parseLimitException() throws Exception {
        parsingService.parseLimit("b");
    }

}