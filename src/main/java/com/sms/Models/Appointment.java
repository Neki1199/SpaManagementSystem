package com.sms.Models;

public class Appointment {
    private Integer appointmentId;
    private int clientId;
    private int serviceId;
    private int staffId;
    private String date;
    private String hour;
    private String status;

    public Appointment(Integer appointmentId, int clientId, int serviceId, int staffId, String date, String hour, String status) {
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.staffId = staffId;
        this.date = date;
        this.hour = hour;
        this.status = status;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", clientId=" + clientId +
                ", serviceId=" + serviceId +
                ", staffId=" + staffId +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
