package by.bsuir.common.connection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@RequiredArgsConstructor
public class Connection implements IConnection<String, String> {

    private final Socket connectedSocket;

    public void write(byte[] writable) throws IOException {
        connectedSocket.getOutputStream().write(writable);
        connectedSocket.getOutputStream().flush();
    }

    public String readAll() throws IOException {
        return new String(connectedSocket.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public void write(String writable) throws IOException {
        write(writable.getBytes(StandardCharsets.UTF_8));
    }

    public Socket getConnectedSocket() {
        return connectedSocket;
    }

    @Override
    public void shutdownOutput() throws IOException {
        connectedSocket.shutdownOutput();
    }

    @Override
    public void close() throws IOException {
        connectedSocket.close();
    }
}
