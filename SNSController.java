package com.aws.controller;

import com.aws.sns.SnsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sns")
public class SNSController {
    @Autowired
    private SnsClient snsClient;

    @RequestMapping("/createTopic")
    public String createTopicAndPublish() {
        snsClient.createTopicAndSubscribe();
        return "Able to create topic, subscription details sent";
    }

    @RequestMapping("/sendSNS")
    public String sendSNS() {
        snsClient.publish();
        return "Able to publish message to email after successfully subscribed from java";
    }
}
