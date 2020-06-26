package com.mboot.annotation;

import java.lang.annotation.*;

import com.mboot.enums.LogType;

/**
 *
 * @author mboot
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

	String description() default "";

	LogType type() default LogType.OPERATION;
}
