package com.example.bsuir.services;

import com.example.bsuir.dao.ServiceDao;
import com.example.bsuir.interfaces.DAO;
import com.example.bsuir.interfaces.Service;

import java.util.List;

public class ServiceService implements Service<com.example.bsuir.models.entities.Service> {

    private final DAO daoService = new ServiceDao();

    @Override
    public com.example.bsuir.models.entities.Service findEntity(int id) {
        return (com.example.bsuir.models.entities.Service) daoService.findById(id);
    }

    @Override
    public void saveEntity(com.example.bsuir.models.entities.Service entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(com.example.bsuir.models.entities.Service entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(com.example.bsuir.models.entities.Service entity) {
        daoService.update(entity);
    }

    @Override
    public List<com.example.bsuir.models.entities.Service> findAllEntities() {
        return daoService.findAll();
    }
}
