package org.easyarch.myutils.orm.annotation.sql;

import org.easyarch.myutils.orm.type.JDBCType;

import java.lang.annotation.*;

/**
 * Description :
 * Created by code4j on 16-12-26
 * 下午4:30
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlParam {
    String name ();
    JDBCType type ();

}