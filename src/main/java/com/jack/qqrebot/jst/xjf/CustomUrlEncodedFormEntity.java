package com.jack.qqrebot.jst.xjf;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author mujingjing
 * @date 2019/11/5 12:48
 */
public class CustomUrlEncodedFormEntity extends StringEntity {
    public CustomUrlEncodedFormEntity(List<? extends NameValuePair> parameters, String charset) throws UnsupportedEncodingException {
        super(URLEncodedUtils.format(parameters, charset != null ? charset : HTTP.DEF_CONTENT_CHARSET.name()).replaceAll("\\+","%20"),
                ContentType.create("application/x-www-form-urlencoded", charset));
    }
}
