package com.lab3.DBModels;

import java.io.Serializable;

public class SubjectScore implements Serializable {
    private int studentId;
    private int subjectId;
    private int note;

    private String studentName;
    private String subjectName;

    public SubjectScore(int studentId, int subjectId, int note) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.note = note;
    }

    public  SubjectScore(int studentId, int subjectId, int note, String studentName, String subjectName) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.note = note;
        this.studentName = studentName;
        this.subjectName = subjectName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "SubjectScore{" +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", note=" + note +
                '}';
    }
}