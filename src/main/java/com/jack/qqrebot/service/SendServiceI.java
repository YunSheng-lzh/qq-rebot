package com.jack.qqrebot.service;


import java.io.UnsupportedEncodingException;

public interface SendServiceI {

    void dealGroupMsg(String message) throws Exception;

    void dealNotice(String noticeType);
}
