package com.jack.qqrebot.jst;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jack.qqrebot.jst.xjf.XjfUtils;
import com.jack.qqrebot.utils.DateUtils;
import com.jack.qqrebot.utils.HttpUtils;
import com.jack.qqrebot.utils.SendMsgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GroupNoticeService {

    @Autowired
    private ModianProjectDao modianProjectDao;
    @Autowired
    private ModianProjectService modianProjectService;
    @Autowired
    private ProjectDao projectDao;

    @Value("${desperado.rebot.groupqq:#{null}}")
    private String groupQq;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @Author mujj
     * @Description // 新人入群欢迎
     * @Date 21:31 2019/8/27
     * @Param [userId]
     * @return java.lang.String
     **/
    public String groupIncreaseNotice(String userId){
        StringBuilder result = new StringBuilder();
        result.append("[CQ:at,qq=" + userId + "] 欢迎宁来到郭爽的朋友圈！妹妹宁终于来啦！\n\n");
        // result.append("【百场应援-可乐妹】\n" + "http://mourl.cc/nUZhQPJT\n\n");
        result.append("郭爽微博 : " + "SNH48-郭爽 \n" + "https://weibo.com/u/6666235165\n");
        result.append("郭爽抖音 : " + "好吧我就是郭爽 \n" + "http://v.douyin.com/uhKHAu/\n");
        result.append("郭爽b站 : " + "有点ok的爽崽 \n" + "https://space.bilibili.com/394343318\n");
        result.append("郭爽应援会b站 : " + "SNH48郭爽应援会 \n" + "https://space.bilibili.com/35374377\n");
        return result.toString();
    }




    public void updateData() throws Exception {
        System.out.println("---------开始更新------------");
        modianProjectService.updateProjectsData();
        List<ModianProject> modianProjects = modianProjectDao.findByStatusCode(0);
        li :for(ModianProject modianProject : modianProjects){
            long millis = System.currentTimeMillis()/1000;
            String url="http://mapi.modian.com/v45/product/comment_list?_t="+millis+4+"&client=2&json_type=1" +
                    "&mapi_query_time="+millis+"&moxi_post_id="+modianProject.getPid();
            double currMoney = modianProject.getBackerMoney();
            for (int i= 0; i< modianProject.getBackerCount(); i=i+10){
                url = url + "&page_index="+i+"&page_rows=10&pro_class=202&pro_id="+modianProject.getTid()+"&code=6fb4088b1b45a6b5";

                String get = HttpUtils.sendGet(url, null);
                JSONObject object = JSONObject.parseObject(get);
                JSONArray data = object.getJSONArray("data");

                for (int j=0; j<data.size(); j++){
                    JSONObject jsonObject = data.getJSONObject(j);
                    Integer pay_amount = jsonObject.getInteger("pay_amount");
                    if(pay_amount == 0){
                        continue;
                    }
                    String pid = jsonObject.getString("id");
                    if(isExists(pid,modianProject.getTid())){
                        continue li;
                    }
                    String userId = jsonObject.getString("user_id");
                    String userName = jsonObject.getJSONObject("user_info").getString("username");
                    double money = Double.valueOf(jsonObject.getString("content").replace("支持了","")
                            .replace("元","").trim());

                    Date date = null;
                    try {
                        date = sdf.parse(jsonObject.getString("ctime"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ProjectVo projectVo = new ProjectVo();
                    projectVo.setPid(pid);
                    projectVo.setTid(modianProject.getTid());
                    projectVo.setUserId(userId);
                    projectVo.setUsername(userName);
                    projectVo.setMoney(money);
                    projectVo.setCreateDate(date);
                    projectDao.save(projectVo);
                    UserRanking userRanking = getUserRanking(modianProject.getTid(),userId);
                    /*String thanks = new String();
                    if ("可乐妹小爽牌可乐".equals(modianProject.getName())) {
                        thanks = "感谢您为小爽百场应援做出的贡献！";
                    } else {
                        thanks = "谢谢您的支持，圆梦郭爽的广州之旅！";
                    }*/
                    // 打卡播报
                    String result ="感谢 "+userName+" 刚刚在"+modianProject.getName()+"中支持了 ¥"+money+"，" +
                            "感谢您对郭爽的支持!\n\n"
                            +modianProject.getName()+":\n"+
                            "累计打卡：¥" + userRanking.money +"\n"+
                            "当前排名：第" + userRanking.ranking +"名/总人数:"+modianProject.getBackerCount()+"人\n"+
                            "目前金额：¥" +currMoney+"\n"+
                            "目标金额：¥" + modianProject.getGoal()+"\n"+
                            "当前进度：¥" + modianProject.getProgress()+"%\n" +
                            "剩余时间：" + DateUtils.dateDiff(sdf1.format(new Date()), sdf1.format(modianProject.getEndTime()), "yyyy-MM-dd HH:mm:ss")+"\n\n"
                            +"支持链接：https://zhongchou.modian.com/item/"+modianProject.getTid()+".html";

                    SendMsgUtils.sendGroupMsg(groupQq, result);
                }
            }
        }
    }


    public GroupNoticeService.UserRanking getUserRanking(String tid, String userId) {
        Map map = this.projectDao.getUserRanking(tid, userId);
        GroupNoticeService.UserRanking userRanking = new GroupNoticeService.UserRanking();
        userRanking.money = (Double) map.get("money");
        userRanking.ranking = Double.valueOf((Double) map.get("no")).intValue();
        Long userCount = this.projectDao.countDistinctByTid(tid);
        userRanking.userCount = userCount;
        return userRanking;
    }

    private boolean isExists(String pid, String tid) {
        ProjectVo projectVo = this.projectDao.findByPidAndTid(pid, tid);
        return projectVo != null;
    }


    /**
     * @Author mujj
     * @Description // 获取打卡链接
     * @Date 21:32 2019/8/27
     * @Param []
     * @return java.lang.String
     **/
    public String getJz() throws Exception {
        this.modianProjectService.updateProjectsData();
        List<ModianProject> modianProjects = this.modianProjectDao.findByStatusCodeAndType(0,0);

        StringBuilder sb = new StringBuilder();
        sb.append("当前打卡链接:\n");
        Iterator var3 = modianProjects.iterator();

        while (var3.hasNext()) {
            ModianProject modianProject = (ModianProject) var3.next();
            sb.append(modianProject.getName()).append("\n");
            sb.append("当前进度: ").append(modianProject.getBackerMoney()).append("/").append(modianProject.getGoal()).append("\n");
            sb.append("剩余时间：").append(DateUtils.dateDiff(sdf1.format(new Date()), sdf1.format(modianProject.getEndTime()), "yyyy-MM-dd HH:mm:ss")).append("\n");
            sb.append("打卡链接：https://zhongchou.modian.com/item/").append(modianProject.getTid()).append(".html").append("\n\n");
        }

        List<ModianProject> modianProjects1 = this.modianProjectDao.findByStatusCodeAndType(1,1);
        var3 = modianProjects1.iterator();

        while (var3.hasNext()) {
            ModianProject modianProject = (ModianProject) var3.next();
            sb.append(modianProject.getName()).append("(小经费)\n");
            if(modianProject.getGoal() != 0){
                sb.append("当前进度:").append(modianProject.getBackerMoney()).append("/");
                sb.append(modianProject.getGoal()).append("\n");
            }

            if(modianProject.getEndTime() != null){
                sb.append("剩余时间：").append(DateUtils.dateDiff(sdf1.format(new Date()), sdf1.format(modianProject.getEndTime()), "yyyy-MM-dd HH:mm:ss")).append("\n");
            }else {
                sb.append("剩余时间: 长期有效").append("\n");
            }
        }
        sb.append("[CQ:image,file=xjf.jpg]\n");
        sb.append("谢谢您对郭爽的支持！");
        return sb.toString();
    }

    class UserRanking{
        int ranking;
        double money;
        long userCount;
    }




    public void updateXJFData() throws Exception {
        System.out.println("---------开始更新小经费------------");
        modianProjectService.updateProjectsData();
        List<ModianProject> modianProjects = this.modianProjectDao.findByStatusCodeAndType(1,1);
        Iterator var2 = modianProjects.iterator();
        while (var2.hasNext()){
            ModianProject modianProject = (ModianProject)var2.next();
            String projectId = modianProject.getTid();
            Date createDate = projectDao.getLastCreateDate(projectId);
            String endTime = DateUtils.foramt(new Date(), "yyyy-MM-dd HH:mm:ss");
            String startTime = "2019-07-01 12:00:00";
            if (!StringUtils.isEmpty(createDate)) {
                startTime = DateUtils.foramt(createDate, "yyyy-MM-dd HH:mm:ss");
            }
            String water = XjfUtils.water(projectId, startTime, endTime);
            if (!StringUtils.isEmpty(water)) {

                String water_array1 = water.substring(0, water.indexOf("water_array") + 13);
                String water_array2 = water.substring(water.indexOf("water_array") + 13, water.lastIndexOf("water_num") - 2);
                String water_array3 = water.substring(water.lastIndexOf("water_num") - 2);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(water_array1);
                stringBuffer.append(water_array2.substring(1, water_array2.length() - 1));
                stringBuffer.append(water_array3);

                JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
                JSONArray array = jsonObject.getJSONObject("water_array").getJSONArray("water_array");
                for (int j = 0; j < array.size(); ++j) {
                    JSONObject jsonObject1 = array.getJSONObject(j);
                    String pid = jsonObject1.getString("bkid");
                    String projectName = jsonObject1.getString("project_name") == null ? modianProject.getName():jsonObject1.getString("project_name");
                    if(!projectName.equals(modianProject.getName())){
                        continue;
                    }
                    if (this.isExists(pid, projectId)) {
                        continue;
                    }
                    ProjectVo projectVo = new ProjectVo();
                    projectVo.setPid(pid);
                    projectVo.setTid(projectId);
                    projectVo.setUserId(jsonObject1.getString("nickname"));
                    String username = jsonObject1.getString("nickname");
                    if(jsonObject1.containsKey("remark")){
                        username = jsonObject1.getString("remark");
                    }
                    projectVo.setUsername(username);
                    projectVo.setMoney(jsonObject1.getDouble("fee") / 100);
                    projectVo.setCreateDate(jsonObject1.getDate("time"));
                    this.projectDao.save(projectVo);
                    GroupNoticeService.UserRanking userRanking = this.getUserRanking(projectId, jsonObject1.getString("nickname"));
                    StringBuilder sb = new StringBuilder();


                    sb.append("感谢 ").append(username).append(" 刚刚在【").
                            append(modianProject.getName()).append("(小经费)】").append("中支持了 ¥").append(jsonObject1.getDouble("fee") / 100).append("，")
                            .append("感谢您对郭爽的支持\n\n")
                            .append("累计金额：¥").append(userRanking.money).append("\n")
                            .append("当前排名：第").append(userRanking.ranking).append("名/总人数:").append(userRanking.userCount).append("人\n")
                            .append("目前金额：¥").append(modianProject.getBackerMoney()).append("\n");
                    if(modianProject.getGoal() != 0){
                        sb.append("目标金额:￥").append(modianProject.getGoal()).append("\n");
                        sb.append("目前进度:￥").append(modianProject.getProgress()).append("%\n");
                    }

                    if(modianProject.getEndTime() != null){
                        sb.append("剩余时间：").append(DateUtils.dateDiff(sdf1.format(new Date()), sdf1.format(modianProject.getEndTime()), "yyyy-MM-dd HH:mm:ss")).append("\n");
                    }else {
                        sb.append("剩余时间: 长期有效\n");
                    }

                    sb.append("[CQ:image,file=xjf.jpg]");

                    System.out.println("---------------------------------------小经费-------------------------------------------");
                    System.out.println(sb.toString());
                    SendMsgUtils.sendGroupMsg(groupQq, sb.toString());
                }
            }
        }

    }

}
