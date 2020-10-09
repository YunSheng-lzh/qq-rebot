package com.jack.qqrebot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jack.qqrebot.service.SendServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FirstController {

    private final SendServiceI sendService;

    @Autowired
    public FirstController(SendServiceI sendService) {
        this.sendService = sendService;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public void get(@RequestBody String message) throws Exception {
        JSONObject jsonObject = JSON.parseObject(message);
        String messageType = jsonObject.getString("message_type");
        String postType = jsonObject.getString("post_type");
        if(postType.equals("event")){
            sendService.dealNotice(message);
        }else if("group".equals(messageType)) {
            sendService.dealGroupMsg(message);
        }
    }

}
