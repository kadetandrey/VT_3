package by.bsuir.client;

import by.bsuir.client.service.FileService;
import by.bsuir.common.app.entity.StudentFile;
import by.bsuir.common.command_line.CommandLine;
import by.bsuir.common.command_listener.Command;
import by.bsuir.common.command_listener.CommandListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientMain {

    private static final FileService fileService = new FileService();

    public static void main(String[] args) {
        CommandListener commandListener = new CommandListener();
        commandListener.registerCommand(
                Command.builder()
                        .command("getById")
                        .argumentsDescription("<id>")
                        .requiredArgsCount(1)
                        .runnable(
                                arguments -> fileService.getById(Integer.valueOf(arguments[0]))
                                        .subscribe(
                                                value -> CommandLine.println(value + ""),
                                                error -> CommandLine.println(error.getMessage())
                                        )
                        )
                        .description("Get student files by name")
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("getByName")
                        .argumentsDescription("<name>")
                        .requiredArgsCount(1)
                        .runnable(
                                arguments -> fileService.getByName(arguments[0])
                                        .subscribe(
                                                value -> CommandLine.println(value + ""),
                                                error -> CommandLine.println(error.getMessage())
                                        )
                        )
                        .description("Get student files by name")
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("getAll")
                        .runnable(
                                arguments -> fileService.getAll()
                                        .subscribe(
                                                value -> CommandLine.println(
                                                        "[\n\t" + value.stream()
                                                                .map(StudentFile::toString)
                                                                .collect(Collectors.joining(",\n\t"))
                                                                + "\n]"
                                                ),
                                                error -> CommandLine.println(error.getMessage())
                                        )
                        )
                        .description("Get all students files")
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("update")
                        .argumentsDescription("<id> <field_name>=<new_value>[, <field_name>=<new_value>]")
                        .requiredArgsCount(2)
                        .runnable(
                                arguments -> fileService.getById(Integer.valueOf(arguments[0]))
                                        .subscribe(
                                                studentFile -> {
                                                    if(studentFile == null) {
                                                        CommandLine.println("Student file with such is not found.");
                                                        return;
                                                    }
                                                    Map<String, Function<String, Void>> strategies = new HashMap<>();

                                                    strategies.put("name", newValue -> {
                                                        studentFile.setName(newValue);
                                                        return null;
                                                    });
                                                    strategies.put("age", newValue -> {
                                                        studentFile.setAge(Integer.valueOf(newValue));
                                                        return null;
                                                    });
                                                    strategies.put("summary", newValue -> {
                                                        studentFile.setSummary(newValue);
                                                        return null;
                                                    });
                                                    for(int i = 1; i < arguments.length; i++) {
                                                        String expression = arguments[i];
                                                        String field = expression.substring(0, expression.indexOf("="));
                                                        String newValue = expression.substring(expression.indexOf("=") + 1);

                                                        strategies.getOrDefault(
                                                                field,
                                                                ignored -> {
                                                                    throw new RuntimeException("No such field");
                                                                }
                                                        ).apply(newValue);
                                                    }
                                                    fileService.update(studentFile);
                                                },
                                                error -> CommandLine.println(error.getMessage())
                                        )
                        )
                        .description("Get student files by name")
                        .build()
        ); commandListener.registerCommand(
                Command.builder()
                        .command("test")
                        .argumentsDescription("<id> <field_name>=<new_value>[, <field_name>=<new_value>]")
                        .requiredArgsCount(2)
                        .runnable(
                                arguments -> CommandLine.println(Arrays.toString(arguments))
                        )
                        .description("Get student files by name")
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("save")
                        .argumentsDescription("<name> <age> <summary>")
                        .requiredArgsCount(3)
                        .runnable(
                                arguments -> fileService.save(
                                        StudentFile.builder()
                                                .name(arguments[0])
                                                .age(Integer.parseInt(arguments[1]))
                                                .summary(arguments[2])
                                                .build()
                                )
                        )
                        .description("Get student files by name")
                        .build()
        );

        commandListener.registerCommand(
                Command.builder()
                        .command("exit")
                        .runnable(arguments -> commandListener.close())
                        .description("Exit")
                        .build()
        );

        commandListener.startSync();
    }
}
