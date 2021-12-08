package by.bsuir.server.connection_listener;

import by.bsuir.server.http.HttpConnectionHandler;
import by.bsuir.common.command_line.CommandLine;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;

@RequiredArgsConstructor
public class Server extends Thread {

    private final int port;

    @Override
    public void run() {
        CommandLine.println("Starting to listen port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port); Dispatcher dispatcher = new Dispatcher(new HttpConnectionHandler())) {
            CommandLine.println("Listening...");
            while(!isInterrupted()) {
                dispatcher.dispatch(serverSocket.accept());
            }
        } catch (IOException e) {
            shutdown();
            CommandLine.println("Port " + port + " is already listening. Free it and restart server.");
        }
    }

    public void shutdown() {
        interrupt();
    }
}
