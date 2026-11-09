package com.clinic.model;

import java.io.Serializable;
import java.sql.Time;

/**
 * Doctor work schedule slot.
 */
public class DoctorSchedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private int scheduleId;
    private int doctorId;
    private String dayOfWeek;
    private Time startTime;
    private Time endTime;
    private int slotDurationMinutes;
    private boolean active;

    public DoctorSchedule() {
        this.slotDurationMinutes = 30;
        this.active = true;
    }

    public DoctorSchedule(int scheduleId, int doctorId, String dayOfWeek, Time startTime, Time endTime, int slotDurationMinutes, boolean active) {
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
        this.active = active;
    }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public int getSlotDurationMinutes() { return slotDurationMinutes; }
    public void setSlotDurationMinutes(int slotDurationMinutes) { this.slotDurationMinutes = slotDurationMinutes; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
