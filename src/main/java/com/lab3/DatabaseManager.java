package com.lab3;

import com.lab3.DBModels.Student;
import com.lab3.DBModels.Subject;
import com.lab3.DBModels.SubjectScore;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends UnicastRemoteObject implements DatabaseManagerInterface {
    private Connection connection;

    public DatabaseManager() throws RemoteException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "12345678");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = connection;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void addStudent(Student student) throws SQLException, RemoteException {
        String query = "INSERT INTO students (last_name, group_number) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, student.getLastName());
        statement.setInt(2, student.getGroupNumber());
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            student.setId(generatedKeys.getInt(1));
        }
    }

    public Student getStudentById(int id) throws SQLException, RemoteException {
        String query = "SELECT * FROM students WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                int groupNumber = resultSet.getInt("group_number");
                return new Student(lastName, groupNumber, resultSet.getInt("id"));
            } else {
                return null;
            }
        }
    }

    public Student getStudentByName(String name) throws SQLException, RemoteException {
        String query = "SELECT * FROM students WHERE last_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String lastName = resultSet.getString("last_name");
                int groupNumber = resultSet.getInt("group_number");
                int id = resultSet.getInt("id");
                return new Student(name, groupNumber, id);
            } else {
                return null;
            }
        }
    }

    public void addSubject(Subject subject) throws SQLException, RemoteException {
        String query = "INSERT INTO subjects (subject_name) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, subject.getName());
        statement.executeUpdate();

        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            subject.setId(generatedKeys.getInt(1));
        }
    }

    public Subject getSubject(int id) throws SQLException, RemoteException {
        String query = "SELECT * FROM subjects WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String name = resultSet.getString("subject_name");
                return new Subject(name, resultSet.getInt("id"));
            } else {
                return null;
            }
        }
    }

    public Subject getSubjectByName(String name) throws SQLException, RemoteException {
        String query = "SELECT * FROM subjects WHERE subject_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new Subject(name, resultSet.getInt("id"));
            } else {
                return null;
            }
        }
    }

    public void addSubjectScore(SubjectScore score) throws SQLException, RemoteException {
        String query = "INSERT INTO subject_scores (student_id, subject_id, score) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, score.getStudentId());
        statement.setInt(2, score.getSubjectId());
        statement.setInt(3, score.getNote());
        statement.executeUpdate();
    }

    public void deleteStudentByName(String lastName) throws SQLException, RemoteException {
        String query = "SELECT id FROM students WHERE last_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, lastName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int studentId = resultSet.getInt("id");
            String deleteScoresQuery = "DELETE FROM subject_scores WHERE student_id = ?";
            PreparedStatement scoresStatement = connection.prepareStatement(deleteScoresQuery);
            scoresStatement.setInt(1, studentId);
            scoresStatement.executeUpdate();
            String deleteStudentQuery = "DELETE FROM students WHERE id = ?";
            PreparedStatement studentStatement = connection.prepareStatement(deleteStudentQuery);
            studentStatement.setInt(1, studentId);
            studentStatement.executeUpdate();
        }
    }

    public void deleteSubjectByName(String lastName) throws SQLException {
        String query = "SELECT id FROM subjects WHERE subject_name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, lastName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int subjectId = resultSet.getInt("id");
            String deleteScoresQuery = "DELETE FROM subject_scores WHERE subject_id = ?";
            PreparedStatement scoresStatement = connection.prepareStatement(deleteScoresQuery);
            scoresStatement.setInt(1, subjectId);
            scoresStatement.executeUpdate();
            String deleteStudentQuery = "DELETE FROM subjects WHERE id = ?";
            PreparedStatement studentStatement = connection.prepareStatement(deleteStudentQuery);
            studentStatement.setInt(1, subjectId);
            studentStatement.executeUpdate();
        }
    }

    public void deleteScore(String forStudent, String forSubject) throws SQLException, RemoteException {
        Student student = getStudentByName(forStudent);
        Subject subject = getSubjectByName(forSubject);

        String query = "DELETE FROM subject_scores WHERE student_id = ? AND subject_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, student.getId());
        statement.setInt(2, subject.getId());
        statement.executeUpdate();
    }

    public void deleteDebtors() throws SQLException, RemoteException {
        String query = "SELECT * FROM subject_scores WHERE score < 4";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Student student = this.getStudentById(resultSet.getInt("student_id"));
                deleteStudentByName(student.getLastName());
            }
        }
    }

    public List<Student> getAllStudents() throws SQLException, RemoteException {
        String query = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String lastName = resultSet.getString("last_name");
                int groupNumber = resultSet.getInt("group_number");
                students.add(new Student(lastName, groupNumber, id));
            }
        }
        return students;
    }

    public List<Subject> getAllSubjects() throws SQLException, RemoteException {
        String query = "SELECT * FROM subjects";
        List<Subject> subjects = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("subject_name");
                subjects.add(new Subject(name, id));
            }
        }
        return subjects;
    }

    public List<SubjectScore> getAllScores() throws SQLException, RemoteException {
        String query = "SELECT * FROM subject_scores";
        List<SubjectScore> scores = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Student student = this.getStudentById(resultSet.getInt("student_id"));
                Subject subject = this.getSubject(resultSet.getInt("subject_id"));
                int score = resultSet.getInt("score");
                scores.add(
                        new SubjectScore(student.getId(),
                                subject.getId(),
                                score,
                                student.getLastName(),
                                subject.getName()));
            }
        }
        return scores;
    }

    public List<SubjectScore> getDebtorsScores() throws SQLException, RemoteException {
        String query = "SELECT * FROM subject_scores WHERE score < 4";
        List<SubjectScore> scores = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Student student = this.getStudentById(resultSet.getInt("student_id"));
                Subject subject = this.getSubject(resultSet.getInt("subject_id"));
                int score = resultSet.getInt("score");
                scores.add(
                        new SubjectScore(student.getId(),
                                subject.getId(),
                                score,
                                student.getLastName(),
                                subject.getName()));
            }
        }
        return scores;
    }
}


