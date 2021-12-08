package by.bsuir.client.http;

import by.bsuir.client.config.Config;
import by.bsuir.client.observable.Observable;
import by.bsuir.common.http.entity.HttpRequest;
import by.bsuir.common.http.entity.HttpResponse;
import by.bsuir.common.http.entity.connection.HttpResponseConnection;
import by.bsuir.common.http.entity.specification.HttpResponseCode;
import by.bsuir.common.command_line.CommandLine;
import by.bsuir.common.connection.Connection;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;

@RequiredArgsConstructor
public class HttpClient {

    private final String host;
    private final int port;

    public void send(HttpRequest request) {
        new Thread(() -> {
            try (Socket socket = new Socket(Config.serverUrl, Config.port)) {

                Connection connection = new Connection(socket);
                connection.write(request.toString());

            } catch (IOException e) {
                CommandLine.println("Request error: " + e.getMessage());
            } catch (Exception e) {
                CommandLine.printStackTrace(e);
            }
        }).start();
    }

    public <R> Observable<R> send(HttpRequest request, Type resultClass) {
        Observable<R> result = new Observable<>();
        new Thread(() -> {
            try (HttpResponseConnection connection = new HttpResponseConnection(new Connection(new Socket(host, port)))) {

                connection.write(request);
                connection.shutdownOutput();
                HttpResponse response = connection.readAll();

                if (response.getCode() != HttpResponseCode.OK.getCode())
                    result.throwError(new Exception("Response is not OK: " + response.getCode() + " " + response.getMessage()));
                else
                    result.setValue(response.getEntity(resultClass));

            } catch (Exception e) {
                CommandLine.printStackTrace(e);
            }
        }).start();
        return result;
    }
}
