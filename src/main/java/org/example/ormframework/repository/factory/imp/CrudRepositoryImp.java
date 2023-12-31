package org.example.ormframework.repository.factory.imp;

import org.example.ormframework.connection.ConnectionManager;
import org.example.ormframework.pagination.Pagination;
import org.example.ormframework.predicate.QueryPredicateExecutor;
import org.example.ormframework.query.QueryGenerator;
import org.example.ormframework.repository.CrudRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CrudRepositoryImp<T, ID> implements CrudRepository<T, ID>, Pagination<T, ID>, QueryPredicateExecutor<T> {
    private final Class<T> entityClass;
    ConnectionManager connectionManager;
    Connection dbConnection;
    Statement statement;


    public CrudRepositoryImp(ParameterizedType type) throws SQLException, ClassNotFoundException, IOException {
        entityClass = (Class<T>) type.getActualTypeArguments()[0];

        connectionManager = new ConnectionManager();
        dbConnection = connectionManager.getConnection();
        statement = dbConnection.createStatement();
    }

    @Override
    public void save(T entity) throws SQLException, IllegalAccessException {
        String insertQuery = QueryGenerator.insertQuery(entity);
        statement.execute(insertQuery);
    }

    @Override
    public void update(T entity) throws SQLException, IllegalAccessException {
        String updateQuery = QueryGenerator.updateQuery(entity);
        statement.execute(updateQuery);
    }

    @Override
    public void delete(ID id) throws SQLException, IllegalAccessException {
        String deleteQuery = QueryGenerator.deleteQuery(id);
        statement.execute(deleteQuery);
    }

    @Override
    public T findById(ID id) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String selectQuery = QueryGenerator.selectByIdQuery(entityClass, id);
        return getResultSet(statement, selectQuery).get(0);
    }

    @Override
    public List<T> findAll() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String selectQuery = QueryGenerator.selectAllQuery(entityClass);
        statement.executeQuery(selectQuery);
        return getResultSet(statement, selectQuery);
    }

    @Override
    public List<T> find(Predicate<T> predicate) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        var selectAllQuery = QueryGenerator.selectAllQuery(entityClass);
        List<T> list = getResultSet(statement, selectAllQuery);
        List<T> filteredList = new ArrayList<>();
        for (T entity : list) {
            if (predicate.test(entity)) {
                filteredList.add(entity);
            }
        }
        return filteredList;
    }

    private List<T> getResultSet(Statement statement, String selectAllQuery) throws SQLException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        var resultSet = statement.executeQuery(selectAllQuery);

        List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
        fields.forEach(field -> field.setAccessible(true));

        List<T> list = new ArrayList<>();

        while (resultSet.next()) {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.set(entity, resultSet.getObject(field.getName()));
            }
            list.add(entity);
        }
        return list;
    }

    @Override
    public long count(Predicate<T> predicate) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        var list = getResultSet(statement, QueryGenerator.selectAllQuery(entityClass));
        return list.stream().filter(predicate).count();
    }

    @Override
    public List<T> getInPage(int page, int size) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        var selectAllQuery = QueryGenerator.selectAllQuery(entityClass);
        List<T> list = getResultSet(statement, selectAllQuery);
        List<T> filteredList = new ArrayList<>();
        for (int i = (page - 1) * size; i < page * size; i++) {
            filteredList.add(list.get(i));
        }
        return filteredList;
    }

}
