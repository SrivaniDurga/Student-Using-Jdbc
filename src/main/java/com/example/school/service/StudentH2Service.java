package com.example.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.school.repository.StudentRepository;
import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;
@Service
public class StudentH2Service implements StudentRepository{
    
	@Autowired
    private JdbcTemplate db;
    @Override
    public ArrayList<Student> getStudents(){
        List<Student> studentsall = db.query("select * from Student" , new StudentRowMapper());
        ArrayList<Student> allStudents = new ArrayList<>(studentsall);
        return allStudents;
    }
    @Override
    public String addBulkStudents(ArrayList<Student> bulkStudents){
        int len =  bulkStudents.size();
        for(int i = 0; i<len;i++){
            Student student = bulkStudents.get(i);
            String sql = "INSERT INTO Student (studentName ,gender , standard) VALUES (? , ? , ?)";
            db.update(sql ,student.getStudentName() , student.getGender() , student.getStandard());
        }
        String str = "Successfully added "+ len + " students";
        return str;
    }
    @Override
    public Student getStudentById(int studentId){
        try{
            String sql = "select * from Student where studentId = ?";
            Student s = db.queryForObject(sql, new StudentRowMapper(),studentId);
            return s;
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public Student addStudent(Student student){
        // String sql = "INSERT INTO Student (studentName ,gender , standard) VALUES (? , ? , ?)";
        // db.update(sql ,student.getStudentName() , student.getGender() , student.getStandard());
        // String sql1 = "select * from Student where studentName = ? and gender = ? and standard=?";
        // Student std = db.queryForObject("select * from Student where studentName = ? and gender = ? and standard=?", new StudentRowMapper() , student.getStudentName() , student.getGender() , student.getStandard());
        // return std;
        String sqll = "INSERT INTO Student (studentName, gender , standard) VALUES (?, ? , ?)";  
        db.update(sqll, student.getStudentName(), student.getGender() , student.getStandard());
        Student person = db.queryForObject("select * from Student where studentName = ? and gender = ? and standard = ?",new StudentRowMapper(), student.getStudentName(),student.getGender(),student.getStandard());
        return person;
        
    }
    @Override
    public Student updateStudent(int studentId ,Student student){
        if(student.getStudentName() != null){
            db.update("update Student set studentName = ? where studentId = ?",student.getStudentName() , studentId);
        }
        if(student.getGender() != null){
            db.update("update Student set gender = ? where studentId = ?" , student.getGender() , studentId);
        }
        if(student.getStandard() != 0){
            db.update("update Student set standard = ? where studentId = ?" , student.getStandard() , studentId);
        }
        return getStudentById(studentId);
    }
    @Override 
    public void deleteStudent(int studentId){
        db.update("delete from Student where studentId = ?" , studentId);
    }

}