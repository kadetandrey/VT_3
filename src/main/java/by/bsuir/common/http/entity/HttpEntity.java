package by.bsuir.common.http.entity;

import by.bsuir.common.http.entity.specification.HttpVersion;
import by.bsuir.common.http.entity.specification.Specification;
import by.bsuir.common.http.entity.specification.exception.HttpException;
import by.bsuir.common.http.entity.specification.exception.request.BadRequest;
import com.google.gson.Gson;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Getter
public abstract class HttpEntity {

    protected Gson mapper = new Gson();
    protected HttpVersion version = HttpVersion.HTTP_1_1;
    protected Map<String, String> headers;
    protected String body;

    protected HttpEntity() {
        headers = new LinkedHashMap<>();
        body = "";
    }

    public HttpEntity(String template) throws HttpException {
        this();
        Scanner scanner = new Scanner(template);
        scanFirstLine(scanner.nextLine());
        headers.putAll(scanHeaders(scanner));
        body = scanBody(scanner);
    }

    private Map<String, String> scanHeaders(Scanner template) throws BadRequest {
        Map<String, String> headers = new LinkedHashMap<>();
        String line;
        while (template.hasNextLine() && !(line = template.nextLine()).isBlank()) {
            int separatorPosition = line.indexOf(Specification.HEADER_KEY_VALUE_SEPARATOR);
            if (separatorPosition == -1)
                throw new BadRequest("No header key value separator (" + Specification.HEADER_KEY_VALUE_SEPARATOR + ")");

            headers.put(
                    line.substring(0, separatorPosition),
                    line.substring(separatorPosition + Specification.HEADER_KEY_VALUE_SEPARATOR.length())
            );
        }
        return headers;
    }

    private String scanBody(Scanner template) {
        StringBuilder bodyBuilder = new StringBuilder(); // bodyBuilder lol
        while (template.hasNextLine()) bodyBuilder.append(template.nextLine());
        return bodyBuilder.toString();
    }

    @Override
    public String toString() {
        return headers.entrySet().stream()
                .map(entry -> entry.getKey() + Specification.HEADER_KEY_VALUE_SEPARATOR + entry.getValue())
                .reduce("", (accumulator, header) -> accumulator + header + "\n")
                + "\n"
                + body;
    }

    protected abstract void scanFirstLine(String firstLine) throws HttpException;

    public <R>R getEntity(Type resultClass) {
        return mapper.fromJson(body, resultClass);
    }
}
