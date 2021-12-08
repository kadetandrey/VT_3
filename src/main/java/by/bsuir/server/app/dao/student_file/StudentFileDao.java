package by.bsuir.server.app.dao.student_file;


import by.bsuir.server.app.dao.AbstractDao;
import by.bsuir.server.app.dao.iface.IStudentFileDao;
import by.bsuir.common.app.entity.StudentFile;
import by.bsuir.server.app.dao.query.Query;

import java.util.List;
import java.util.Objects;

public class StudentFileDao extends AbstractDao<StudentFile> implements IStudentFileDao {

    public StudentFileDao() {
        super("student_file.db");
    }

    @Override
    public StudentFile getById(Integer id) {
        List<StudentFile> results = get(new Query<>(value -> Objects.equals(id, value.getId()), StudentFile.class));
        return results.size() > 0 ? results.get(0) : null;
    }

    @Override
    public List<StudentFile> getByName(String name) {
        return get(new Query<>(value -> value.getName().equals(name), StudentFile.class));
    }

    @Override
    public List<StudentFile> getAll() {
        return get(new Query<>(value -> true, StudentFile.class));
    }
}
