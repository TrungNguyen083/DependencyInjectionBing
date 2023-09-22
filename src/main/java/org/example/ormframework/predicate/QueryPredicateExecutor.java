package org.example.ormframework.predicate;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public interface QueryPredicateExecutor<T> {
    List<T> find(Predicate<T> predicate) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
    long count(Predicate<T> predicate) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
}
