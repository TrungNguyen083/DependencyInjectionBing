package org.example.ormframework.repository.factory;

import org.example.ormframework.repository.CrudRepository;
import org.example.ormframework.repository.factory.imp.CrudRepositoryImp;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

public class RepositoryFactory {
    private RepositoryFactory() {}

    public static CrudRepository createRepoImpl(Class repoInterface) throws SQLException, IOException, ClassNotFoundException {
        ParameterizedType type = (ParameterizedType) repoInterface.getGenericInterfaces()[0];
        InvocationHandler handler = new RepositoryInvocationHandler(new CrudRepositoryImp(type));
        return (CrudRepository) Proxy.newProxyInstance(repoInterface.getClassLoader(), new Class[]{repoInterface}, handler);
    }
}
