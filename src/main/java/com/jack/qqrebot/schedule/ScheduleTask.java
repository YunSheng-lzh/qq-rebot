package com.jack.qqrebot.schedule;

import com.jack.qqrebot.jst.GroupNoticeService;
import com.jack.qqrebot.jst.ModianProjectService;
import com.jack.qqrebot.service.task.SchedualServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    private final SchedualServiceI schedualService;
    private final GroupNoticeService groupNoticeService;
    private final ModianProjectService modianProjectService;

    @Autowired
    public ScheduleTask(SchedualServiceI schedualService,GroupNoticeService groupNoticeService,ModianProjectService modianProjectService) {
        this.schedualService = schedualService;
        this.groupNoticeService = groupNoticeService;
        this.modianProjectService = modianProjectService;
    }


    @Scheduled(cron = "0 30 0 * * ?")
    public void light(){
        schedualService.goodLight();
    }


    @Scheduled(cron = "*/30 * * * * ?")
    public void updateModianData() throws Exception {
        groupNoticeService.updateXJFData();
    }
}
