package by.bsuir.server.app.dao.query;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class Query<E> {

    private final Function<E, Boolean> criteria;
    private final Class<? extends E> targetClass;

    public Class<? extends E> getTargetClass() {
        return targetClass;
    }

    public boolean isSuitable(E entity) {
        return criteria.apply(entity);
    }
}
