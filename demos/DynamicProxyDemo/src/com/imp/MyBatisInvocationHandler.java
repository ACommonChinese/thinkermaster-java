package com.imp;

import com.domain.Mapper;
import com.domain.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBatisInvocationHandler implements InvocationHandler {

    static Map<String, Mapper> mapper = new HashMap<>();

    // mybatis会读取xml配置文件并写入mapper中
    static {
        Mapper map = new Mapper();
        map.setQueryString("select * from user");
        map.setResultType("com.domain.User");
        mapper.put("com.dao.IUserDao.findAll", map);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("触发了invoke方法");
        // 1. 获取方法名
        String methodName = method.getName();
        // 2. 获取方法所在类的名称
        String className = method.getDeclaringClass().getName();
        // 3. 组合key
        String key = className + "." + methodName;
        if (mapper.containsKey(key)) {
            Mapper m = mapper.get(key);
            System.out.println("==== 执行SQL语句 ====");
            System.out.println("返回值：" + m.getResultType());
            System.out.println("sql语句：" + m.getQueryString());
            User user1 = new User();
            user1.setId(1001);
            user1.setUsername("大刘");
            user1.setAddress("中国北京");
            User user2 = new User();
            user1.setId(1002);
            user1.setUsername("张三丰");
            user1.setAddress("中国河南");
            ArrayList<User> arrayList = new ArrayList<>();
            arrayList.add(user1);
            arrayList.add(user2);
            return arrayList;
        }
        return null;
    }
}