package com;

import com.dao.IUserDao;
import com.domain.User;
import com.imp.DefaultSqlSession;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        DefaultSqlSession session = new DefaultSqlSession();
        List<User> users = session.getMapper(IUserDao.class).findAll();
        for (User u : users) {
            System.out.println(u);
        }
    }
}
