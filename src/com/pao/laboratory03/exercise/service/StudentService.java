package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja!");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți înregistrați.");
            return;
        }
        for (Student s : students) {
            System.out.println(s + " | Note: " + s.getGrades());
        }
    }

    public void printTopStudents() {
        List<Student> sortedStudents = new ArrayList<>(students);
        sortedStudents.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        System.out.println("=== TOP STUDENȚI ===");
        for (Student s : sortedStudents) {
            System.out.println(s);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sumMap = new HashMap<>();
        Map<Subject, Integer> countMap = new HashMap<>();

        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                Subject subj = entry.getKey();
                Double grade = entry.getValue();
                sumMap.put(subj, sumMap.getOrDefault(subj, 0.0) + grade);
                countMap.put(subj, countMap.getOrDefault(subj, 0) + 1);
            }
        }

        Map<Subject, Double> averageMap = new HashMap<>();
        for (Subject subj : sumMap.keySet()) {
            averageMap.put(subj, sumMap.get(subj) / countMap.get(subj));
        }
        return averageMap;
    }
}