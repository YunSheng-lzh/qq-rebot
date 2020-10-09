package com.jack.qqrebot.jst.wanan;

import com.alibaba.fastjson.JSONArray;
import com.jack.qqrebot.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WananService {


    private static JSONArray weeks = JSONArray.parseArray("[\"日\",\"一\",\"二\",\"三\",\"四\",\"五\",\"六\"]");

    /**
     * @Author mujj
     * @Description // 说晚安
     * @Date 21:29 2019/8/27
     * @Param []
     * @return java.lang.String
     **/
    public String getResult() {

        String url="https://nongli.911cha.com/";
        String result = HttpUtils.sendGet(url, null);
        Document document = Jsoup.parse(result);
        Element elementsByClass = document.getElementsByClass("mt f14").get(0);
        Elements elements = elementsByClass.getElementsByTag("tr");
        StringBuilder sb = new StringBuilder();
        sb.append("新的一天已到，我来给宁报时啦！\n\n");
        sb.append(getTodayString()).append("\n");
        elements.forEach(element -> {
            element.getElementsByTag("th").forEach(element1 -> {
                if(element1.text().equals("农历")){
                    Element td = element.getElementsByTag("td").get(0);
                    sb.append("农历:").append(td.text()).append("\n\n");
                }
            });
        });

        elementsByClass = document.getElementsByClass("mt f14").get(1);
        elements = elementsByClass.getElementsByTag("tr");

        elements.forEach(element -> {
            element.getElementsByTag("th").forEach(element1 -> {
                if(element1.text().contains("本日之吉凶")){
                    Element td = element.getElementsByTag("td").get(0);
                    String text = td.text();
                    text = text.replace(td.getElementsByTag("span").text(),"");
                    String[] strings = text.split(" ");
                    sb.append("本日之吉凶:").append("\n").append(strings[strings.length -2]).append(strings[strings.length-1]).append("\n\n");
                }
            });
        });

        sb.append("各位，晚安好梦");
        return sb.toString();
    }


    private static String getTodayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String iday = sdf.format(new Date());
        String[] split = iday.split("-");
        return split[0] + "年" + split[1] + "月" + split[2] + "日 星期" + weeks.getString(new Date().getDay());
    }
}
