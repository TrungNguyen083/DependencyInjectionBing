package org.example.ormframework.repository.factory;

import org.example.ormframework.repository.factory.imp.CrudRepositoryImp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RepositoryInvocationHandler implements InvocationHandler {
    private CrudRepositoryImp crudRepositoryImpl;
    public RepositoryInvocationHandler(CrudRepositoryImp _crudRepositoryImpl) {
        crudRepositoryImpl = _crudRepositoryImpl;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(crudRepositoryImpl, args);
        return result;
    }
}
