package by.bsuir.common.http.entity.specification.exception.request;

import by.bsuir.common.http.entity.specification.HttpResponseCode;

public class HttpVersionNotSupported extends HttpRequestException {

    public HttpVersionNotSupported(String message) {
        super(message);
    }

    @Override
    public HttpResponseCode responseCode() {
        return HttpResponseCode.HTTP_VERSION_NOT_SUPPORTED;
    }
}
