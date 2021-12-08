package by.bsuir.server.http.end_point;

import by.bsuir.common.http.entity.specification.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestEndPoint {

    HttpMethod method();

    String url();
}
