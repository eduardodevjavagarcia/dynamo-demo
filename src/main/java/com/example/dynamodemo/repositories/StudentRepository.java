package com.example.dynamodemo.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.example.dynamodemo.domain.Student;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Repository
public class StudentRepository {

    private DynamoDBMapper dynamoDBMapper;

    public String save(Student student) {
        dynamoDBMapper.save(student);
        return student.getStudentId();
    }

    public Student getStudent(String studentId, String lastName) {
        return dynamoDBMapper.load(Student.class, studentId, lastName);
    }

    public void updateStudent(String studentId, String firstName, Student student) {
        dynamoDBMapper.save(student, dynamoDBSaveExpression(studentId, firstName));
    }

    public void deleteStudent(Student student) {
        dynamoDBMapper.delete(student);
    }

    public DynamoDBSaveExpression dynamoDBSaveExpression(String studentId, String firstName) {
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> mapStudentUpdate = new HashMap<>();
        mapStudentUpdate.put("student_id", new ExpectedAttributeValue(new AttributeValue(studentId)).withComparisonOperator(ComparisonOperator.EQ));
        mapStudentUpdate.put("first_name", new ExpectedAttributeValue(new AttributeValue(firstName)).withComparisonOperator(ComparisonOperator.EQ));
        saveExpression.setExpected(mapStudentUpdate);
        return saveExpression;
    }

    public List<Student> getAllStudents() {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(Student.class, dynamoDBScanExpression);
    }
}