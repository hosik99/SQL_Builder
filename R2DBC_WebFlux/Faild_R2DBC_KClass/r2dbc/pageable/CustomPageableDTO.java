package com.icetea.monstu_back.r2dbc.pageable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/*
    기존의 Pageable의 객체와 비슷한 DTO입니다.
    추가적으로 정렬, 필터 값을 저장해서 사용할 수 있습니다.
*/
@Getter
@Setter
@ToString
@Builder
public class CustomPageableDTO {

    private int page;
    private int size;

    private String sortValue;
    private String sortDirection;

    private String filterOption;
    private String filterValue;

    private String dateOption;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}
