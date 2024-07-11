package com.sms.BackEnd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private int id;
    private String name;
    private int phone;
    private String notes;
    private String email;

    public Client(int id, String name, int phone, String notes, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.notes = notes;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
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
