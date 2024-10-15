package com.kutub.sqlitedatabase.model;

public class Student {

    private int id;
    private String name;
    private String email;

    public Student() {
    }

    public Student(int id, String name, String gmail) {
        this.id = id;
        this.name = name;
        this.email = gmail;
    }

    public Student(String gmail, String name) {
        this.email = gmail;
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
