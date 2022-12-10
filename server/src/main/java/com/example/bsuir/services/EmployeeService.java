package com.example.bsuir.services;

import com.example.bsuir.dao.EmployeeDao;
import com.example.bsuir.interfaces.DAO;
import com.example.bsuir.interfaces.Service;
import com.example.bsuir.models.entities.Employee;

import java.util.List;

public class EmployeeService implements Service<Employee> {

    private final DAO daoService = new EmployeeDao();

    @Override
    public Employee findEntity(int id) {
        return (Employee) daoService.findById(id);
    }

    @Override
    public void saveEntity(Employee entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(Employee entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(Employee entity) {
        daoService.update(entity);
    }

    @Override
    public List<Employee> findAllEntities() {
        return daoService.findAll();
    }
}
