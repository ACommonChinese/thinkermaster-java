package com.imp;

import java.lang.reflect.Proxy;

public class DefaultSqlSession {
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T) Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(), new Class[]{daoInterfaceClass}, new MyBatisInvocationHandler());
    }
}