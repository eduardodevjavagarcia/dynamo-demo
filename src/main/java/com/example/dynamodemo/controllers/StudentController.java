package com.example.dynamodemo.controllers;

import com.example.dynamodemo.request.CreateStudentRequest;
import com.example.dynamodemo.request.UpdateStudentRequest;
import com.example.dynamodemo.response.StudentResponse;
import com.example.dynamodemo.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Void> postStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest) {
        String studentId = studentService.save(createStudentRequest);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}/{firstName}")
                .buildAndExpand(studentId, createStudentRequest.getFirstName())
                .toUri())
                .build();
    }

    @GetMapping("/{studentId}/{firstName}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable String studentId, @PathVariable String firstName) {
        return ResponseEntity.ok(StudentResponse.valueOf(studentService.getStudent(studentId, firstName)));
    }

    @PutMapping("/{studentId}/{firstName}")
    public ResponseEntity<Void> updateStudentDetails(@PathVariable String studentId, @PathVariable String firstName, @RequestBody UpdateStudentRequest updateStudentRequest) {
        studentService.updateStudent(studentId, firstName, updateStudentRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{studentId}/{firstName}")
    public ResponseEntity<Void> deleteStudentDetails(@PathVariable("studentId") String studentId,
                                                     @PathVariable("firstName") String firstName) {
        studentService.deleteStudent(studentId, firstName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents().stream().map(StudentResponse::valueOf).collect(Collectors.toList()));
    }
}