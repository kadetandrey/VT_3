package by.bsuir.server.app.service;

import by.bsuir.server.app.dao.iface.IStudentFileDao;
import by.bsuir.common.app.entity.StudentFile;
import by.bsuir.server.app.ServiceLocator;
import by.bsuir.server.app.dao.student_file.IStudentFileService;

import java.util.List;

public class StudentFileService implements IStudentFileService {

    private final IStudentFileDao studentFileDao = ServiceLocator.locate(IStudentFileDao.class);

    @Override
    public List<StudentFile> getByName(String name) {
        return studentFileDao.getByName(name);
    }

    @Override
    public List<StudentFile> getAll() {
        return studentFileDao.getAll();
    }

    @Override
    public StudentFile getById(Integer id) {
        return studentFileDao.getById(id);
    }

    @Override
    public void update(StudentFile studentFile) {
        studentFileDao.update(studentFile);
    }

    @Override
    public void save(StudentFile studentFile) {
        studentFileDao.save(studentFile);
    }
}
