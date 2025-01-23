package com.example.board.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // 메소드에 적용되는 어노테이션
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSession {

	
}



