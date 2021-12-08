package by.bsuir.server.app.dao.iface;

import by.bsuir.common.app.entity.StudentFile;

import java.util.List;

public interface IStudentFileDao extends IDao<StudentFile> {

    List<StudentFile> getByName(String name);

    List<StudentFile> getAll();

    StudentFile getById(Integer id);
}
