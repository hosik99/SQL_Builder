import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SqlBuilder {

    private final StringBuilder query;

    public SqlBuilder(){
        this.query = new StringBuilder();
    }

    // p.title -> p.title AS p_title
    private String[] asCasting(String [] fields){
        return Arrays.stream(fields)
                .map(field -> {
                    if (field.contains(".")) {
                        String[] f = field.split("\\.");
                        return field + " AS "+ f[0] + "_" + f[1];  // 테이블명_컬럼명 형식
                    }
                    return field;  // 점이 없으면 그대로 사용
                })
                .toArray(String[]::new);
    }

    public SqlBuilder select(String... fields) {
        if (fields.length == 0) { query.append("SELECT *"); return this; }
        String[] processedFields = asCasting(fields);
        query.append("SELECT ").append(String.join(", ", processedFields));
        return this;
    }

    public SqlBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    public SqlBuilder as(String alias) {
        query.append(" AS ").append(alias);
        return this;
    }

    public SqlBuilder join(String table) {
        query.append(" JOIN ").append(table);
        return this;
    }

    public SqlBuilder on(String condition) {
        query.append(" ON ").append(condition);
        return this;
    }

    public SqlBuilder where(String condition) {
        query.append(" WHERE ").append(condition);
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
        clear();
        return result;
    }

    public void clear() {
        query.setLength(0); // 쿼리 초기화
    }

    private static String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase
                .replaceAll("([a-z])([A-Z])", "$1_$2") // 소문자와 대문자 경계에 "_" 추가
                .toLowerCase(); // 소문자로 변환
    }
}
