## 机器人
1. 项目机器人使用 酷Q机器人 ，官网地址 https://cqp.cc/forum.php    
2. 项目使用HTTP接口进行开发，参考文档https://cqhttp.cc/docs/4.10/#/

## 使用修改地方
1. 修改新人进群欢迎语(com\jack\qqrebot\jst\GroupNoticeService.java#groupIncreaseNotice)
2. 修改摩点集资播报语(com\jack\qqrebot\jst\GroupNoticeService.java#updateData)
3. 修改晚安语(com.jack.qqrebot.jst.wanan.WananService.getResult)
4. 在application.properties里面修改QQ机器人对应的QQ
5. 在application.properties里面修改需要监控的摩点用户id（应援会的摩点账号id）
6. 修改机器人所在服务器的ip（com.jack.qqrebot.utils.SendMsgUtils）
7. 修改获取集资链接语（com.jack.qqrebot.jst.GroupNoticeService.getJz）

## 数据库
1. 数据库在application.properties里面指定
2. 数据库文件在resources/sql下面

## 酷Q机器人插件说明
1. 插件在resources/plugin下面
2. 将插件放入机器人的app目录下面。
3. 详细参考 https://cqhttp.cc/docs/4.10/#/

