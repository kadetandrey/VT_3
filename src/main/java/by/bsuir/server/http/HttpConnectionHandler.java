package by.bsuir.server.http;

import by.bsuir.common.http.entity.HttpResponse;
import by.bsuir.common.http.entity.connection.HttpRequestConnection;
import by.bsuir.common.http.entity.specification.HttpResponseCode;
import by.bsuir.common.command_line.CommandLine;
import by.bsuir.common.http.entity.specification.exception.request.HttpRequestException;
import by.bsuir.common.connection.Connection;
import by.bsuir.common.connection.IConnectionHandler;

public class HttpConnectionHandler implements IConnectionHandler {

    private final HttpRequestDispatcher httpRequestDispatcher = new HttpRequestDispatcher();

    @Override
    public void handle(Connection connection) throws Exception {
        HttpRequestConnection httpRequestConnection = new HttpRequestConnection(connection);
        try {
            httpRequestConnection.write(
                    httpRequestDispatcher.dispatch(
                            httpRequestConnection.readAll()
                    )
            );
        } catch (HttpRequestException e) {
            httpRequestConnection.write(HttpResponse.create().code(e.responseCode()));
            CommandLine.println("Error during request.");
            CommandLine.printStackTrace(e);
        } catch (Exception e) {
            httpRequestConnection.write(HttpResponse.create().code(HttpResponseCode.INTERNAL_SERVER_ERROR));
            throw e;
        }
        httpRequestConnection.shutdownOutput();
    }
}
