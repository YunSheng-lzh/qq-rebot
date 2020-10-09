package com.jack.qqrebot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.jack.qqrebot.jst.GroupNoticeService;
import com.jack.qqrebot.utils.SendMsgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service("sendService")
public class SendServiceImpl implements SendServiceI {

    @Value("${desperado.rebot.qq:#{null}}")
    private String rebotQQ;

    private final GroupNoticeService groupNoticeService;

    @Autowired
    public SendServiceImpl(GroupNoticeService groupNoticeService) {
        this.groupNoticeService = groupNoticeService;
    }

    @Override
    public void dealGroupMsg(String message) throws Exception {
        JSONObject jsonObject = JSON.parseObject(message);
        String result = "";
        message = jsonObject.getString("message");
        String group_id = jsonObject.getString("group_id");
        List<String> list = Arrays.asList("dk");
        if (!StringUtils.isEmpty(message) && (list.indexOf(message) != -1)) {
            result = groupNoticeService.getJz();
            SendMsgUtils.sendGroupMsg(group_id, result);
        }
    }

    @Override
    public void dealNotice(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        String result = "";
        String event = jsonObject.getString("event");
        String group_id = jsonObject.getString("group_id");
        if (event.equals("group_increase")) {
            String user_id = jsonObject.getString("user_id");
            result= groupNoticeService.groupIncreaseNotice(user_id);
        }
        SendMsgUtils.sendGroupMsg(group_id, result);
    }

}
