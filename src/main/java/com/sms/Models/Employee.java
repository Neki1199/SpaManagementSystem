package com.sms.Models;

public class Employee {
    private Integer id;
    private String name;
    private String role;
    private String phone;
    private int admin;
    private String username;
    private String password;

    public Employee(Integer id, String name, String role, String phone, int admin, String username, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.admin = admin;
        this.username = username;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", phone=" + phone +
                ", admin=" + admin +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}