package com.example.bsuir.services;

import com.example.bsuir.dao.EmployeeServiceDao;
import com.example.bsuir.interfaces.DAO;
import com.example.bsuir.interfaces.Service;

import java.util.List;

public class EmployeeServService implements Service<com.example.bsuir.models.entities.EmployeeService> {

    private final DAO daoService = new EmployeeServiceDao();

    @Override
    public com.example.bsuir.models.entities.EmployeeService findEntity(int id) {
        return (com.example.bsuir.models.entities.EmployeeService) daoService.findById(id);
    }

    @Override
    public void saveEntity(com.example.bsuir.models.entities.EmployeeService entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(com.example.bsuir.models.entities.EmployeeService entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(com.example.bsuir.models.entities.EmployeeService entity) {
        daoService.update(entity);
    }

    @Override
    public List<com.example.bsuir.models.entities.EmployeeService> findAllEntities() {
        return daoService.findAll();
    }
}
