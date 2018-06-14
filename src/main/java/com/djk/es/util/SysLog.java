package com.djk.es.util;

import java.lang.annotation.*;

/**
 * Created by dujinkai on 2018/6/14.
 * 日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "操作日志";
}
