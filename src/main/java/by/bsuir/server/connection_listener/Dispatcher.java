package by.bsuir.server.connection_listener;

import by.bsuir.common.command_line.CommandLine;
import by.bsuir.common.connection.Connection;
import by.bsuir.common.connection.IConnectionHandler;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class Dispatcher implements Closeable {

    private final Queue<Connection> connections = new ConcurrentLinkedQueue<>();
    private final IConnectionHandler connectionHandler;

    public void dispatch(Socket socket) {
        Thread handlingThread = new Thread(() -> {
            String connectionDescription = "connection with " + socket.getInetAddress() + ".";
            CommandLine.println("Opening new " + connectionDescription);
            try (Connection connection = new Connection(socket)) {
                connections.add(connection);
                CommandLine.println("Handling connection with " + connectionDescription);
                connectionHandler.handle(connection);
            } catch (Exception e) {
                CommandLine.printStackTrace(e);
                CommandLine.println("Exception occurred while handling " + connectionDescription);
            } finally {
                connections.removeIf((connection -> connection.getConnectedSocket().equals(socket)));
            }
        });
        handlingThread.start();
    }

    public void close() throws IOException {
        for (Connection connection : connections) connection.close();
    }
}
