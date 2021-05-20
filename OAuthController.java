package com.aws.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @RequestMapping("/oauth")
    @ResponseBody
    public String oauth() {
        return "oAuth authentication";
    }
}
