package club.c1sec.c1ctfplatform.interceptor;

import club.c1sec.c1ctfplatform.checkers.BasicChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptCheck {
    Class<? extends BasicChecker>[] checkers() default {};
}
