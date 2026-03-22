-- =============================================
-- 创建流程动作定义表
-- =============================================

CREATE TABLE IF NOT EXISTS `form_workflow_action` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `action_code`     VARCHAR(50)  NOT NULL COMMENT '动作编码 (如 submit, auditPass)',
    `action_name`     VARCHAR(100) NOT NULL COMMENT '动作显示名称 (如 提交, 审核通过)',
    `icon`            VARCHAR(50)  DEFAULT NULL COMMENT '动作图标 (Ionicons 名称)',
    `button_type`     VARCHAR(20)  DEFAULT 'primary' COMMENT '按钮类型 (primary, info, success, warning, error)',
    `need_remark`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否默认需要填写备注',
    `remark_placeholder` VARCHAR(200) DEFAULT NULL COMMENT '备注框提示语',
    `sort`            INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `description`     VARCHAR(500) DEFAULT NULL COMMENT '动作描述',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_action_code` (`action_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流动作定义表';

-- 初始化预设动作
INSERT INTO `form_workflow_action` (`action_code`, `action_name`, `icon`, `button_type`, `need_remark`, `sort`) VALUES
('submit', '提交', 'CloudUploadOutline', 'primary', 0, 10),
('auditPass', '审核通过', 'CheckmarkCircleOutline', 'success', 0, 20),
('auditReject', '审核驳回', 'CloseCircleOutline', 'error', 1, 30),
('resubmit', '重新提交', 'RefreshOutline', 'primary', 0, 40),
('submitLocal', '递交当地同事', 'PeopleOutline', 'info', 0, 50),
('submitTax', '递交税局', 'BusinessOutline', 'info', 0, 60),
('submitOrg', '递交组织', 'FileTrayFullOutline', 'info', 0, 70),
('complete', '完成', 'CheckmarkDoneOutline', 'success', 0, 80),
('terminate', '终止', 'StopCircleOutline', 'error', 1, 90);
