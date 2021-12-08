package by.bsuir.server.app.dao.session.exception;

import java.io.IOException;

public class SessionCommittedException extends IOException {

    public SessionCommittedException(String message) {
        super(message);
    }
}
