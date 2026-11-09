package com.clinic;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.DoctorDao;
import com.clinic.exception.ValidationException;
import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;
import com.clinic.model.Doctor;
import com.clinic.service.AppointmentService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Service Layer Unit & Business Rule Testing.
 */
public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private MockAppointmentDao mockAppointmentDao;
    private MockDoctorDao mockDoctorDao;

    @Before
    public void setUp() {
        mockAppointmentDao = new MockAppointmentDao();
        mockDoctorDao = new MockDoctorDao();
        appointmentService = new AppointmentService(mockAppointmentDao, mockDoctorDao);
    }

    @Test
    public void testSuccessfulAppointmentBooking() throws Exception {
        String futureDate = LocalDate.now().plusDays(3).toString();
        Appointment apt = appointmentService.bookAppointment(1, 1, futureDate, "10:00", "Chest discomfort check");

        assertNotNull(apt);
        assertEquals(AppointmentStatus.CONFIRMED, apt.getStatus());
        assertTrue(apt.getAppointmentNumber().startsWith("APT-"));
    }

    @Test(expected = ValidationException.class)
    public void testPastDateBookingRejection() throws Exception {
        String pastDate = "2020-01-01";
        appointmentService.bookAppointment(1, 1, pastDate, "10:00", "Past checkup");
    }

    @Test(expected = ValidationException.class)
    public void testSlotConflictRejection() throws Exception {
        mockAppointmentDao.slotTaken = true;
        String futureDate = LocalDate.now().plusDays(2).toString();
        appointmentService.bookAppointment(1, 1, futureDate, "10:00", "Conflict test");
    }

    // Mock DAO implementations for isolated, fast unit testing without live MySQL instance
    private static class MockDoctorDao extends com.clinic.dao.DoctorDaoImpl {
        @Override
        public Doctor findById(int doctorId) {
            Doctor doc = new Doctor();
            doc.setDoctorId(1);
            doc.setFirstName("Marcus");
            doc.setLastName("Smith");
            doc.setAvailable(true);
            return doc;
        }
    }

    private static class MockAppointmentDao extends com.clinic.dao.AppointmentDaoImpl {
        boolean slotTaken = false;

        @Override
        public boolean isSlotAvailable(int doctorId, Date date, Time startTime) {
            return !slotTaken;
        }

        @Override
        public int create(Appointment apt) {
            return 999;
        }
    }
}
