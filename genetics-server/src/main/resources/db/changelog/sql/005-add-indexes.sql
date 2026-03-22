-- =============================================
-- 为核心表添加索引以优化查询性能
-- =============================================

-- 1. 表单控件表 (form_control)
-- 优化按控件类型筛选和排序
CREATE INDEX `idx_control_type` ON `form_control` (`control_type`);
-- 优化 listAll 查询 (enabled + sort)
CREATE INDEX `idx_enabled_sort` ON `form_control` (`enabled`, `sort`);
-- 优化模糊查询 (虽然 LIKE '%xxx%' 无法完全利用 B-tree 索引，但前缀搜索或某些场景仍有帮助)
CREATE INDEX `idx_control_name` ON `form_control` (`control_name`);

-- 2. 表单模板表 (form_template)
-- 优化按国家和三级服务筛选
CREATE INDEX `idx_country_service` ON `form_template` (`country_code`, `service_code_l3`);
-- 优化按创建时间倒序排列
CREATE INDEX `idx_create_time` ON `form_template` (`create_time`);

-- 3. 表单实例表 (form_instance)
-- 优化按逻辑状态和业务状态筛选
CREATE INDEX `idx_status_order` ON `form_instance` (`status`, `order_status_id`);
-- 优化按创建时间倒序排列
CREATE INDEX `idx_create_time` ON `form_instance` (`create_time`);
