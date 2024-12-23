package com.icetea.monstu_back.r2dbc.sqlBuilder;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SqlBuilder {

    private final StringBuilder query;

    public SqlBuilder(){
        this.query = new StringBuilder();
    }

    public SqlBuilder select(String... fields) {
        if (fields.length == 0) { query.append("SELECT *"); return this; }  //.from() 필요
        query.append("SELECT ").append(String.join(", ", fields));
        return this;
    }

    // p.table -> ["p","table"]
    private String[] splitTableName(String tableName) {
        if(tableName.contains(".")) return tableName.split("\\.");
        return null;
    }

    public SqlBuilder from(String tableName) {
        String[] array = splitTableName(tableName);
        if(array != null){
            query.append(" FROM ").append(array[1]).append(" AS ").append(array[0]);
        }else{
            query.append(" FROM ").append(tableName);
        }
        return this;
    }

    public SqlBuilder as(String alias) {
        query.append(" AS ").append(alias);
        return this;
    }

    public SqlBuilder join(String tableName) {
        String[] array = splitTableName(tableName);
        if(array != null){
            query.append(" JOIN ").append(array[1]).append(" AS ").append(array[0]);
        }else{
            query.append(" JOIN ").append(tableName);
        }
        return this;
    }

    public SqlBuilder on(String condition) {
        query.append(" ON ").append(condition);
        return this;
    }

    public SqlBuilder between(String one,String two) {
        query.append(" BETWEEN ").append(one).append(" AND ").append(two);
        return this;
    }

    public SqlBuilder where(String condition) {
        query.append(" WHERE ").append(condition);
        return this;
    }

    public SqlBuilder orderBy(String sortValue,String direction) {
        query.append(" ORDER BY ").append(sortValue).append(" ").append(direction);
        return this;
    }

    public SqlBuilder orderBy(Sort sort) {
        StringBuilder orderByClause = new StringBuilder(" ORDER BY ");
        for (Sort.Order order : sort) {
            String field = order.getProperty(); // 필드 이름
            String direction = order.getDirection().name(); // ASC 또는 DESC
            orderByClause.append(field).append(" ").append(direction).append(", ");
        }
        query.append(orderByClause);
        System.out.println(query.toString());
        return this;
    }

    public SqlBuilder orderBy(Sort sort,String nickname) {
        StringBuilder orderByClause = new StringBuilder(" ORDER BY ");
        for (Sort.Order order : sort) {
            String field = order.getProperty(); // 필드 이름
            String direction = order.getDirection().name(); // ASC 또는 DESC
            orderByClause.append(nickname).append(".").append(field).append(" ").append(direction).append(", ");
        }
        if (!orderByClause.isEmpty()) { orderByClause.setLength(orderByClause.length() - 2); }
        query.append(orderByClause);
        return this;
    }

    public SqlBuilder limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    public SqlBuilder offset(int offset) {
        query.append(" OFFSET ").append(offset);
        return this;
    }

    public SqlBuilder eq(String condition) {
        query.append(" = ").append(condition);
        return this;
    }

    public String build() {
        String result = query.toString().trim();
        System.out.println(result);
        clear();
        return result;
    }

    public void clear() {
        query.setLength(0); // 쿼리 초기화
    }


}
