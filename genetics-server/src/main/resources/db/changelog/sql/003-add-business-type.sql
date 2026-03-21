-- =============================================
-- 为 form_control 表添加 business_type 字段
-- 用于按业务类型分组筛选控件
-- =============================================

-- 添加 business_type 字段
ALTER TABLE `form_control`
ADD COLUMN `business_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型（实体类名）: Company/CompanyAddress等' AFTER `control_type`;

-- 从 control_key 中提取 business_type 并更新
UPDATE `form_control`
SET `business_type` = SUBSTRING_INDEX(`control_key`, '.', 1)
WHERE `control_key` LIKE '%.%';

-- 添加索引以支持按业务类型查询
CREATE INDEX `idx_business_type` ON `form_control` (`business_type`);
