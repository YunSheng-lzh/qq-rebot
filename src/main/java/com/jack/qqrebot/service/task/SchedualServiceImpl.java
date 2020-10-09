package com.jack.qqrebot.service.task;



import com.jack.qqrebot.jst.wanan.WananService;
import com.jack.qqrebot.utils.SendMsgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service("schedualService")
public class SchedualServiceImpl implements SchedualServiceI {

    @Value("${desperado.rebot.groupqq:#{null}}")
    private String groupQq;

    private final WananService wananService;

    @Autowired
    public SchedualServiceImpl(WananService wananService) {
        this.wananService = wananService;
    }

    @Override
    public void goodLight() {
        boolean flag = false;
        try {
            SendMsgUtils.sendGroupMsg(groupQq,wananService.getResult());
        }catch (Exception e){
            flag = true;
        }finally {
            if(flag){
                ScheduledExecutorService scheduledExecutorService =
                        Executors.newScheduledThreadPool(1);
                scheduledExecutorService.schedule(this::goodLight,60,TimeUnit.SECONDS);
            }
        }

    }

}
