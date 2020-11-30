package com.dao;

import com.domain.User;
import java.util.List;

public interface IUserDao {
    List<User> findAll();
}
