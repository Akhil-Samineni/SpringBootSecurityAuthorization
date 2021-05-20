package com.aws.dynamoDBRepository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.aws.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class DynamoDbRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public void insertIntoDynamoDB(Student student) {
        try {
            mapper.save(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Student getOneStudentDetails(String studentId, String lastName) {
        return mapper.load(Student.class, studentId, lastName);
    }
}
