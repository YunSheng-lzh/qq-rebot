package com.jack.qqrebot.jst.xjf;

import com.alibaba.fastjson.JSONObject;
import com.jack.qqrebot.utils.DateUtils;
import com.jack.qqrebot.utils.HttpUtils;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class XjfUtils {

    private static final String GRP_QLSKEY="v0ae788b1105dbad95fa568d8ee12bcf";
    private static final String  GRP_QLUIN="3539a2d13e47609270c15a919ac083be";

    public static String getItems() throws Exception {

        Map<String,String> headers = new HashMap<>();
        headers.put("Host","groupaccount.tenpay.com");
        headers.put("Accept","*/*");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept-Encoding","gzip");
        headers.put("Connection","keep-alive");
        headers.put("Cookie","grp_qlskey="+GRP_QLSKEY+";grp_qluin="+GRP_QLUIN+"");
        headers.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.5(0x17000523) NetType/WIFI Language/zh_CN");
        headers.put("Referer","https://servicewechat.com/wxcf8e5b328359cb7a/207/page-frame.html");
        headers.put("Accept-Language","zh-cn");
        headers.put("charset","utf-8");
        Map<String,String> params = new HashMap<>();
        params.put("parent_guid","2oY7VsfA0100003100047716");
        params.put("limit","50");
        params.put("offset","0");
        params.put("qry_state","");
        String result = HttpUtils.requestOCRForHttp(XjfApi.JZ_ITEMS, params, headers);
        return result;
    }


    public static String getTotal(String groupAccountId) throws Exception {

        Map<String,String> headers = new HashMap<>();
        headers.put("Host","groupaccount.tenpay.com");
        headers.put("Accept","*/*");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept-Encoding","gzip");
        headers.put("Connection","keep-alive");
        headers.put("Cookie","grp_qlskey="+GRP_QLSKEY+";grp_qluin="+GRP_QLUIN+"");
        headers.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.5(0x17000523) NetType/WIFI Language/zh_CN");
        headers.put("Referer","https://servicewechat.com/wxcf8e5b328359cb7a/200/page-frame.html");
        headers.put("Accept-Language","zh-cn");
        headers.put("charset","utf-8");
        Map<String,String> params = new HashMap<>();
        params.put("group_account_id",groupAccountId);
        String result = HttpUtils.requestOCRForHttp(XjfApi.JZ_TOTAL, params, headers);
        return result;
    }


    public static String water(String groupAccountId,String startTime,String endTime) throws Exception {
        Map<String,String> headers = new HashMap<>();
        headers.put("Host","groupaccount.tenpay.com");
        headers.put("Accept","*/*");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept-Encoding","gzip");
        headers.put("Connection","keep-alive");
        headers.put("Cookie","grp_qlskey="+GRP_QLSKEY+";grp_qluin="+GRP_QLUIN+"");
        headers.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.5(0x17000523) NetType/WIFI Language/zh_CN");
        headers.put("Referer","https://servicewechat.com/wxcf8e5b328359cb7a/202/page-frame.html");
        headers.put("Accept-Language"," zh-cn");
        Map<String,String> params = new LinkedHashMap<>();
        params.put("group_account_id",groupAccountId);
        params.put("start_time", startTime);
        params.put("end_time",endTime);
        params.put("qry_ver","1");
        params.put("lastbkid","");
        params.put("offset","20");
        params.put("limit","50");
        params.put("type","2");
        params.put("qry_sub","1");
        String result = HttpUtils.requestOCRForHttp(XjfApi.JZ_WATER, params, headers);
        if(!StringUtils.isEmpty(result)){
            result = URLDecoder.decode(result,"UTF-8");
        }
        return result;
    }




    public static Double getTotalMoney(String groupAccountId) throws Exception {

        Map<String,String> headers = new HashMap<>();
        headers.put("Host","groupaccount.tenpay.com");
        headers.put("Accept","*/*");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept-Encoding","gzip");
        headers.put("Connection","keep-alive");
        headers.put("Cookie","grp_qlskey="+GRP_QLSKEY+";grp_qluin="+GRP_QLUIN+"");
        headers.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.5(0x17000523) NetType/WIFI Language/zh_CN");
        headers.put("Referer","https://servicewechat.com/wxcf8e5b328359cb7a/207/page-frame.html");
        headers.put("Accept-Language","zh-cn");
        headers.put("charset","utf-8");
        Map<String,String> params = new HashMap<>();
        params.put("group_account_id",groupAccountId);
        String result = HttpUtils.requestOCRForHttp(XjfApi.JZ_TOTAL, params, headers);
        if(!StringUtils.isEmpty(result)){
            JSONObject total = JSONObject.parseObject(result);
            String balance = total.getString("balance");
            if(!StringUtils.isEmpty(balance)){
                return Double.valueOf(balance) / 100;
            }
        }
        return 0.0;
    }


    public static void main(String[] args) throws Exception {
        Date date = DateUtils.plusOrAddDay(new Date(),-1);
        String startTime = DateUtils.foramt(date, "yyyy-MM-dd hh:mm:ss");
       String endTime = "2100-01-31 00:00:00";
        System.out.println(getItems());
        //System.out.println(getTotal("N5RN5uRZ0100009000041062"));
    }
}
