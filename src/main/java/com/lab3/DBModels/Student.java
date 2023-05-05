package com.lab3.DBModels;

import java.io.Serializable;

public class Student implements Serializable {
    private int id = 0;
    private String lastName;
    private int groupNumber;

    public Student(String lastName, int groupNumber) {
        this.lastName = lastName;
        this.groupNumber = groupNumber;
    }

    public Student(String lastName, int groupNumber, int id) {
        this.lastName = lastName;
        this.groupNumber = groupNumber;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getLastName() {
        return lastName;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + lastName + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                '}';
    }
}
