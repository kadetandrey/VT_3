package by.bsuir.common.http.entity.specification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpResponseCode {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad request"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    NOT_IMPLEMENTED(501, "Not implemented"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP version is not supported");

    private final int code;
    private final String message;
}
