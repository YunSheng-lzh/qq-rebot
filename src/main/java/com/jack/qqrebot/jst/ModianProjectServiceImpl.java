package com.jack.qqrebot.jst;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.qqrebot.jst.xjf.XjfUtils;
import com.jack.qqrebot.utils.HttpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;


/**
 * @Auther: mujj
 * @Date: 2019/7/3 10:32
 * @Description:
 * @Version: 1.0
 */

@Service("modianProjectService")
public class ModianProjectServiceImpl implements ModianProjectService {

    @Value("${desperado.modian.userid:#{null}}")
    private String modianUserId;
    @Autowired
    private ModianProjectDao modianProjectDao;


    @Override
    public void updateProjectsData() throws Exception {
        updateMDProjectsData();
        updateXJFProjectsData();
    }


    private void updateMDProjectsData() {
        HashSet<String> tidSet = new HashSet<>();
        Long timeStamp = System.currentTimeMillis() /1000 ;

        String url = "http://mapi.modian.com//v45/user/build_product_list";
        String param = "code=148ed474a1028443&user_id=1431046&_t="+timeStamp+"&to_user_id="+modianUserId+"&json_type=1&page_index=0&page_rows=100&client=2&";

        String result = HttpUtils.sendPost(url, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray datas = jsonObject.getJSONArray("data");
        for (int j=0; j<datas.size(); j++){
            JSONObject data = datas.getJSONObject(j);
            Integer statusCode = data.getInteger("status_code");
            if(statusCode != 0){
                continue;
            }
            String tid = data.getString("id");
            if(tidSet.contains(tid)){
                return;
            }
            System.out.println("----------更新摩点项目----------");
            String userId = data.getString("user_id");
            String username = data.getString("username");
            String name = data.getString("name");
            Date startTime = data.getDate("start_time");
            Date endTime = data.getDate("end_time");
            Double goal = data.getDouble("goal");
            Double backerMoney = data.getDouble("backer_money");
            Double progress = data.getDouble("progress");
            Integer backerCount = data.getInteger("backer_count");

            String logo = data.getString("logo");
            Integer subscribeCount = data.getInteger("subscribe_count");

            String postId = getPostIdByTid(tid);
            ModianProject modianProject = modianProjectDao.findByTid(tid);
            if(modianProject == null){
                modianProject = new ModianProject();
            }
            modianProject.setTid(tid);
            modianProject.setUserId(userId);
            modianProject.setUsername(username);
            modianProject.setName(name);
            modianProject.setStartTime(startTime);
            modianProject.setEndTime(endTime);
            modianProject.setGoal(goal);
            modianProject.setBackerMoney(backerMoney);
            modianProject.setBackerCount(backerCount);
            modianProject.setStatusCode(statusCode);
            modianProject.setLogo(logo);
            modianProject.setProgress(progress);
            modianProject.setSubscribeCount(subscribeCount);
            modianProject.setPid(postId);
            modianProjectDao.save(modianProject);

            tidSet.add(tid);
        }

    }



    private void updateXJFProjectsData() throws Exception {

        String items = XjfUtils.getItems();
        JSONObject object = JSONObject.parseObject(items);
        if(!StringUtils.isEmpty(items) && object.getString("retcode").equals("0")){
            String project_array = object.getString("project_array");
            JSONArray array = JSONArray.parseArray(project_array);
            for (int i=0; i< array.size(); i++){
                JSONObject json = array.getJSONObject(i);
                String projectId = json.getString("project_id");
                ModianProject modianProject = modianProjectDao.findByTid(projectId);
                if(modianProject == null){
                    modianProject = new ModianProject();
                }
                modianProject.setTid(projectId);
                modianProject.setUserId(json.getString("sponsor_unionid"));
                modianProject.setUsername(json.getString("sponsor_nickname"));
                modianProject.setName(json.getString("title"));
                modianProject.setStartTime(json.getDate("begin_time"));
                modianProject.setEndTime(json.getDate("expire_time"));
                modianProject.setGoal(json.getDouble("target_amount") / 100);
                modianProject.setBackerMoney(json.getDouble("total_amount") / 100);
                modianProject.setBackerCount(json.getInteger("pay_num"));
                modianProject.setStatusCode(1);
                if(System.currentTimeMillis() - json.getDate("expire_time").getTime() > 0){
                    modianProject.setStatusCode(9);
                }
                modianProject.setLogo(json.getString("pic_url"));
                modianProject.setProgress(json.getDouble("percent"));
                modianProject.setSubscribeCount(json.getInteger("pay_num"));
                modianProject.setPid(projectId);
                modianProject.setType(1);
                modianProjectDao.save(modianProject);
            }
        }
        String xjf = XjfUtils.getTotal("2oY7VsfA0100003100047716");
        if(!StringUtils.isEmpty(xjf)){
            JSONObject json = JSONObject.parseObject(xjf);
            ModianProject modianProject = modianProjectDao.findByTid("2oY7VsfA0100003100047716");
            if(modianProject == null){
                modianProject = new ModianProject();
            }
            modianProject.setTid("2oY7VsfA0100003100047716");
            modianProject.setUserId(json.getString("admin_unionid"));
            modianProject.setUsername(json.getString("admin_nickname"));
            modianProject.setName(json.getString("name"));
            modianProject.setStartTime(json.getDate("create_time"));
            modianProject.setGoal(0.0);
            modianProject.setBackerMoney(json.getDouble("balance_in") / 100);
            modianProject.setBackerCount(json.getInteger("user_num"));
            modianProject.setStatusCode(1);
            modianProject.setLogo(json.getString("pic_url"));
            modianProject.setProgress(0.0);
            modianProject.setSubscribeCount(json.getInteger("user_num"));
            modianProject.setPid(json.getString("short_guid"));
            modianProject.setType(1);
            modianProjectDao.save(modianProject);
        }
    }


    private String getPostIdByTid(String tid){
        String url =" http://sapi.modian.com/v45/main/productInfo";
        Long time = System.currentTimeMillis() / 1000;
        String param = "code=6fb4088b1b45a6b5&pro_id="+tid+"&user_id=1431046&_t="+time+"&json_type=1&client=2&";
        String s = HttpUtils.sendPost(url, param);
        JSONObject object = JSONObject.parseObject(s).getJSONObject("data").getJSONObject("product_info");
        return  object.getString("moxi_post_id");
    }
}
