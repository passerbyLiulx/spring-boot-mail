/*
 Navicat MySQL Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : mail

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 14/10/2020 17:11:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for job_info
-- ----------------------------
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info`  (
  `job_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '定时任务主键',
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务路径',
  `expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表达式',
  `expression_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表达式说明',
  `task_cycle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务周期',
  `run_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行节点',
  `open_state` tinyint(1) NULL DEFAULT NULL COMMENT '开启状态：0关闭 1开启',
  `delete_state` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态 0：未删除 1：删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_info
-- ----------------------------
INSERT INTO `job_info` VALUES ('12345', 'mailTask1', 'com.example.mail.job.MailInfoJob', '0/15 * * * * ?', '每隔15秒执行', NULL, NULL, 1, 0, NULL, NULL);

-- ----------------------------
-- Table structure for mail_info
-- ----------------------------
DROP TABLE IF EXISTS `mail_info`;
CREATE TABLE `mail_info`  (
  `mail_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `mail_from` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人',
  `mail_to` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收人 以逗号间隔',
  `mail_cc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抄送人 以逗号间隔',
  `mail_bcc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密送人 以逗号间隔',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `picture_json` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key：类别Id，value：图片路径',
  `attachment_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件路径 以符号 ”|“ 间隔',
  `template_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `template_json` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key：参数，value：内容',
  `send_date` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `mail_type` tinyint(1) NULL DEFAULT NULL COMMENT '邮件类型 1：简单文本邮件 2：带图片邮件 3：带附件邮件 4：模板邮件',
  `send_state` tinyint(1) NULL DEFAULT NULL COMMENT '发送状态 0：失败 1：成功',
  `delete_state` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态 0：未删除 1：删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`mail_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mail_info
-- ----------------------------
INSERT INTO `mail_info` VALUES ('ab8e8907c26a4ca8865d1a3ca60734f6', '1191026928@qq.com', 'llx1191026928@163.com', NULL, NULL, '泰照护机构运营管理平台使用申请书', '尊敬的客户：\n  您好！感谢您注册使用泰照护机构运营管理平台。\n  由于在申请注册流程中，需上传加盖公章的《泰照护机构运营管理平台使用申请书》原件照片或扫描件，所以请您先下载附件中的《申请书模版》并填写，填写完成后再进行注册。\n\n泰照护机构运营管理平台\n2020-09-22', NULL, 'D:\\项目测试\\泰照护机构运营管理平台使用申请书.pdf|D:\\项目测试\\测试2.txt', NULL, NULL, '2020-10-13 11:41:53', 3, 1, 0, '2020-10-13 11:22:29', '2020-10-13 11:41:53');

SET FOREIGN_KEY_CHECKS = 1;
