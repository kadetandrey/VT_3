package by.bsuir.common.http.entity.specification.exception.request;

import by.bsuir.common.http.entity.specification.HttpResponseCode;
import by.bsuir.common.http.entity.specification.exception.HttpException;

public abstract class HttpRequestException extends HttpException {

    public HttpRequestException(String message) {
        super(message);
    }

    public abstract HttpResponseCode responseCode();
}
