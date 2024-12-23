package com.icetea.monstu_back.repository.custom;

import com.icetea.monstu_back.dto.PrePostsDTO;
import com.icetea.monstu_back.enums.State;
import com.icetea.monstu_back.manager.PostsManager;
import com.icetea.monstu_back.r2dbc.pageable.CustomPageableDTO;
import com.icetea.monstu_back.r2dbc.pageable.PageableCustomRepository;
import com.icetea.monstu_back.r2dbc.sqlBuilder.AnnotationReader;
import com.icetea.monstu_back.r2dbc.sqlBuilder.SqlBuilder;
import com.kclass.generated.KMembers;
import com.kclass.generated.KPostCategory;
import com.kclass.generated.KPosts;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


import java.time.LocalDateTime;


@Repository
public class PostsCustomRepository implements PageableCustomRepository<PrePostsDTO> {

    private final PostsManager postsMng;
    private final DatabaseClient databaseClient;
    private final SqlBuilder sqlBuilder;

    public PostsCustomRepository(PostsManager postsMng, DatabaseClient databaseClient, SqlBuilder sqlBuilder) {
        this.postsMng = postsMng;
        this.databaseClient = databaseClient;
        this.sqlBuilder = sqlBuilder;
    }

    // 정렬검색
    @Override
    public Flux<PrePostsDTO> findWithPagination(CustomPageableDTO pageableDTO) {
        String sql = prePostsSQL(pageableDTO);

        KPosts oriKPosts = new KPosts();
        KMembers oriKMembers = new KMembers();
        KPostCategory oriKPostCategory = new KPostCategory();

        return databaseClient.sql(sql)
                .map(row -> PrePostsDTO.builder()
                        .id(row.get(oriKPosts.id, Long.class))
                        .author(row.get(oriKMembers.name, String.class))
                        .title(row.get(oriKPosts.title, String.class))
                        .thumbnailUrl(row.get(oriKPosts.thumbnailUrl, String.class))
                        .createdAt(row.get(oriKPosts.createdAt, LocalDateTime.class))
                        .updatedAt(row.get(oriKPosts.updatedAt, LocalDateTime.class))
                        .category(row.get(oriKPostCategory.category, String.class))
                        .state(State.valueOf(row.get(oriKPosts.state, String.class)))
                        .isPublic(row.get(oriKPosts.isPublic, Boolean.class))
                        .build() )
                .all(); // 모든 결과를 비동기적으로 가져오며 return Flux
    }

    public String prePostsSQL(CustomPageableDTO pageableDTO){
        KPosts kPosts = new KPosts().withNick("p");
        KMembers kMembers = new KMembers().withNick("m");
        KPostCategory kPostCategory = new KPostCategory().withNick("c");

        String filterOption = pageableDTO.getFilterOption();
        String dateOption = pageableDTO.getDateOption();
        String sortValue = pageableDTO.getSortValue();

        sqlBuilder.select( kPosts.id, kMembers.name, kPosts.title, kPosts.thumbnailUrl,kPosts.createdAt,kPosts.updatedAt,kPostCategory.category, kPosts.state, kPosts.isPublic )
                .from(kPosts.table)
                .join(kPostCategory.table)
                .on(kPosts.category).eq(kPostCategory.id)
                .join(kMembers.table)
                .on(kPosts.authorId).eq(kMembers.id);

        // if(filterOption != null && !filterOption.isEmpty()){
        //     filterOption = switch (filterOption){
        //         case "category" -> kPostCategory.category;
        //         case "author" -> kMembers.name;
        //         default -> kPosts.nick+ "." + toSnakeCase(filterOption);
        //     };
        //     sqlBuilder.where(filterOption).eq(pageableDTO.getFilterValue());
        // }

        // if(dateOption != null && !dateOption.isEmpty()){
        //     dateOption = kPosts.nick+ "." + toSnakeCase(dateOption);
        //     String dateStart = "'" + String.valueOf(pageableDTO.getDateStart()).replace("T", " ") + "'";
        //     String dateEnd = "'" + String.valueOf(pageableDTO.getDateEnd()).replace("T", " ") + "'";
        //     sqlBuilder.where(dateOption).between(dateStart,dateEnd);
        // }

        // sqlBuilder.orderBy( kPosts.nick+ "." + toSnakeCase(sortValue), pageableDTO.getSortDirection())
                .limit(pageableDTO.getSize())
                .offset(pageableDTO.getPage()*pageableDTO.getSize());

        return sqlBuilder.build();
    }

    public String toSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase
                .replaceAll("([a-z])([A-Z])", "$1_$2") // 소문자와 대문자 경계에 "_" 추가
                .toLowerCase(); // 소문자로 변환
    }
}
