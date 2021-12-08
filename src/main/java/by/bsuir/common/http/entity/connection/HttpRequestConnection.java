package by.bsuir.common.http.entity.connection;

import by.bsuir.common.connection.Connection;
import by.bsuir.common.connection.IConnection;
import by.bsuir.common.http.entity.HttpResponse;
import by.bsuir.common.http.entity.HttpRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class HttpRequestConnection implements IConnection<HttpResponse, HttpRequest> {

    private final Connection connection;

    @Override
    public void write(byte[] writable) throws IOException {
        connection.write(writable);
    }

    @Override
    public HttpRequest readAll() throws IOException {
        return new HttpRequest(connection.readAll());
    }

    @Override
    public void write(HttpResponse writable) throws IOException {
        connection.write(writable.toString());
    }

    @Override
    public void shutdownOutput() throws IOException {
        connection.shutdownOutput();
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }
}
