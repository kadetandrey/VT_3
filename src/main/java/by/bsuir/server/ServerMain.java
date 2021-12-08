package by.bsuir.server;

import by.bsuir.common.command_listener.Command;
import by.bsuir.common.command_listener.CommandListener;
import by.bsuir.server.connection_listener.Server;

public class ServerMain {

    public static final int port = 8080;

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(port);

        CommandListener commandListener = new CommandListener();
        commandListener.registerCommand(
                Command.builder()
                        .command("start")
                        .description("Start server")
                        .runnable(arguments -> server.start())
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("stop")
                        .description("Stop server")
                        .runnable(arguments -> server.shutdown())
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("exit")
                        .description("Exit")
                        .runnable(arguments -> {
                            server.shutdown();
                            commandListener.close();
                        })
                        .build()
        );

        commandListener.start();
        commandListener.join();
    }
}
