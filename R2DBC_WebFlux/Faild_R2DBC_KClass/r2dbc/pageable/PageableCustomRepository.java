package com.icetea.monstu_back.r2dbc.pageable;

import reactor.core.publisher.Flux;

public interface PageableCustomRepository<T> {

    Flux<T> findWithPagination( CustomPageableDTO pageableDTO );

}
