package com.aws.util;


/*import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Created by Akhil on 23/07/2019.
 */
@Component
public class Credentials {
    @Value("${amazon.access.key}")
    private String awsAccessKey;
    @Value("${amazon.access.secret-key}")
    private String awsSecretKey;
    @Value("${amazon.region}")
    private String awsRegion;
    @Value("${amazon.kms.keyarn}")
    private String keyArn;
    @Value("${amazon.s3.bucketName}")
    private String bucketName;
    @Value("${amazon.s3.bucketTransitName}")
    private String bucketTransitName;
    @Value("${amazon.s3.accId}")
    private String accId;

    /*public Credentials(){

    }
    @PostConstruct
    public void init(){
        AWSSimpleSystemsManagement ssmClient = AWSSimpleSystemsManagementClientBuilder
                .standard().withRegion("us-east-1").build();
        GetParameterRequest parameterRequest= new GetParameterRequest().withName("");
        GetParameterResult parameterResult= new GetParameterResult();
        parameterResult = ssmClient.getParameter(parameterRequest);

    }*/
    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public String getKeyArn() {
        return keyArn;
    }

    public void setKeyArn(String keyArn) {
        this.keyArn = keyArn;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketTransitName() {
        return bucketTransitName;
    }

    public void setBucketTransitName(String bucketTransitName) {
        this.bucketTransitName = bucketTransitName;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}
