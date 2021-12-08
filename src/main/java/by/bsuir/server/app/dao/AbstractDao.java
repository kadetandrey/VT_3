package by.bsuir.server.app.dao;


import by.bsuir.server.app.dao.iface.IDao;
import by.bsuir.server.app.dao.query.Query;
import by.bsuir.server.app.dao.session.Session;
import by.bsuir.server.app.dao.session.exception.SessionCommittedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public abstract class AbstractDao<V> implements IDao<V> {

    private final File source;

    public AbstractDao(String path) {
        source = new File(path);
    }

    public Session<V> getSession() throws FileNotFoundException {
        return new Session<>(source);
    }

    @Override
    public List<V> get(Query<V> criteria) {
        try {
            return getSession()
                    .specifyQuery(criteria)
                    .commit()
                    .getResult();
        } catch (SessionCommittedException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(V value) {
        try {
            getSession().save(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(V value) {
        try {
            getSession().update(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
