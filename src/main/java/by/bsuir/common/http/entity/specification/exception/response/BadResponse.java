package by.bsuir.common.http.entity.specification.exception.response;

import by.bsuir.common.http.entity.specification.exception.HttpException;

public class BadResponse extends HttpException {

    public BadResponse(String message) {
        super(message);
    }
}
