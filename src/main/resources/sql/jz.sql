CREATE TABLE `modian_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `goal` double DEFAULT NULL,
  `backer_money` double DEFAULT NULL,
  `progress` double DEFAULT NULL,
  `backer_count` int(11) DEFAULT NULL,
  `status_code` int(11) DEFAULT NULL,
  `subscribe_count` int(11) DEFAULT NULL,
  `logo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_tid` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pay_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `money` double(10,2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_tid` (`tid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5097 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
