package com.aws.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.aws.util.Credentials;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class S3JavaSDK {

    @Autowired
    private Credentials credentials;

    final AwsCrypto awsCrypto = AwsCrypto.builder()
            .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
            .build();

    public void createAndPopulateSimpleBucket() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(credentials.getAwsAccessKey(), credentials.getAwsSecretKey());

        /*AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();*/
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .build();

        List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b : buckets) {
            deleteObjects(s3Client, b.getName());
            s3Client.deleteBucket(b.getName());
        }
        String newBucketName = credentials.getBucketName();
        try {
            s3Client.createBucket(newBucketName);
            String policyFileName = "encrypted-folder-policy";
            File policyfile = readAndWriteToFile(policyFileName);
            String path = policyfile.getPath();
            String policy = new String(Files.readAllBytes(Paths.get(path)));
            policy = policy.replace("bucketname", newBucketName);
            s3Client.setBucketPolicy(newBucketName, policy);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void uploadFile(String newBucketName, String fileName) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(credentials.getAwsAccessKey(), credentials.getAwsSecretKey());

        /*AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();*/
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .build();

        File someText1 = readAndWriteToFile(fileName);
        try {
            PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName, "encrypted/" + fileName, someText1);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            putRequest1.setMetadata(objectMetadata);
            PutObjectResult response1 = s3Client.putObject(putRequest1);
            System.out.println("Uploaded object encryption status is " +
                    response1.getSSEAlgorithm());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void deleteObjects(AmazonS3 s3Client, String bucket_name) {
        ObjectListing object_listing = s3Client.listObjects(bucket_name);
        for (Iterator<?> iterator = object_listing.getObjectSummaries().iterator(); iterator.hasNext(); ) {
            S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
            s3Client.deleteObject(bucket_name, summary.getKey());
        }
    }

    public void createAndTransitEncryptSimpleBucket() throws Exception {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(credentials.getAwsAccessKey(), credentials.getAwsSecretKey());
        /*AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();*/
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(credentials.getAwsRegion())
                .build();
        /*List<Bucket> buckets = s3Client.listBuckets();
        for (Bucket b:buckets) {
            deleteObjects(s3Client,b.getName());
            s3Client.deleteBucket(b.getName());
        }*/
        String newBucketName = credentials.getBucketTransitName();
        try {
            s3Client.createBucket(newBucketName);
            final String fileName = "sometext.txt";
            File file = readAndWriteToFile(fileName);
            byte[] encryptedData = encryptDataUsingKMS(file);
            FileUtils.writeByteArrayToFile(new File(file.getPath()), encryptedData);
            PutObjectRequest putRequest1 = new PutObjectRequest(newBucketName, fileName, file);

            PutObjectResult response1 = s3Client.putObject(putRequest1);
            System.out.println("Uploaded object encryption status is " +
                    response1.getSSEAlgorithm());

            S3Object object = s3Client.getObject(new GetObjectRequest(newBucketName, fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] encryptDataUsingKMS(File file) {
        final KmsMasterKeyProvider keyProvider = KmsMasterKeyProvider.builder().buildStrict(credentials.getKeyArn());
        CryptoResult<byte[], KmsMasterKey> encryptResult = null;
        byte[] unencrypted;
        final Map<String, String> encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");
        try {
            unencrypted = Files.readAllBytes(file.toPath());
            encryptResult = awsCrypto.encryptData(keyProvider, unencrypted, encryptionContext);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptResult.getResult();
    }

    public byte[] decryptDataUsingKMS(byte[] encrypted) {
        final KmsMasterKeyProvider keyProvider = KmsMasterKeyProvider.builder().buildStrict(credentials.getKeyArn());
        CryptoResult<byte[], KmsMasterKey> encryptResult = null;
        final Map<String, String> encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");
        try {
            encryptResult = awsCrypto.decryptData(keyProvider, encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptResult.getResult();
    }

}
