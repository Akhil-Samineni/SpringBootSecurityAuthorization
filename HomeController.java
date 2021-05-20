package com.aws.controller;

import com.aws.impl.S3JavaSDK;
import com.aws.util.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/s3")
public class HomeController {

    @Autowired
    public S3JavaSDK s3JavaSDK;

    @Autowired
    public Credentials credentials;

    @RequestMapping("/createBucket")
    @PreAuthorize("hasRole('ADMIN')")
    public String createBucket(Principal principal) {

        s3JavaSDK.createAndPopulateSimpleBucket();
        return "Bucket created successfully";
    }

    @RequestMapping("/uploadFile1")
    @PreAuthorize("hasRole('USER')")
    public String uploadFile() {
        s3JavaSDK.uploadFile(credentials.getBucketName(), "sometext.txt");
        return "File uploaded successfully";
    }

  /*  @RequestMapping("/createBucketAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createBucketAuthority(Principal principal) {

        s3JavaSDK.createAndPopulateSimpleBucket();
        return "Bucket created successfully";
    }

    @RequestMapping("/uploadFile1Auth")
    @PreAuthorize("hasAuthority('USER')")
    public String uploadFileAuthority() {
        s3JavaSDK.uploadFile(credentials.getBucketName(), "sometext.txt");
        return "File uploaded successfully";
    }*/


    @RequestMapping("/uploadFile2")
    public String uploadFile2() {
        s3JavaSDK.uploadFile(credentials.getBucketName(), "sometext2.txt");
        return "File uploaded successfully";
    }

    @RequestMapping("/createBucketTransitEncrypt")
    public String createBucketTransitEncrypt() throws Exception {
        s3JavaSDK.createAndTransitEncryptSimpleBucket();
        return "createBucketTransitEncrypt created successfully";
    }
}
