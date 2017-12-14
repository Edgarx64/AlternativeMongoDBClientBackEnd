package com.service;

import com.bean.QuerySQLBean;
import com.core.Condition;
import com.core.Expression;
import com.core.SortBy;
import com.enums.DirectSort;
import com.enums.Inequality;
import com.enums.LogicalConnective;
import com.enums.SQLKeyword;
import com.exception.ParsingRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ParsingService {

    private static Pattern PATTERN_PARSE_EXPRESSION = Pattern.compile("(.*?)(AND|and|OR|or|$)");
    private static Pattern PATTERN_PARSE_CONDITION = Pattern.compile("(.*?)(<=|>=|<>|<|>|=)(.*)");

    private static final List<Pattern> PATTERNS_PARSE_QUERY_SQL = Arrays.asList(
            Pattern.compile("(SELECT)(.*?)(FROM)(.*?)(WHERE|GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(ORDER BY|SKIP|LIMIT)(.*?)(SKIP|LIMIT)(.*?)(LIMIT)(.*)"),
            Pattern.compile("(SELECT)(.*?)(FROM)(.*?)(WHERE|GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(ORDER BY|SKIP|LIMIT)(.*?)(SKIP|LIMIT)(.*)"),
            Pattern.compile("(SELECT)(.*?)(FROM)(.*?)(WHERE|GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(ORDER BY|SKIP|LIMIT)(.*)"),
            Pattern.compile("(SELECT)(.*?)(FROM)(.*?)(WHERE|GROUP BY|ORDER BY|SKIP|LIMIT)(.*?)(GROUP BY|ORDER BY|SKIP|LIMIT)(.*)"),
            Pattern.compile("(SELECT)(.*?)(FROM)(.*?)(WHERE|GROUP BY|ORDER BY|SKIP|LIMIT)(.*)"),
            Pattern.compile("(SELECT)(.*?)(FROM)(.*)")
    );

    public LinkedList<Expression> parseExpression(String where) {
        LinkedList<Expression> result = new LinkedList<>();
        Matcher matcher = PATTERN_PARSE_EXPRESSION.matcher(where);
        while (matcher.find() && !matcher.group(0).isEmpty()) {
            String conditionString = matcher.group(1).trim();
            String logicalConnectiveString = matcher.group(2).trim();
            Condition condition = parseCondition(conditionString);
            LogicalConnective logicalConnective = LogicalConnective.parse(logicalConnectiveString);
            Expression expression = new Expression(condition, logicalConnective);
            result.add(expression);
        }
        return result;
    }

    public Condition parseCondition(String condition) {
        Matcher matcher = PATTERN_PARSE_CONDITION.matcher(condition);
        if (matcher.matches()) {
            return new Condition(
                    matcher.group(1).trim(),
                    Inequality.parse(matcher.group(2)),
                    matcher.group(3).trim()
            );
        }
        throw new ParsingRuntimeException("Could not parse condition: " + condition);
    }

    public QuerySQLBean parseQuerySQL(String query) {
        QuerySQLBean result = QuerySQLBean.build();

        Matcher matcher = null;
        for (Pattern pattern : PATTERNS_PARSE_QUERY_SQL) {
            Matcher m = pattern.matcher(query);
            if (m.matches()) {
                matcher = m;
                break;
            }
        }
        if (null == matcher) {
            throw new ParsingRuntimeException("Could not parse query SQL: " + query);
        }

        int indexGroup = 1;
        while (indexGroup <= matcher.groupCount()) {
            String group = matcher.group(indexGroup).trim();
            SQLKeyword keyword = SQLKeyword.parse(group);
            result.set(keyword, matcher.group(indexGroup + 1).trim());
            indexGroup += 2;
        }

        if (!result.existSelect()) {
            throw new ParsingRuntimeException("Query does not have keyword SELECT: " + query);
        }
        if (!result.existFrom()) {
            throw new ParsingRuntimeException("Query does not have keyword FROM: " + query);
        }

        return result;
    }

    public List<String> parseSelect(String select) {
        List<String> selects = Arrays.stream(select.split(","))
                .map(String::trim)
                .map(s -> s.replace(".*", ""))
                .collect(Collectors.toList());
        if (selects.size() == 1 && selects.get(0).equals("*")) {
            selects = new ArrayList<>();
        }
        return selects;
    }

    public List<SortBy> parseSort(String orderBy) {
        return Arrays.stream(orderBy.split(","))
                .map(String::trim)
                .map(this::getOrderBySort)
                .collect(Collectors.toList());
    }

    private SortBy getOrderBySort(String s) {
        String[] strings = s.split(" ");
        if (strings.length == 1) {
            return new SortBy(strings[0].trim(), DirectSort.ASC);
        }
        else if (strings.length == 2) {
            return new SortBy(strings[0].trim(), DirectSort.parse(strings[1].trim()));
        }
        else {
            throw new ParsingRuntimeException("Could not parse order by sort: " + s);
        }
    }

    public Integer parseSkip(String skip) {
        try {
            return Integer.parseInt(skip.trim());
        } catch (NumberFormatException e) {
            throw new ParsingRuntimeException("Could not parse skip: " + skip);
        }
    }

    public Integer parseLimit(String limit) {
        try {
            return Integer.parseInt(limit.trim());
        } catch (NumberFormatException e) {
            throw new ParsingRuntimeException("Could not parse limit: " + limit);
        }
    }
}
