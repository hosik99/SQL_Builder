@Override
    public Flux<PrePostsDTO> findWithPagination(CustomPageableDTO pageableDTO) {
        KPosts kPosts = new KPosts().withNick("p");
        KMembers kMembers = new KMembers().withNick("m");
        KPostCategory kPostCategory = new KPostCategory().withNick("c");

        String sql  = sqlBuilder
                .select( "p.id", "m.name", "p.title", "p.thumbnail_url","p.created_at","p.updated_at","c.category", "p.state", "p.is_public" )
                .from("posts").as("p")
                .join("post_category").as("c")
                .on("p.category_id").eq("c.id")
                .join("members").as("m")
                .on("p.author_id").eq("m.id")
                .orderBy( postsMng.createSortDefault(pageableDTO),"p")
                .limit(pageableDTO.getSize())
                .offset(pageableDTO.getPage()*pageableDTO.getSize())
                .build();

        return databaseClient.sql(sql)
                .map(row -> PrePostsDTO.builder()
                        .id(row.get("p_id", Long.class))
                        .author(row.get("m_name", String.class))
                        .title(row.get("p_title", String.class))
                        .thumbnailUrl(row.get("p_thumbnail_url", String.class))
                        .createdAt(row.get("p_created_at", LocalDateTime.class))
                        .updatedAt(row.get("p_updated_at", LocalDateTime.class))
                        .category(row.get("c_category", String.class))
                        .state(State.valueOf(row.get("p_state", String.class)))
                        .isPublic(row.get("p_is_public", Boolean.class))
                        .build() )
                .all(); // 모든 결과를 비동기적으로 가져오며 return Flux
    }