package com.example.bsuir.services;

import com.example.bsuir.dao.UserDao;
import com.example.bsuir.interfaces.DAO;
import com.example.bsuir.interfaces.Service;
import com.example.bsuir.models.entities.User;

import java.util.List;

public class UserService implements Service<User> {

    private final DAO daoService = new UserDao();

    @Override
    public User findEntity(int id) {
        return (User) daoService.findById(id);
    }

    @Override
    public void saveEntity(User entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(User entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(User entity) {
        daoService.update(entity);
    }

    @Override
    public List<User> findAllEntities() {
        return daoService.findAll();
    }
}
