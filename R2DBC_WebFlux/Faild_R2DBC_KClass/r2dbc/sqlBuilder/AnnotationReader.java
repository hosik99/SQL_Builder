package com.icetea.monstu_back.r2dbc.sqlBuilder;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

// with R2DBC, Lombok
@Component
public class AnnotationReader {

    // 클래스명 -> 컬럼명 맵
   /**
    < Table Name :
        <
            "table" : @Table
            "$(field Name) : @Column"...
        >
    * */
    private static final Map<String, Map<String, String>> entityColumnMapping = new HashMap<>();

    // 초기화 시점에 @Table과 @Column 정보를 읽어 맵에 저장
    public static void init() {
        List<Class<?>> entityClasses = getEntityClasses();
        for (Class<?> entityClass : entityClasses) {
            processEntity(entityClass);
        }

        //Log
        for (Map.Entry<String, Map<String, String>> entry : entityColumnMapping.entrySet()) {
            String tableName = entry.getKey();
            Map<String, String> columnMapping = entry.getValue();
            System.out.println(tableName);
            System.out.println(columnMapping);
        }
    }

    // @GenerateKClass 애너테이션이 적용된 클래스들을 반환
    public static List<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections("com.icetea.monstu_back.model", Scanners.TypesAnnotated);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(GenerateKClass.class);
        System.out.println("annotatedClasses: "+annotatedClasses);
        return new ArrayList<>(annotatedClasses);
    }

    // 특정 엔티티 클래스의 @Table과 @Column 정보를 처리하는 메서드
    private static void processEntity(Class<?> entityClass) {
        /** < Table Name : $(other Map Collection) ></>*/
        Map<String, String> columnMapping = new HashMap<>();

        // 테이블 이름 처리
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            columnMapping.put("table", table.value() );
        }

        // 컬럼 이름 처리
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnMapping.put( field.getName() , column.value() );
            }else{
                columnMapping.put( field.getName() , field.getName() );
            }
        }

        // 클래스명 -> 컬럼 맵을 저장
        entityColumnMapping.put(entityClass.getSimpleName(), columnMapping); //getSimpleName() -> 클래스의 이름을 단순히 (패키지 경로 없이) 반환
    }

    // 특정 클래스의 컬럼 맵을 가져오는 메서드
    public static Map<String, Map<String, String>> getColumnMapping() {
        return entityColumnMapping;
    }

}


