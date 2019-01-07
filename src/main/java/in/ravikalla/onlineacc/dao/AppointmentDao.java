package in.ravikalla.onlineacc.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import in.ravikalla.onlineacc.domain.Appointment;

public interface AppointmentDao extends CrudRepository<Appointment, Long> {

    List<Appointment> findAll();
}
