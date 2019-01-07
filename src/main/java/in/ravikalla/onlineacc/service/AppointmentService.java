package in.ravikalla.onlineacc.service;

import java.util.List;

import in.ravikalla.onlineacc.domain.Appointment;

public interface AppointmentService {
    
	Appointment createAppointment(Appointment appointment);

    List<Appointment> findAll();

    Appointment findAppointment(Long id);

    void confirmAppointment(Long id);
}
