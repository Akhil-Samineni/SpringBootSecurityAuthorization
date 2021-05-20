package com.aws.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.aws.util.Credentials;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class SnsClient {
    private static AmazonSNSClient amazonSNSClient;
    private static final Logger logger = LogManager.getLogger(SnsClient.class);
    private static final String EMAIL_SUBJECT = "Test SNS Notification from Java";
    private static final String EMAIL_MESSAGE = "Able to create topic, subscribe to it and publish to that topic from Java";
    @Autowired
    private Credentials credentials;
    public String TOPIC_ARN;

    public void createTopicAndSubscribe() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(credentials.getAwsAccessKey(), credentials.getAwsSecretKey());
        amazonSNSClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().
                withRegion(credentials.getAwsRegion()).
                withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        final CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyEmailTopicFromJava");

        final CreateTopicResult createTopicResult = amazonSNSClient.createTopic(createTopicRequest);

        TOPIC_ARN = createTopicResult.getTopicArn();
        logger.debug("TOPIC_ARN is {}", TOPIC_ARN);
        logger.debug("Create topic request: " + amazonSNSClient.getCachedResponseMetadata(createTopicRequest));
        final SubscribeResult subscribeResult = amazonSNSClient.subscribe(TOPIC_ARN, "email", "akhilsamineni47@gmail.com");

        String policyFileName = "snsAllowS3Policy";
        File policyfile = readAndWriteToFile(policyFileName);
        String path = policyfile.getPath();
        String policy = null;
        try {
            policy = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        policy = policy.replace("topicARN", TOPIC_ARN);
        policy = policy.replace("bucket-name", credentials.getBucketName());
        policy = policy.replace("accountId", credentials.getAccId());
        logger.debug("policy is " + policy);
        System.out.println("policy is " + policy);
        try {
            amazonSNSClient.setTopicAttributes(TOPIC_ARN, "Policy", policy);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void publish() {
        amazonSNSClient.publish(TOPIC_ARN, EMAIL_MESSAGE, EMAIL_SUBJECT);
    }


    public File readAndWriteToFile(String fileName) {
        File file = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            InputStream inputStream1 = classPathResource.getInputStream();
            file = new File(fileName);
            OutputStream outputStream1 = new FileOutputStream(file);
            IOUtils.copy(inputStream1, outputStream1);
        } catch (Exception e) {
            System.out.println("exception in readAndWriteToFile");
            e.printStackTrace();
        }
        return file;
    }

}
