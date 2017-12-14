package com.bean;

import com.enums.SQLKeyword;

public class QuerySQLBean {
    private String select;
    private String from;
    private String where;
    private String groupBy;
    private String orderBy;
    private String skip;
    private String limit;

    private QuerySQLBean() {

    }

    public static QuerySQLBean build() {
        return new QuerySQLBean();
    }

    public String getSelect() {
        return select;
    }

    public String getFrom() {
        return from;
    }

    public String getWhere() {
        return where;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getSkip() {
        return skip;
    }

    public String getLimit() {
        return limit;
    }

    public QuerySQLBean setSelect(String select) {
        this.select = select;
        return this;
    }

    public QuerySQLBean setFrom(String from) {
        this.from = from;
        return this;
    }

    public QuerySQLBean setWhere(String where) {
        this.where = where;
        return this;
    }

    public QuerySQLBean setGroupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public QuerySQLBean setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public QuerySQLBean setSkip(String skip) {
        this.skip = skip;
        return this;
    }

    public QuerySQLBean setLimit(String limit) {
        this.limit = limit;
        return this;
    }

    public QuerySQLBean set(SQLKeyword keyword, String s) {
        switch (keyword) {
            case SELECT: {
                return setSelect(s);
            }
            case FROM: {
                return setFrom(s);
            }
            case WHERE: {
                return setWhere(s);
            }
            case GROUP_BY: {
                return setGroupBy(s);
            }
            case ORDER_BY: {
                return setOrderBy(s);
            }
            case SKIP: {
                return setSkip(s);
            }
            case LIMIT: {
                return setLimit(s);
            }
        }
        return this;
    }

    public boolean existSelect() {
        return null != select && !select.isEmpty();
    }

    public boolean existFrom() {
        return null != from && !from.isEmpty();
    }

    public boolean existWhere() {
        return null != where && !where.isEmpty();
    }

    public boolean existGroupBy() {
        return null != groupBy && !groupBy.isEmpty();
    }

    public boolean existOrderBy() {
        return null != orderBy && !orderBy.isEmpty();
    }

    public boolean existSkip() {
        return null != skip && !skip.isEmpty();
    }

    public boolean existLimit() {
        return null != limit && !limit.isEmpty();
    }

    @Override
    public String toString() {
        return "QuerySQLBean{" +
                "select='" + select + '\'' +
                ", from='" + from + '\'' +
                ", where='" + where + '\'' +
                ", groupBy='" + groupBy + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", skip='" + skip + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
