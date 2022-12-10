package com.example.bsuir.services;

import com.example.bsuir.dao.AppointmentDao;
import com.example.bsuir.interfaces.DAO;
import com.example.bsuir.interfaces.Service;
import com.example.bsuir.models.entities.Appointment;

import java.util.List;

public class AppointmentService implements Service<Appointment> {

    private final DAO daoService = new AppointmentDao();

    @Override
    public Appointment findEntity(int id) {
        return (Appointment) daoService.findById(id);
    }

    @Override
    public void saveEntity(Appointment entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(Appointment entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(Appointment entity) {
        daoService.update(entity);
    }

    @Override
    public List<Appointment> findAllEntities() {
        return daoService.findAll();
    }
}
