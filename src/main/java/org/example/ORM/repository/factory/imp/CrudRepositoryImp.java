package org.example.ORM.repository.factory.imp;

import org.example.ORM.connection.ConnectionManager;
import org.example.ORM.pagination.Pagination;
import org.example.ORM.predicate.QueryPredicateExecutor;
import org.example.ORM.query.QueryGenerator;
import org.example.ORM.repository.CrudRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CrudRepositoryImp<T,ID> implements CrudRepository<T, ID>, Pagination<T,ID>, QueryPredicateExecutor<T> {
    private final Class<T> entityClass;
    public Class<?> persistentClass;

    public CrudRepositoryImp() {
        entityClass = null;
    }
    public CrudRepositoryImp(Class<T> _entityClass) {
        entityClass = _entityClass;
        //How can I get the persistentClass from the entityClass at runtime?
        // Your suggestion is not working
    }

    public CrudRepositoryImp(ParameterizedType type) {
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public void save(T entity) throws Exception {
        String insertQuery = QueryGenerator.insertQuery(entity);
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        statement.execute(insertQuery);
    }

    @Override
    public void update(T entity) {
        String updateQuery = QueryGenerator.updateQuery(entity);
        try {
            Connection dbConnection = ConnectionManager.getConnection();
            var statement = dbConnection.createStatement();
            statement.execute(updateQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(ID id) {
        String deleteQuery = QueryGenerator.deleteQuery(id);
        try {
            Connection dbConnection = ConnectionManager.getConnection();
            var statement = dbConnection.createStatement();
            statement.execute(deleteQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public T findById(ID id) throws Exception {
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        String selectQuery = QueryGenerator.selectByIdQuery(entityClass, id);
        return getResultSet(statement, selectQuery).get(0);
    }

    @Override
    public List<T> findAll() throws Exception {
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        String selectQuery = QueryGenerator.selectAllQuery(entityClass);
        var resultSet = statement.executeQuery(selectQuery);
        return getResultSet(statement, selectQuery);
    }

    @Override
    public List<T> find(Predicate<T> predicate) throws Exception {
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        var selectAllQuery = QueryGenerator.selectAllQuery(entityClass);
        List<T> list = getResultSet(statement, selectAllQuery);
        List<T> filteredList = new ArrayList<>();
        for(T entity: list) {
            if(predicate.test(entity)) {
                filteredList.add(entity);
            }
        }
        return filteredList;
    }

    private List<T> getResultSet(Statement statement, String selectAllQuery) throws SQLException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        var resultSet = statement.executeQuery(selectAllQuery);

        List<Field> fields = Arrays.asList(entityClass.getDeclaredFields());
        for(Field field: fields) {
            field.setAccessible(true);
        }

        List<T> list = new ArrayList<>();

        while (resultSet.next()) {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for(Field field: fields) {
                field.set(entity, resultSet.getObject(field.getName()));
            }
            list.add(entity);
        }
        return list;
    }

    @Override
    public long count(Predicate<T> predicate) throws Exception {
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        var list = getResultSet(statement, QueryGenerator.selectAllQuery(entityClass));
        return list.stream().filter(predicate).count();
    }

    @Override
    public List<T> getInPage(int page, int size) throws Exception {
        Connection dbConnection = ConnectionManager.getConnection();
        var statement = dbConnection.createStatement();
        var selectAllQuery = QueryGenerator.selectAllQuery(entityClass);
        List<T> list = getResultSet(statement, selectAllQuery);
        List<T> filteredList = new ArrayList<>();
        for(int i = (page - 1) * size; i < page * size; i++) {
            filteredList.add(list.get(i));
        }
        return filteredList;
    }

}
