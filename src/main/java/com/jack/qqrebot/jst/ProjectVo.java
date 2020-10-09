package com.jack.qqrebot.jst;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: mujj
 * @Date: 2019/6/28 21:55
 * @Description:
 * @Version: 1.0
 */

@Entity
@Table(name = "project")
public class ProjectVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pid")
    private String pid;

    @Column(name = "tid")
    private String tid;

    @Column(name = "pay_id")
    private String payId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String username;

    @Column(name = "money")
    private double money;

    @Column(name = "create_date")
    private Date createDate;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    @Override
    public String toString() {
        return "ProjectVo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", money=" + money +
                ", createDate=" + createDate +
                '}';
    }
}
