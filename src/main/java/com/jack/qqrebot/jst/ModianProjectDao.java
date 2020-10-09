package com.jack.qqrebot.jst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Auther: mujj
 * @Date: 2019/7/3 10:30
 * @Description:
 * @Version: 1.0
 */
public interface ModianProjectDao extends JpaRepository<ModianProject,Integer> {

    ModianProject findByTid(String tid);

    List<ModianProject> findByStatusCode(Integer statusCode);

    List<ModianProject> findByStatusCodeAndType(Integer statusCode,Integer type);

}
