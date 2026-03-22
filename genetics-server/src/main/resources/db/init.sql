-- =============================================
-- VAT & EPR 动态表单系统 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS genetics_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE genetics_db;

-- 自定义控件定义表
CREATE TABLE IF NOT EXISTS `form_control` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `control_name`    VARCHAR(100) NOT NULL COMMENT '控件名称（展示用）',
    `control_key`     VARCHAR(200) NOT NULL COMMENT '控件key，格式: ClassName.fieldName',
    `control_type`    VARCHAR(30)  NOT NULL COMMENT '控件类型: INPUT/SELECT/SWITCH/UPLOAD/TEXTAREA/DATE/NUMBER',
    `placeholder`     VARCHAR(200) DEFAULT NULL COMMENT '占位文本',
    `tips`            VARCHAR(500) DEFAULT NULL COMMENT '控件说明(TIPS)',
    `required`        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否必填: 0否 1是',
    `regex_pattern`   VARCHAR(500) DEFAULT NULL COMMENT '正则表达式约束',
    `regex_message`   VARCHAR(200) DEFAULT NULL COMMENT '正则校验失败提示语',
    `min_length`      INT          DEFAULT NULL COMMENT '最小长度',
    `max_length`      INT          DEFAULT NULL COMMENT '最大长度',
    `select_options`  JSON         DEFAULT NULL COMMENT '下拉框选项 [{"label":"xx","value":"xx"}]',
    `upload_config`   JSON         DEFAULT NULL COMMENT '上传配置 {"maxCount":3,"accept":".pdf","maxSizeMB":10}',
    `default_value`   VARCHAR(500) DEFAULT NULL COMMENT '默认值',
    `sort`            INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `enabled`         TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_control_key` (`control_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义控件定义表';

-- 服务单模板表
CREATE TABLE IF NOT EXISTS `form_template` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
    `template_name`   VARCHAR(100) NOT NULL COMMENT '服务单名称',
    `version`         VARCHAR(20)  NOT NULL DEFAULT '1.0.0' COMMENT '服务单版本',
    `country_code`    VARCHAR(10)  NOT NULL COMMENT '国家代码: DEU/FRA/ITA/ESP/POL/CZE/GBR',
    `service_code_l1` VARCHAR(10)  NOT NULL COMMENT '一级服务code',
    `service_code_l2` VARCHAR(10)  NOT NULL COMMENT '二级服务code',
    `service_code_l3` VARCHAR(10)  NOT NULL COMMENT '三级服务code',
    `json_schema`     LONGTEXT     NOT NULL COMMENT '画板定义的JSON Schema',
    `workflow_config` LONGTEXT     DEFAULT NULL COMMENT '工作流配置JSON',
    `status`          TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '状态: 0草稿 1发布',
    `remark`          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务单模板表';

-- 服务单实例表
CREATE TABLE IF NOT EXISTS `form_instance` (
    `id`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `template_id`       BIGINT       NOT NULL COMMENT '关联的服务单模板ID',
    `template_name`     VARCHAR(100) NOT NULL COMMENT '服务单名称（冗余）',
    `version`           VARCHAR(20)  NOT NULL COMMENT '服务单版本（冗余）',
    `country_code`      VARCHAR(10)  NOT NULL COMMENT '国家代码（冗余）',
    `service_code_l1`   VARCHAR(10)  NOT NULL COMMENT '一级服务code',
    `service_code_l2`   VARCHAR(10)  NOT NULL COMMENT '二级服务code',
    `service_code_l3`   VARCHAR(10)  NOT NULL COMMENT '三级服务code',
    `form_data`         LONGTEXT     NOT NULL COMMENT '表单数据 Map<controlKey,value> JSON',
    `status`            TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '状态: 0草稿 1已提交 2已审核',
    `order_status_id`   INT          NOT NULL DEFAULT 10 COMMENT '业务状态ID: 10待提交 20待审核 30待递交 31组织处理 32税局处理 33当地同事处理 40已完成 50已驳回 99已终止',
    `service_start_time` DATETIME    DEFAULT NULL COMMENT '服务开始时间',
    `service_end_time`  DATETIME     DEFAULT NULL COMMENT '服务结束时间',
    `submit_time`       DATETIME     DEFAULT NULL COMMENT '提交时间',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`           TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务单实例表';

-- 示例数据：控件
INSERT INTO `form_control` (`control_name`, `control_key`, `control_type`, `placeholder`, `tips`, `required`, `min_length`, `max_length`, `sort`) VALUES
('公司名称', 'Company.companyName', 'INPUT', '请输入公司名称', '请填写营业执照上的完整公司名称', 1, 2, 100, 1),
('公司所在国', 'Company.companyCountry', 'SELECT', NULL, '请选择公司注册国家', 1, NULL, NULL, 2),
('法人姓名', 'CompanyLegalPerson.companyLegalName', 'INPUT', '请输入法人姓名', '请填写法定代表人姓名', 1, 2, 50, 3),
('法人地址', 'CompanyLegalPerson.companyLegalAddress', 'TEXTAREA', '请输入法人地址', '请填写详细的法人联系地址', 0, NULL, 500, 4);
