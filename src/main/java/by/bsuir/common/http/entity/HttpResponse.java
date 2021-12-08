package by.bsuir.common.http.entity;

import by.bsuir.common.http.entity.specification.HttpResponseCode;
import by.bsuir.common.http.entity.specification.HttpVersion;
import by.bsuir.common.http.entity.specification.exception.HttpException;
import by.bsuir.common.http.entity.specification.exception.request.HttpVersionNotSupported;
import by.bsuir.common.http.entity.specification.exception.response.BadResponse;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class HttpResponse extends HttpEntity {

    private int code;
    private String message;

    public static HttpResponse ok() {
        return create().code(HttpResponseCode.OK);
    }

    public static HttpResponse create() {
        return new HttpResponse();
    }

    public static HttpResponse nullBody() {
        return ok().body(null);
    }

    private HttpResponse() {
        super();
        code(HttpResponseCode.OK);
    }

    public HttpResponse(String template) throws HttpException {
        super(template);
    }

    @Override
    protected void scanFirstLine(String firstLine) throws BadResponse, HttpVersionNotSupported {
        String[] tokens = firstLine.split(" +", 3);

        if (tokens.length != 3)
            throw new BadResponse("First line is incorrect.");

        version = checkVersion(tokens[0]);
        code = Integer.parseInt(tokens[1]);
        message = tokens[2];
    }

    private HttpVersion checkVersion(String token) throws HttpVersionNotSupported {
        HttpVersion version = Arrays.stream(HttpVersion.values())
                .filter(httpVersion -> httpVersion.getLabel().equals(token))
                .findFirst().orElse(null);
        if (version != null)
            return version;

        throw new HttpVersionNotSupported("Such version not supported: " + token);
    }

    public HttpResponse code(int code) {
        this.code = code;
        return this;
    }

    public HttpResponse code(HttpResponseCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        return this;
    }

    public HttpResponse message(String message) {
        this.message = message;
        return this;
    }

    public HttpResponse addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpResponse body(Object body) {
        this.body = mapper.toJson(body);
        return this;
    }

    @Override
    public String toString() {
        return (version.getLabel() + " "
                + code + " "
                + message + "\n"
                + super.toString()).trim();
    }
}