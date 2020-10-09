package com.jack.qqrebot.jst;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: mujj
 * @Date: 2019/6/28 21:59
 * @Description:
 * @Version: 1.0
 */
public interface ProjectDao extends JpaRepository<ProjectVo,Integer> {

        Long countByTid(String tid);

        @Query(value = "select count(distinct user_id) from project where tid = :tid",nativeQuery = true)
        Long countDistinctByTid(String tid);

        @Query("SELECT createDate,COUNT(DISTINCT userId) as count,COUNT(userId) as totle,sum(money)as money FROM ProjectVo where tid= :tid GROUP BY DATE(createDate)")
        public List getCountGroupByDate(@Param("tid") Integer tid);

        @Query("SELECT COUNT(DISTINCT userId) as count,COUNT(userId) as total,COALESCE(sum(money),0) as money FROM ProjectVo where tid= :tid and DATE(createDate)=CURDATE() ")
        public List getTodayCount(@Param("tid") Integer tid);

        @Query("SELECT COUNT(DISTINCT userId) as count,COUNT(userId) as total, COALESCE(sum(money),0) as money FROM ProjectVo where tid= :tid")
        public List allCount(@Param("tid") Integer tid);

        @Query(value = "SELECT substring_index(group_concat(username order by create_date desc),',',1),sum(money) FROM project where tid= :tid and DATE(create_date)=CURDATE() group by user_id order by create_date asc",nativeQuery = true)
        public List getTodayUser(@Param("tid") Integer tid);

        public ProjectVo findByPidAndTid(String pid, String tid);

        @Query(value = "SELECT pay_id from project order by create_date desc limit 1",nativeQuery = true)
        public String getLastPayId(@Param("tid") Integer tid);

        @Query(value = "SELECT create_date from project where tid = :tid order by create_date desc limit 1",nativeQuery = true)
        public Date getLastCreateDate(@Param("tid") String tid);

        Long countByPayId(@Param("payId") String payId);

        @Query(value = "SELECT substring_index(group_concat(username order by create_date desc),',',1),sum(money) FROM project where tid=:tid group by user_id order by sum(money) desc",nativeQuery = true)
        public List getUserRanking(@Param("tid")String tid);

        @Query(value = "SELECT * FROM (\n" +
                "select (@i \\:=@i+1) as no,a.* from (\n" +
                "SELECT user_id,SUM(money) as money FROM project WHERE tid=:tid GROUP BY user_id order BY SUM(money) DESC\n" +
                ") a,(select @i \\:=0) b \n" +
                ") c  WHERE c.user_id=:userId",nativeQuery = true)
        Map getUserRanking(@Param("tid") String tid,@Param("userId") String userId);


}
