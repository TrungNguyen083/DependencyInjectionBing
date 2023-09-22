package org.example.ormframework.repository.factory;

import org.example.ormframework.repository.factory.imp.CrudRepositoryImp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocationHandler implements InvocationHandler {
    private final CrudRepositoryImp crudRepositoryImpl;
    public RepositoryInvocationHandler(CrudRepositoryImp crudRepositoryImpl) {
        this.crudRepositoryImpl = crudRepositoryImpl;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.crudRepositoryImpl, args);
    }
}
