package by.bsuir.common.command_listener;

import by.bsuir.common.command_listener.exception.CannotRegisterCommandException;
import by.bsuir.common.command_line.CommandLine;

import java.io.Closeable;
import java.util.*;

public class CommandListener extends Thread implements Closeable {

    private static final Command NOT_FOUND_COMMAND = Command.builder()
            .runnable(args -> CommandLine.println("Command not found."))
            .build();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void startSync() {
        run();
    }

    @Override
    public void run() {
        CommandLine.println(buildCommandList() + "************************************");
        CommandLine.setInputMessage(">>> Print command >>> ");
        while (!isInterrupted()) {
            String line = CommandLine.readLine().trim();
            String[] tokens = parseTokens(line);
            if (tokens.length == 0) continue;
            String cmd = tokens[0];
            String[] args = tokens.length == 1 ? new String[]{}
                    : Arrays.copyOfRange(tokens, 1, tokens.length);

            try {
                commands.getOrDefault(cmd, NOT_FOUND_COMMAND).run(args);
            } catch (Exception e) {
                CommandLine.printStackTrace(e);
            }
        }
    }

    private String[] parseTokens(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        boolean isInString = false;
        boolean tokenStarted = false;
        boolean isShielded = false;
        for (char c : line.toCharArray()) {
            if (isShielded) {
                isShielded = false;
                token.append(c);
                continue;
            }
            if (c == '\\') isShielded = true;
            if (c == '"') {
                if (isInString) {
                    isInString = false;
                    tokenStarted = false;
                    tokens.add(token.toString());
                    token = new StringBuilder();
                } else {
                    isInString = true;
                    tokenStarted = true;
                }
                continue;
            }
            if (!isInString) {
                if ((c + "").isBlank()) {
                    if (tokenStarted) {
                        tokenStarted = false;
                        tokens.add(token.toString());
                        token = new StringBuilder();
                    }
                    continue;
                } else {
                    tokenStarted = true;
                }
            }
            token.append(c);
        }
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        return tokens.toArray(new String[0]);
    }

    public void registerCommand(Command cmd) {
        if (isAlive()) throw new CannotRegisterCommandException("Listener is already launched.");
        commands.put(cmd.getCommand(), cmd);
    }

    private String buildCommandList() {
        return commands.values().stream()
                .map(command -> String.format("\t- %s\n", command))
                .reduce("Commands:\n", (accumulator, command) -> accumulator + command);
    }

    @Override
    public void close() {
        interrupt();
    }
}
