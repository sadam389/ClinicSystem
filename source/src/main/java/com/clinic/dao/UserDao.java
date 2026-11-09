package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.User;
import java.util.List;

public interface UserDao {
    User findById(int userId) throws DatabaseException;
    User findByEmail(String email) throws DatabaseException;
    int create(User user) throws DatabaseException;
    boolean updatePassword(int userId, String newHash, String salt) throws DatabaseException;
    boolean updateStatus(int userId, String status) throws DatabaseException;
    List<User> findAll() throws DatabaseException;
}
