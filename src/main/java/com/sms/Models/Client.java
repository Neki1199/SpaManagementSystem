package com.sms.Models;

public class Client {
    private Integer id;
    private String name;
    private String phone;
    private String notes;
    private String email;

    public Client(Integer id, String name, String phone, String notes, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.notes = notes;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", notes='" + notes + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
