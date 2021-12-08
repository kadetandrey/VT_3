package by.bsuir.server.http.end_point.scanner;

import by.bsuir.common.http.entity.HttpRequest;
import by.bsuir.common.http.entity.HttpResponse;
import by.bsuir.common.utils.KeyValue;
import by.bsuir.server.http.end_point.EndPoint;
import by.bsuir.server.http.end_point.RequestEndPoint;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpEndPointScanner {

    public static Map<String, EndPoint<HttpRequest, HttpResponse>> findIn(Object endPointCluster) {
        return Arrays.stream(endPointCluster.getClass().getDeclaredMethods())
                .filter(method ->
                        Arrays.stream(method.getAnnotations())
                                .anyMatch(
                                        annotation -> annotation.annotationType().equals(RequestEndPoint.class)
                                )
                ).map(method ->
                        new KeyValue<String, EndPoint<HttpRequest, HttpResponse>>(
                                Arrays.stream(method.getAnnotations())
                                        .filter(
                                                annotation -> annotation.annotationType().equals(RequestEndPoint.class)
                                        ).map(annotation -> (RequestEndPoint) annotation)
                                        .findFirst()
                                        .map(annotation -> annotation.method() + annotation.url())
                                        .orElseThrow(),
                                argument -> (HttpResponse) method.invoke(endPointCluster, argument)
                        )
                ).collect(Collectors.toUnmodifiableMap(KeyValue::getKey, KeyValue::getValue));
    }
}
