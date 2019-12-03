package com.example.dynamodemo.services;

import com.example.dynamodemo.domain.Student;
import com.example.dynamodemo.exceptions.NotFoundException;
import com.example.dynamodemo.exceptions.StudentAlreadyExistException;
import com.example.dynamodemo.repositories.StudentRepository;
import com.example.dynamodemo.request.CreateStudentRequest;
import com.example.dynamodemo.request.UpdateStudentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private StudentRepository studentRepository;

    public String save(CreateStudentRequest createStudentRequest) {

        boolean studentPresent = Optional.ofNullable(createStudentRequest.getStudentId())
                .map(studentId -> getStudent(studentId, createStudentRequest.getFirstName()))
                .map(Student::getStudentId)
                .isPresent();

        if (studentPresent) {
            throw new StudentAlreadyExistException();
        }

        return this.studentRepository.save(Student.valueOf(createStudentRequest));
    }

    public Student getStudent(String studentId, String lastName) {
        return Optional.ofNullable(this.studentRepository.getStudent(studentId, lastName)).orElse(Student.builder().build());
    }

    public void updateStudent(String studentId, String firstName, UpdateStudentRequest updateStudentRequest) {

        Student studentPresent = Optional.ofNullable(getStudent(studentId, firstName))
                .orElseThrow(NotFoundException::new);

        studentPresent.setLastName(updateStudentRequest.getLastName());
        this.studentRepository.updateStudent(studentId, firstName, studentPresent);
    }

    public void deleteStudent(String studentId, String firstName) {

        Student student = Optional.ofNullable(getStudent(studentId, firstName))
                .orElseThrow(NotFoundException::new);
        this.studentRepository.deleteStudent(student);
    }

    public List<Student> getAllStudents() {
        return Optional.ofNullable(this.studentRepository.getAllStudents()).orElse(new ArrayList<>());
    }
}
