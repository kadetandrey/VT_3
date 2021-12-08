package by.bsuir.client.service;

import by.bsuir.client.observable.Observable;
import by.bsuir.common.app.entity.StudentFile;
import by.bsuir.common.http.entity.HttpRequest;
import by.bsuir.client.config.Config;
import by.bsuir.client.http.HttpClient;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class FileService implements IFileService {

    private final HttpClient http = new HttpClient(Config.serverUrl, Config.port);

    @Override
    public Observable<StudentFile> getById(Integer id) {
        return http.send(
                HttpRequest.get().addParam("id", id + ""),
                StudentFile.class
        );
    }

    @Override
    public Observable<List<StudentFile>> getByName(String name) {
        return http.send(
                HttpRequest.get()
                        .url("/getByName")
                        .addParam("name", name),
                new TypeToken<List<StudentFile>>(){}.getType()
        );
    }

    @Override
    public Observable<List<StudentFile>> getAll() {
        return http.send(
                HttpRequest.get().url("/getAll"),
                new TypeToken<List<StudentFile>>(){}.getType()
        );
    }

    @Override
    public void update(StudentFile studentFile) {
        http.send(
                HttpRequest.post().body(studentFile)
        );
    }

    @Override
    public void save(StudentFile studentFile) {
        http.send(
                HttpRequest.put().body(studentFile)
        );
    }
}
