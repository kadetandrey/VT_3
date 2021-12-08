package by.bsuir.server.app.dao.student_file;

import by.bsuir.common.app.entity.StudentFile;

import java.util.List;

public interface IStudentFileService {

    List<StudentFile> getByName(String name);

    List<StudentFile> getAll();

    void update(StudentFile studentFile);

    void save(StudentFile studentFile);

    StudentFile getById(Integer id);
}
