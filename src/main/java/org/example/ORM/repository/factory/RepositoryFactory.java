package org.example.ORM.repository.factory;

import org.example.ORM.repository.CrudRepository;
import org.example.ORM.repository.factory.imp.CrudRepositoryImp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

public class RepositoryFactory {
    public RepositoryFactory() {
    }

    public static CrudRepository createRepoImpl(Class<?> repoInterface) {
        ParameterizedType type = (ParameterizedType) repoInterface.getGenericInterfaces()[0];
        InvocationHandler handler = new RepositoryInvocationHandler(new CrudRepositoryImp(type));
        return (CrudRepository) Proxy.newProxyInstance(repoInterface.getClassLoader(), new Class[]{repoInterface}, handler);
    }
}
