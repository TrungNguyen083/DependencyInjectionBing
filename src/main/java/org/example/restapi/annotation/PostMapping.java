package org.example.restapi.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    @AliasFor(
            annotation = RequestMapping.class
    )
    String name() default "";

    @AliasFor(
            annotation = RequestMapping.class
    )
    String value() default "";

    @AliasFor(
            annotation = RequestMapping.class
    )
    String path() default "";

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] params() default {};
}
