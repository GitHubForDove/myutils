package org.easyarch.myutils.orm.annotation.entity;

import org.easyarch.myutils.orm.jdbc.type.JDBCType;

import java.lang.annotation.*;

/**
 * Description :
 * Created by code4j on 16-12-25
 * 下午10:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Column {
    String name() default "";
    JDBCType type() default JDBCType.VARCHAR;
}
