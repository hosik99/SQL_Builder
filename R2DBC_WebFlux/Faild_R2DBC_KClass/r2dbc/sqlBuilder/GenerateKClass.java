package com.icetea.monstu_back.r2dbc.sqlBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스, 인터페이스에 적용
@Retention(RetentionPolicy.RUNTIME) // 타임에만 유지  SOURCE   RUNTIME
public @interface GenerateKClass {

}
