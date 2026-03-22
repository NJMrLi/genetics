-- 为 form_template 表添加 workflow_config 字段
ALTER TABLE `form_template`
    ADD COLUMN `workflow_config` LONGTEXT DEFAULT NULL COMMENT '工作流配置JSON' AFTER `json_schema`;
