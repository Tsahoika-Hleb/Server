package com.lab3;

import com.lab3.DBModels.Student;
import com.lab3.DBModels.Subject;
import com.lab3.DBModels.SubjectScore;

import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONManager extends UnicastRemoteObject implements DatabaseManagerInterface{

    private static final String studentsJSONFileName = "students.json";
    private static final String subjectsJSONFileName = "subjects.json";
    private static final String resultsJSONFileName = "results.json";

    private JSONArray students = new JSONArray();
    private JSONArray subjects = new JSONArray();
    private JSONArray results = new JSONArray();

    public JSONManager() throws RemoteException {
        JSONParser parser = new JSONParser();

        try {
            Object obj1 = parser.parse(new FileReader(studentsJSONFileName));
            students = (JSONArray) obj1;
            Object obj2 = parser.parse(new FileReader(subjectsJSONFileName));
            subjects = (JSONArray) obj2;
            Object obj3 = parser.parse(new FileReader(resultsJSONFileName));
            results = (JSONArray) obj3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStudent(Student student) throws SQLException, RemoteException {
        JSONObject studentObj = new JSONObject();
        studentObj.put("id", student.getId());
        studentObj.put("last_name", student.getLastName());
        studentObj.put("group_number", student.getGroupNumber());

        students.add(studentObj);

        try (FileWriter file = new FileWriter(studentsJSONFileName)) {
            file.write(students.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentById(int id) throws SQLException, RemoteException {
        Student student = null;
        for (int i = 0; i < students.size(); i++) {
            JSONObject studentObj = (JSONObject) students.get(i);

            String currentId = (String) studentObj.get("id");
            if (id == Integer.parseInt(currentId)) {
                String lastName = (String) studentObj.get("last_name");
                String groupNumber = (String) studentObj.get("group_number");

                student = new Student(lastName, Integer.parseInt(groupNumber), id);
            }
        }
        return student;
    }

    @Override
    public Student getStudentByName(String name) throws SQLException, RemoteException {
        Student student = null;
        for (int i = 0; i < students.size(); i++) {
            JSONObject studentObj = (JSONObject) students.get(i);

            String lastName = (String) studentObj.get("last_name");
            if (Objects.equals(name, lastName)) {
                String id = (String) studentObj.get("id");
                String groupNumber = (String) studentObj.get("group_number");

                student = new Student(name, Integer.parseInt(groupNumber), Integer.parseInt(id));
            }
        }
        return student;
    }

    @Override
    public void addSubject(Subject subject) throws SQLException, RemoteException {
        JSONObject subjectObj = new JSONObject();
        subjectObj.put("id", subject.getId());
        subjectObj.put("subject_name", subject.getName());

        subjects.add(subjectObj);

        try (FileWriter file = new FileWriter(subjectsJSONFileName)) {
            file.write(subjects.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Subject getSubject(int id) throws SQLException, RemoteException {
        Subject subject = null;
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subjectObj = (JSONObject) subjects.get(i);

            String currentId = (String) subjectObj.get("id");
            if (id == Integer.parseInt(currentId)) {
                String subjectName = (String) subjectObj.get("subject_name");

                subject = new Subject(subjectName, id);
            }
        }
        return subject;
    }

    @Override
    public Subject getSubjectByName(String name) throws SQLException, RemoteException {
        Subject subject = null;
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subjectObj = (JSONObject) subjects.get(i);

            String subjectName = (String) subjectObj.get("subject_name");
            if (Objects.equals(name, subjectName)) {
                String currentId = (String) subjectObj.get("id");

                subject = new Subject(subjectName, Integer.parseInt(currentId));
            }
        }
        return subject;
    }

    @Override
    public void addSubjectScore(SubjectScore score) throws SQLException, RemoteException {
        JSONObject scoreObj = new JSONObject();
        scoreObj.put("student_id", score.getStudentId());
        scoreObj.put("subject_id", score.getSubjectId());
        scoreObj.put("score", score.getNote());

        results.add(scoreObj);

        try (FileWriter file = new FileWriter(resultsJSONFileName)) {
            file.write(results.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudentByName(String lastName) throws SQLException, RemoteException { //TODO: delete results
        for (int i = 0; i < students.size(); i++) {
            JSONObject student = (JSONObject) students.get(i);
            String name = (String) student.get("last_name");

            if (name.equals(lastName)) {
                students.remove(i);
                break;
            }
        }
        try (FileWriter file = new FileWriter(studentsJSONFileName)) {
            file.write(students.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSubjectByName(String lastName) throws SQLException, RemoteException { //TODO: delete results
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subj = (JSONObject) subjects.get(i);
            String name = (String) subj.get("subject_name");

            if (name.equals(lastName)) {
                subjects.remove(i);
                break;
            }
        }
        try (FileWriter file = new FileWriter(subjectsJSONFileName)) {
            file.write(subjects.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteScore(String forStudent, String forSubject) throws SQLException, RemoteException {
        Student student = getStudentByName(forStudent);
        Subject subject = getSubjectByName(forSubject);

        for (int i = 0; i < results.size(); i++) {
            JSONObject res = (JSONObject) results.get(i);
            String stId = (String) res.get("student_id");
            String subjId = (String) res.get("subject_id");

            if ((student.getId() == Integer.parseInt(stId)) && (subject.getId() == Integer.parseInt(subjId))) {
                results.remove(i);
                break;
            }
        }
        try (FileWriter file = new FileWriter(resultsJSONFileName)) {
            file.write(results.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteDebtors() throws SQLException, RemoteException { // TODO: delete students
        for (int i = 0; i < results.size(); i++) {
            JSONObject res = (JSONObject) results.get(i);
            String note = (String) res.get("score");

            if (Integer.parseInt(note) < 4) {
                results.remove(i);
            }
        }
        try (FileWriter file = new FileWriter(resultsJSONFileName)) {
            file.write(results.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() throws SQLException, RemoteException {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            JSONObject student = (JSONObject) students.get(i);

            String id = (String) student.get("id");
            String lastName = (String) student.get("last_name");
            String groupNumber = (String) student.get("group_number");

            Student student1 = new Student(lastName, Integer.parseInt(groupNumber), Integer.parseInt(id));
            studentList.add(student1);
        }
        return studentList;
    }

    @Override
    public List<Subject> getAllSubjects() throws SQLException, RemoteException {
        List<Subject> subjectList = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject student = (JSONObject) subjects.get(i);

            String id = (String) student.get("id");
            String name = (String) student.get("subject_name");

            Subject subj = new Subject(name, Integer.parseInt(id));
            subjectList.add(subj);
        }
        return subjectList;
    }

    @Override
    public List<SubjectScore> getAllScores() throws SQLException, RemoteException {
        List<SubjectScore> resultsList = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject result = (JSONObject) results.get(i);

            String studentId = (String) result.get("student_id");
            String subjectId = (String) result.get("subject_id");
            String note = (String) result.get("score");

            SubjectScore res = new SubjectScore(Integer.parseInt(studentId), Integer.parseInt(subjectId), Integer.parseInt(note));
            resultsList.add(res);
        }
        return resultsList;
    }

    @Override
    public List<SubjectScore> getDebtorsScores() throws SQLException, RemoteException {
        List<SubjectScore> resultsList = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject result = (JSONObject) results.get(i);

            String studentId = (String) result.get("student_id");
            String subjectId = (String) result.get("subject_id");
            String note = (String) result.get("score");

            if (Integer.parseInt(note) < 4) {
                SubjectScore res = new SubjectScore(Integer.parseInt(studentId), Integer.parseInt(subjectId), Integer.parseInt(note));
                resultsList.add(res);
            }
        }
        return resultsList;
    }
}
