package com.aws.controller;

import com.aws.dynamoDBRepository.DynamoDbRepository;
import com.aws.model.Student;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dynamoDB")
public class DynamoDBController {
    @Autowired
    private DynamoDbRepository dynamoDbRepository;

    @PostMapping
    public ResponseEntity<Student> insertIntoDynamoDB(@RequestBody Student student) {
        dynamoDbRepository.insertIntoDynamoDB(student);
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Get student details using student Id", notes = "Getting student details from Dynamo DB", response = Student.class)
    public ResponseEntity<Student> getOneStudentDetails(@RequestParam String studentId, @RequestParam String lastName) {
        Student student = dynamoDbRepository.getOneStudentDetails(studentId, lastName);
        return new ResponseEntity<Student>(student, HttpStatus.OK);
    }
}
