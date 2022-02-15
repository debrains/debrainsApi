package com.debrains.debrainsApi.common;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityFactory.class)
public @interface WithAuthUser {
    long id() default 1L;
    String email() default "";
    String role() default "USER";
}
