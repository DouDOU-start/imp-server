/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 127.0.0.1:3366
 Source Schema         : IMP

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 21/06/2023 16:32:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for body_part
-- ----------------------------
CREATE TABLE IF NOT EXISTS `body_part` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `body_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身体检查部位名称',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `body_name` (`body_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='身体部位表';

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
CREATE TABLE IF NOT EXISTS `dictionary`(
  `id`         bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `name`       varchar(50)                                                   NOT NULL COMMENT '字典名',
  `value`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '属性（用英文逗号分隔）',
  `created_at` timestamp                                                     NOT NULL COMMENT '创建时间',
  `updated_at` timestamp                                                     NOT NULL COMMENT '修改时间',
  `creator`    bigint                                                        NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater`    bigint                                                        NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
-- Table structure for human_organ
-- ----------------------------
CREATE TABLE IF NOT EXISTS `human_organ` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `organ_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '器官名称',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `scan_type_name` (`organ_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='器官表';

-- ----------------------------
-- Table structure for label_organ
-- ----------------------------
CREATE TABLE IF NOT EXISTS `label_organ`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT COMMENT '自增 id',
    `label_id`   bigint    NOT NULL COMMENT '标签 id',
    `organ_id`   bigint    NOT NULL COMMENT '器官 id',
    `created_at` timestamp NOT NULL COMMENT '创建时间',
    `updated_at` timestamp NOT NULL COMMENT '修改时间',
    `creator`    bigint    NOT NULL DEFAULT '-1' COMMENT '创建人',
    `updater`    bigint    NOT NULL DEFAULT '-1' COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `label_id` (`label_id`, `organ_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='标签对应器官表';

-- ----------------------------
-- Table structure for series_body_part
-- ----------------------------
CREATE TABLE IF NOT EXISTS `series_body_part`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT COMMENT '自增 id',
    `series_id`    bigint    NOT NULL COMMENT '系列 id',
    `body_part_id` bigint    NOT NULL COMMENT '身体检查部位 id',
    `created_at`   timestamp NOT NULL COMMENT '创建时间',
    `updated_at`   timestamp NOT NULL COMMENT '修改时间',
    `creator`      bigint    NOT NULL DEFAULT '-1' COMMENT '创建人',
    `updater`      bigint    NOT NULL DEFAULT '-1' COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `series_id` (`series_id`, `body_part_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='系列对应身体部位表';

-- ----------------------------
-- Table structure for series_scan_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `series_scan_type`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT COMMENT '自增 id',
    `series_id`    bigint    NOT NULL COMMENT '系列 id',
    `scan_type_id` bigint    NOT NULL COMMENT '扫描类型 id',
    `created_at`   timestamp NOT NULL COMMENT '创建时间',
    `updated_at`   timestamp NOT NULL COMMENT '修改时间',
    `creator`      bigint    NOT NULL DEFAULT '-1' COMMENT '创建人',
    `updater`      bigint    NOT NULL DEFAULT '-1' COMMENT '修改人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `series_id` (`series_id`, `scan_type_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT ='系列对应扫描类型表';

-- ----------------------------
-- Table structure for image_instances
-- ----------------------------
CREATE TABLE IF NOT EXISTS `image_instances`
(
  `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `instance_number` int                                                           NOT NULL COMMENT '实例号',
  `instance_uid`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例 uid',
  `slice_location`  double                                                        NOT NULL COMMENT '切片位置',
  `instance_at` timestamp NULL DEFAULT NULL COMMENT '实例创建时间',
  `series_id` bigint NOT NULL COMMENT '系列 id',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='实例表';

-- ----------------------------
-- Table structure for image_label
-- ----------------------------
CREATE TABLE IF NOT EXISTS `image_label` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `file_name` varchar(50) NOT NULL COMMENT '文件名',
  `file_location` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件路径',
  `series_id` bigint NOT NULL COMMENT '系列 id',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='标注文件表';

-- ----------------------------
-- Table structure for image_scan_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `image_scan_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `scan_type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扫描类型名称',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `scan_type_name` (`scan_type_name`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='扫描类型表';

-- ----------------------------
-- Table structure for image_series
-- ----------------------------
CREATE TABLE IF NOT EXISTS `image_series` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `series_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '系列号（单个设备唯一）',
  `series_uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '系列uid',
  `series_description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '系列描述',
  `modality` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '模态',
  `pixel_spacing` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '像素间距',
  `slice_thickness` double NOT NULL COMMENT '切片厚度',
  `row` int NOT NULL COMMENT '行',
  `columns` int NOT NULL COMMENT '列',
  `instance_num` int NOT NULL DEFAULT '0' COMMENT '实例数',
  `patient_age` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '-1' COMMENT '影像患者年龄',
  `series_at` timestamp NULL DEFAULT NULL COMMENT '系列创建时间',
  `study_id` bigint NOT NULL COMMENT '研究 id',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `series_uid` (`series_uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系列表';

-- ----------------------------
-- Table structure for image_studies
-- ----------------------------
CREATE TABLE IF NOT EXISTS `image_studies` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `study_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '研究 id',
  `study_uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '研究 uid',
  `study_description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '研究描述',
  `study_at` timestamp NULL DEFAULT NULL COMMENT '研究时间',
  `patient_id` bigint NOT NULL COMMENT '患者 id',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `study_uid` (`study_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='研究表';

-- ----------------------------
-- Table structure for institution
-- ----------------------------
CREATE TABLE IF NOT EXISTS `institution` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `institution_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构名称',
  `institution_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构地址',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `institution_tb_UN_id` (`id`) USING BTREE,
  UNIQUE KEY `institution_tb_UN_name` (`institution_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='机构表';

-- ----------------------------
-- Table structure for institution_patient
-- ----------------------------
CREATE TABLE IF NOT EXISTS `institution_patient` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增 id',
  `patient_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '患者 id',
  `patient_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '患者姓名',
  `patient_sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '患者性别',
  `institution_id` bigint unsigned NOT NULL COMMENT '所属机构 id',
  `created_at` timestamp NOT NULL COMMENT '创建时间',
  `updated_at` timestamp NOT NULL COMMENT '修改时间',
  `creator` bigint NOT NULL DEFAULT '-1' COMMENT '创建人',
  `updater` bigint NOT NULL DEFAULT '-1' COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='患者表';

SET FOREIGN_KEY_CHECKS = 1;
