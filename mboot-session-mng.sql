/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : mboot

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 26/06/20120 23:53:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `del_flag` int(11) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cost_time` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `ip_info` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `request_param` longtext,
  `request_type` varchar(255) DEFAULT NULL,
  `request_url` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `log_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------
BEGIN;
INSERT INTO `log` VALUES (1, 'system', '2020-06-26 23:35:33', 0, 'system', '2020-06-26 23:35:33', 3720, '127.0.0.1', NULL, 'User login', '{\"password\":\"You cant see me\",\"username\":\"mboot\"}', 'POST', '/doLogin', NULL, 1);
INSERT INTO `log` VALUES (2, 'system', '2020-06-26 23:35:39', 0, 'system', '2020-06-26 23:35:39', 2202, '127.0.0.1', NULL, 'Query user list', '{}', 'GET', '/user', 'mboot', 0);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(2) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_username_uq` (`username`) USING BTREE COMMENT 'Username, unique index',
  UNIQUE KEY `user_email_uq` (`email`) USING BTREE COMMENT 'Email, unique index',
  KEY `user_created_time` (`created_time`) USING BTREE COMMENT 'Creation time, normal index'
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 0, '', 'Words', 'salahsayedatwa@gmail.com', 'mboot', '123456', '2020-06-26 15:04:09', 'system', '2019-06-26 15:04:16', 'system');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
