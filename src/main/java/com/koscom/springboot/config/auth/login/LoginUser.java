package com.koscom.springboot.config.auth.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // (1) 어노테이션이 선언될 수 있는 위치를 정한다.
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { // (2)
}