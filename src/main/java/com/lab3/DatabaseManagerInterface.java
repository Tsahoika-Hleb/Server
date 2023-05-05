package com.lab3;

import com.lab3.DBModels.Student;
import com.lab3.DBModels.Subject;
import com.lab3.DBModels.SubjectScore;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseManagerInterface extends Remote {
    void addStudent(Student student) throws SQLException, RemoteException;
    Student getStudentById(int id) throws SQLException, RemoteException;
    Student getStudentByName(String name) throws SQLException, RemoteException;
    void addSubject(Subject subject) throws SQLException, RemoteException;
    Subject getSubject(int id) throws SQLException, RemoteException;
    Subject getSubjectByName(String name) throws SQLException, RemoteException;
    void addSubjectScore(SubjectScore score) throws SQLException, RemoteException;
    void deleteStudentByName(String lastName) throws SQLException, RemoteException;
    void deleteSubjectByName(String lastName) throws SQLException, RemoteException;
    void deleteScore(String forStudent, String forSubject) throws SQLException, RemoteException;
    void deleteDebtors() throws SQLException, RemoteException;
    List<Student> getAllStudents() throws SQLException, RemoteException;
    List<Subject> getAllSubjects() throws SQLException, RemoteException;
    List<SubjectScore> getAllScores() throws SQLException, RemoteException;
    List<SubjectScore> getDebtorsScores() throws SQLException, RemoteException;
}
