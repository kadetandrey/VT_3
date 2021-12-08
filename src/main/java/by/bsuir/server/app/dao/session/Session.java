package by.bsuir.server.app.dao.session;

import by.bsuir.server.app.dao.session.exception.SessionCommittedException;
import by.bsuir.server.app.dao.query.Query;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Session<V> {

    private final File source;

    private final Gson gson;

    private Scanner scanner;

    private Query<V> query;

    private List<V> result;

    private boolean isCommitted;

    public Session(File source) {
        this.source = source;
        this.isCommitted = false;
        this.gson = new Gson();
    }

    public void update(V updated) throws IOException {
        if (isCommitted)
            throw new SessionCommittedException("Unable to write: session is committed");
        validate(updated);

        Path temp = Files.createTempFile(source.getName(), "db");
        Files.copy(Path.of(source.getAbsolutePath()), new FileOutputStream(temp.toFile()));
        Files.write(source.toPath(), new byte[0]);

        Scanner tempScanner = new Scanner(temp.toFile());
        BufferedWriter bw = new BufferedWriter(new FileWriter(source));

        while (tempScanner.hasNextLine()) {
            String json = tempScanner.nextLine();
            Object value = gson.fromJson(json, updated.getClass());
            validate(value);

            if (Objects.equals(getId(updated), getId(value))) {
                bw.write(gson.toJson(updated) + "\n");
            } else {
                bw.write(gson.toJson(value) + "\n");
            }
        }
        bw.close();
        tempScanner.close();
    }

    private Integer getId(Object value) {
        try {
            return (Integer) value.getClass().getDeclaredField("id").get(value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    public void save(V value) throws IOException {
        if (isCommitted)
            throw new SessionCommittedException("Unable to write: session is committed");
        validate(value);

        try {
            generateId(source, value);
            Files.write(
                    Path.of(source.getAbsolutePath()),
                    (gson.toJson(value) + "\n").getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND
            );
        } catch (FileNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void generateId(File source, V value) throws FileNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        value.getClass().getDeclaredMethod("setId", Integer.class).invoke(value, getLastId(source) + 1);
    }

    @SuppressWarnings("unchecked")
    private Integer getLastId(File source) throws FileNotFoundException {
        Scanner idScanner = new Scanner(source);
        int max = 0;
        while (idScanner.hasNextLine()) {
            String json = idScanner.nextLine();
            Map<String, Object> value = (Map<String, Object>) gson.fromJson(json, Map.class);
            if (!value.containsKey("id")) throw new IllegalArgumentException("Value must have Id field.");

            double id = (Double) value.get("id");
            if (max == id) throw new RuntimeException("Equal ids was found.");
            if (max < id) max = (int) id;
        }
        return max;
    }

    public Session<V> specifyQuery(Query<V> query) throws FileNotFoundException {
        if (query == null)
            throw new IllegalArgumentException("Query is not specified.");

        this.query = query;
        this.scanner = new Scanner(source);
        return this;
    }

    public Session<V> commit() throws SessionCommittedException {
        if (isCommitted)
            throw new SessionCommittedException("Unable to commit: session is committed");

        result = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String json = scanner.nextLine();
            V value = gson.fromJson(json, query.getTargetClass());
            validate(value);
            if (query.isSuitable(value))
                result.add(value);
        }

        scanner.close();
        isCommitted = true;
        return this;
    }

    private void validate(Object value) {
        try {
            if (!value.getClass().getDeclaredField("id").getType().equals(Integer.class))
                throw new IllegalArgumentException("Value id must be integer.");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Value must have Id field.");
        }
    }

    public List<V> getResult() throws SessionCommittedException {
        if (!isCommitted)
            throw new SessionCommittedException("Unable to get result: session isn't committed");

        return result;
    }
}
