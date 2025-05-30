package com.example.dao.security;

import com.example.dao.GenericDAO;
import com.example.entity.security.User;

public interface UserDAO extends GenericDAO<User> {

    User findByUsername(String username);
}
