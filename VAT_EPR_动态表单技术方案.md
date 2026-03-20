# VAT & EPR 动态表单系统技术方案

> 版本：v1.0 | 日期：2026-03-20

---

## 一、技术栈选型

### 服务端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.x | 主框架 |
| Java | 21 | 运行环境 |
| MySQL | 8.0+ | 关系型数据库 |
| MyBatis-Plus | 3.5.x | ORM |
| Jackson | 2.x | JSON序列化 |
| Lombok | latest | 代码简化 |

### 前端
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4.x | 主框架 |
| Vite | 5.x | 构建工具 |
| Element Plus | 2.x | UI 组件库 |
| Vue Draggable | next | 拖拽排序 |
| Pinia | 2.x | 状态管理 |
| Axios | 1.x | HTTP 客户端 |

---

## 二、数据库设计

### 2.1 自定义控件表 `form_control`

```sql
CREATE TABLE `form_control` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `control_name`    VARCHAR(100) NOT NULL COMMENT '控件名称（展示用）',
    `control_key`     VARCHAR(200) NOT NULL COMMENT '控件key，格式: ClassName.fieldName，如 Company.companyName',
    `control_type`    VARCHAR(30)  NOT NULL COMMENT '控件类型: INPUT/SELECT/SWITCH/UPLOAD/TEXTAREA/DATE/NUMBER',
    `placeholder`     VARCHAR(200) DEFAULT NULL COMMENT '占位文本',
    `tips`            VARCHAR(500) DEFAULT NULL COMMENT '控件说明(TIPS)',
    `required`        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '是否必填: 0否 1是',
    `regex_pattern`   VARCHAR(500) DEFAULT NULL COMMENT '正则表达式约束',
    `regex_message`   VARCHAR(200) DEFAULT NULL COMMENT '正则校验失败提示语',
    `min_length`      INT          DEFAULT NULL COMMENT '最小长度',
    `max_length`      INT          DEFAULT NULL COMMENT '最大长度',
    `select_options`  JSON         DEFAULT NULL COMMENT '下拉框选项，格式: [{"label":"xx","value":"xx"}]',
    `upload_config`   JSON         DEFAULT NULL COMMENT '上传文件配置，仅type=UPLOAD时有效，格式: {"maxCount":3,"accept":".pdf,.jpg,.png","maxSizeMB":10}',
    `default_value`   VARCHAR(500) DEFAULT NULL COMMENT '默认值',
    `sort`            INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `enabled`         TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '是否启用: 0否 1是',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_control_key` (`control_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义控件定义表';
```

**control_key 命名规范示例：**
- `Company.companyName` → Company 类的 companyName 字段
- `Company.companyCountry` → Company 类的 companyCountry 字段
- `CompanyLegalPerson.companyLegalName` → CompanyLegalPerson 类的 companyLegalName 字段

---

### 2.2 服务单模板表 `form_template`

```sql
CREATE TABLE `form_template` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
    `template_name`   VARCHAR(100) NOT NULL COMMENT '服务单名称',
    `version`         VARCHAR(20)  NOT NULL DEFAULT '1.0.0' COMMENT '服务单版本',
    `country_code`    VARCHAR(10)  NOT NULL COMMENT '关联国家代码，三位: DEU/FRA/ITA/ESP/POL/CZE/GBR',
    `service_code_l1` VARCHAR(10)  NOT NULL COMMENT '服务类型一级code，如: 01(VAT)/02(EPR)',
    `service_code_l2` VARCHAR(10)  NOT NULL COMMENT '服务类型二级code，如: 0101/0201',
    `service_code_l3` VARCHAR(10)  NOT NULL COMMENT '服务类型三级code，如: 010101',
    `json_schema`     LONGTEXT     NOT NULL COMMENT '画板定义的JSON Schema，描述表单布局与控件引用',
    `status`          TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '状态: 0草稿 1发布',
    `remark`          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务单模板表';
```

**json_schema 结构示例：**
```json
{
  "layout": "grid",
  "columns": 2,
  "rows": [
    {
      "rowIndex": 0,
      "cells": [
        {
          "colSpan": 1,
          "controlId": 1,
          "controlKey": "Company.companyName",
          "controlType": "INPUT",
          "label": "公司名称"
        },
        {
          "colSpan": 1,
          "controlId": 2,
          "controlKey": "Company.companyCountry",
          "controlType": "SELECT",
          "label": "公司所在国"
        }
      ]
    },
    {
      "rowIndex": 1,
      "cells": [
        {
          "colSpan": 2,
          "controlId": 3,
          "controlKey": "CompanyLegalPerson.companyLegalName",
          "controlType": "INPUT",
          "label": "法人姓名"
        }
      ]
    }
  ]
}
```

---

### 2.3 服务单实例表 `form_instance`

```sql
CREATE TABLE `form_instance` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `template_id`     BIGINT       NOT NULL COMMENT '关联的服务单模板ID',
    `template_name`   VARCHAR(100) NOT NULL COMMENT '服务单名称（冗余）',
    `version`         VARCHAR(20)  NOT NULL COMMENT '服务单版本（冗余）',
    `country_code`    VARCHAR(10)  NOT NULL COMMENT '国家代码（冗余）',
    `service_code_l1` VARCHAR(10)  NOT NULL COMMENT '一级服务类型code',
    `service_code_l2` VARCHAR(10)  NOT NULL COMMENT '二级服务类型code',
    `service_code_l3` VARCHAR(10)  NOT NULL COMMENT '三级服务类型code',
    `form_data`       LONGTEXT     NOT NULL COMMENT '表单数据，存储Map<controlKey, value>，JSON格式',
    `status`          TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '状态: 0草稿 1已提交 2已审核',
    `submit_time`     DATETIME     DEFAULT NULL COMMENT '提交时间',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT(1)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务单实例表';
```

**form_data 存储示例：**
```json
{
  "Company.companyName": "测试公司有限公司",
  "Company.companyCountry": "DEU",
  "CompanyLegalPerson.companyLegalName": "张三",
  "CompanyLegalPerson.companyLegalAddress": "北京市朝阳区xx路xx号"
}
```

---

## 三、接口文档

### 3.1 自定义控件 API

#### 3.1.1 创建控件
```
POST /api/form-control
Content-Type: application/json

Request Body:
{
  "controlName": "公司名称",
  "controlKey": "Company.companyName",
  "controlType": "INPUT",
  "placeholder": "请输入公司名称",
  "tips": "请填写营业执照上的完整公司名称",
  "required": true,
  "regexPattern": "^.{2,100}$",
  "regexMessage": "公司名称长度在2-100个字符之间",
  "minLength": 2,
  "maxLength": 100
}

Response:
{
  "code": 0,
  "message": "success",
  "data": { "id": 1 }
}
```

#### 3.1.2 查询控件列表
```
GET /api/form-control/list?controlType=INPUT&keyword=公司&page=1&size=20

Response:
{
  "code": 0,
  "message": "success",
  "data": {
    "total": 10,
    "records": [...]
  }
}
```

#### 3.1.3 更新控件
```
PUT /api/form-control/{id}
```

#### 3.1.4 删除控件
```
DELETE /api/form-control/{id}
```

---

### 3.2 服务单模板 API

#### 3.2.1 创建/保存服务单模板
```
POST /api/form-template
Content-Type: application/json

Request Body:
{
  "templateName": "德国VAT新注册申报",
  "version": "1.0.0",
  "countryCode": "DEU",
  "serviceCodeL1": "01",
  "serviceCodeL2": "0101",
  "serviceCodeL3": "010101",
  "jsonSchema": {
    "layout": "grid",
    "columns": 2,
    "rows": [...]
  },
  "status": 0
}

Response:
{
  "code": 0,
  "message": "success",
  "data": { "id": 1 }
}
```

#### 3.2.2 查询模板列表
```
GET /api/form-template/list?countryCode=DEU&serviceCodeL3=010101&page=1&size=20
```

#### 3.2.3 查询模板详情
```
GET /api/form-template/{id}

Response:
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "templateName": "德国VAT新注册申报",
    "version": "1.0.0",
    "countryCode": "DEU",
    "serviceCodeL1": "01",
    "serviceCodeL2": "0101",
    "serviceCodeL3": "010101",
    "jsonSchema": {...},
    "controlDetails": [
      {
        "controlId": 1,
        "controlKey": "Company.companyName",
        "controlType": "INPUT",
        "controlName": "公司名称",
        "required": true,
        "tips": "...",
        "regexPattern": "...",
        "uploadConfig": null
      }
    ]
  }
}
```

#### 3.2.4 更新模板
```
PUT /api/form-template/{id}
```

#### 3.2.5 发布模板
```
POST /api/form-template/{id}/publish
```

---

### 3.3 服务单实例 API

#### 3.3.1 根据模板创建服务单实例
```
POST /api/form-instance/create
Content-Type: application/json

Request Body:
{
  "templateId": 1
}

Response:
{
  "code": 0,
  "message": "success",
  "data": {
    "instanceId": 100,
    "templateId": 1,
    "templateName": "德国VAT新注册申报",
    "version": "1.0.0",
    "countryCode": "DEU",
    "serviceCodeL3": "010101",
    "jsonSchema": {...},
    "controlDetails": [...],
    "formData": {}
  }
}
```

#### 3.3.2 保存服务单数据（草稿）
```
PUT /api/form-instance/{id}/save
Content-Type: application/json

Request Body:
{
  "formData": {
    "Company.companyName": "测试公司",
    "Company.companyCountry": "DEU",
    "CompanyLegalPerson.companyLegalName": "张三",
    "CompanyLegalPerson.companyLegalAddress": "北京市朝阳区"
  }
}

Response:
{
  "code": 0,
  "message": "success",
  "data": null
}
```

#### 3.3.3 提交服务单
```
POST /api/form-instance/{id}/submit

Response:
{
  "code": 0,
  "message": "success",
  "data": {
    "convertedObjects": {
      "Company": {
        "companyName": "测试公司",
        "companyCountry": "DEU"
      },
      "CompanyLegalPerson": {
        "companyLegalName": "张三",
        "companyLegalAddress": "北京市朝阳区"
      }
    }
  }
}
```

#### 3.3.4 查询服务单实例列表
```
GET /api/form-instance/list?status=0&page=1&size=20
```

---

### 3.4 服务类目 API（透传既存系统）

```
GET /api/service-category/children?parentId=0   # 一级
GET /api/service-category/children?parentId=1   # 二级
GET /api/service-category/children?parentId=3   # 三级
```

---

## 四、核心业务时序逻辑

### 4.1 自定义控件管理时序

```
管理员                     前端                      后端
  |                          |                          |
  |--- 填写控件信息 --------->|                          |
  |                          |--- POST /form-control -->|
  |                          |                          |--- 校验controlKey格式(ClassName.field)
  |                          |                          |--- 校验controlKey唯一性
  |                          |                          |--- 保存 form_control
  |                          |<-- { id: 1 } -----------|
  |<-- 保存成功提示 ----------|                          |
```

### 4.2 服务单模板设计时序

```
管理员            前端画板              后端
  |                  |                   |
  |-- 打开设计器 --->|                   |
  |                  |-- GET /form-control/list -->|
  |                  |<-- 控件列表 -------|
  |<-- 左侧展示控件--|                   |
  |                  |                   |
  |-- 拖拽控件到画板->|                  |
  |-- 配置布局(行列)->|                  |
  |-- 填写模板信息 -->|                  |
  |-- 选择国家+服务 ->|                  |
  |                  |                   |
  |-- 点击保存 ------>|                  |
  |                  |-- POST /form-template -->|
  |                  |    (含jsonSchema) |
  |                  |<-- { id: 1 } -----|
  |<-- 保存成功 ------|                  |
```

### 4.3 创建并填写服务单时序

```
操作员              前端                    后端
  |                   |                       |
  |-- 选择服务单模板 ->|                       |
  |                   |-- POST /form-instance/create -->|
  |                   |      { templateId: 1 }|
  |                   |                       |--- 查询模板详情
  |                   |                       |--- 查询所有关联controlDetail
  |                   |                       |--- 创建instance记录(status=草稿)
  |                   |<-- 返回instanceId + schema + controlDetails--|
  |<-- 动态渲染表单 ---|                       |
  |                   |                       |
  |-- 填写表单数据 --->|                       |
  |-- 点击保存草稿 --->|                       |
  |                   |-- PUT /form-instance/{id}/save -->|
  |                   |    { formData: { "Company.companyName": "xxx", ... } }
  |                   |                       |--- 将formData序列化为JSON存入form_data字段
  |                   |<-- success ------------|
  |<-- 保存成功 -------|                       |
```

### 4.4 服务单提交与对象转换时序

```
操作员              前端                    后端                         业务层
  |                   |                       |                            |
  |-- 点击提交 ------->|                       |                            |
  |                   |-- POST /form-instance/{id}/submit -->|             |
  |                   |                       |--- 查询instance             |
  |                   |                       |--- 解析formData(Map)        |
  |                   |                       |--- 调用FormDataConverter     |
  |                   |                       |       |-- 按className分组key |
  |                   |                       |       |-- 反射赋值目标对象   |
  |                   |                       |       |-- 返回Map<className,Object>
  |                   |                       |--- 打印转换结果日志         |
  |                   |                       |--- 更新status=已提交        |
  |                   |                       |--- 触发后续业务逻辑 ------->|
  |                   |<-- 转换结果 ------------|                            |
  |<-- 提交成功 --------|                       |                            |
```

---

## 五、动态表单渲染与数据存储设计

### 5.1 JSON Schema 结构设计

模板的 `json_schema` 字段定义了表单的完整布局：

```json
{
  "layout": "grid",
  "columns": 2,
  "rows": [
    {
      "rowIndex": 0,
      "cells": [
        {
          "colIndex": 0,
          "colSpan": 1,
          "controlId": 1,
          "controlKey": "Company.companyName",
          "controlType": "INPUT",
          "label": "公司名称"
        },
        {
          "colIndex": 1,
          "colSpan": 1,
          "controlId": 2,
          "controlKey": "Company.companyCountry",
          "controlType": "SELECT",
          "label": "国家"
        }
      ]
    },
    {
      "rowIndex": 1,
      "cells": [
        {
          "colIndex": 0,
          "colSpan": 2,
          "controlId": 5,
          "controlKey": "CompanyLegalPerson.companyLegalAddress",
          "controlType": "TEXTAREA",
          "label": "法人地址"
        }
      ]
    }
  ]
}
```

### 5.2 前端动态渲染流程

```
1. 请求接口获取 instanceId + jsonSchema + controlDetails
2. 将 controlDetails 转为 Map<controlId, ControlConfig>
3. 遍历 jsonSchema.rows → 生成 CSS Grid 布局
4. 每个 cell 根据 controlType 渲染对应组件:
   - INPUT    → <el-input>
   - SELECT   → <el-select>
   - SWITCH   → <el-switch>
   - UPLOAD   → <el-upload>（读取uploadConfig配置maxCount/accept）
   - TEXTAREA → <el-input type="textarea">
   - DATE     → <el-date-picker>
   - NUMBER   → <el-input-number>
5. 校验规则由 controlDetail 中的 regexPattern/required/minLength/maxLength 动态生成
6. 用户输入后，维护一个 formData 对象：{ "Company.companyName": "xxx", ... }
7. 保存时将 formData 原样传给后端
```

**Vue 伪代码示意：**
```vue
<template>
  <div class="dynamic-form">
    <div
      v-for="row in schema.rows"
      :key="row.rowIndex"
      class="form-row"
      :style="{ display: 'grid', gridTemplateColumns: `repeat(${schema.columns}, 1fr)` }"
    >
      <div
        v-for="cell in row.cells"
        :key="cell.controlId"
        :style="{ gridColumn: `span ${cell.colSpan}` }"
      >
        <el-form-item :label="cell.label" :rules="buildRules(cell.controlId)">
          <!-- 根据 controlType 渲染不同组件 -->
          <component
            :is="resolveComponent(cell.controlType)"
            v-model="formData[cell.controlKey]"
            v-bind="resolveProps(cell)"
          />
        </el-form-item>
      </div>
    </div>
  </div>
</template>
```

### 5.3 表单数据存储策略

- **存储格式**：`Map<String, Object>` 序列化为 JSON 字符串存入 `form_instance.form_data`
- **key 命名规范**：`ClassName.fieldName`（与 controlKey 一致）
- **value 类型**：
  - 文本：`String`
  - 开关：`Boolean`
  - 数字：`Number`
  - 文件上传：`List<{ fileName, fileUrl, fileSize }>`
  - 日期：`String`（ISO 8601格式 `yyyy-MM-dd`）

---

## 六、核心服务端代码设计

### 6.1 FormDataConverter（关键核心组件）

```java
/**
 * 表单数据转换器
 * 将 Map<controlKey, value> 转换为对应实体类对象
 * controlKey 格式: ClassName.fieldName
 */
@Slf4j
@Component
public class FormDataConverter {

    // 注册已知实体类（后续可改为扫描注解）
    private static final Map<String, Class<?>> CLASS_REGISTRY = new HashMap<>();

    static {
        CLASS_REGISTRY.put("Company", Company.class);
        CLASS_REGISTRY.put("CompanyLegalPerson", CompanyLegalPerson.class);
        // 注册更多实体类...
    }

    /**
     * 转换表单数据为实体对象Map
     * @param formData Map<"ClassName.fieldName", value>
     * @return Map<ClassName, 实体对象>
     */
    public Map<String, Object> convert(Map<String, Object> formData) {
        // 1. 按 ClassName 分组
        Map<String, Map<String, Object>> grouped = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("\\.", 2);
            if (parts.length != 2) {
                log.warn("Invalid controlKey format: {}, skipping", key);
                continue;
            }
            String className = parts[0];
            String fieldName = parts[1];
            grouped.computeIfAbsent(className, k -> new LinkedHashMap<>())
                   .put(fieldName, entry.getValue());
        }

        // 2. 反射赋值
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : grouped.entrySet()) {
            String className = entry.getKey();
            Class<?> clazz = CLASS_REGISTRY.get(className);
            if (clazz == null) {
                log.warn("No registered class found for: {}", className);
                continue;
            }
            Object instance = createAndPopulate(clazz, entry.getValue());
            result.put(className, instance);
            log.info("Converted [{}]: {}", className, instance);
        }
        return result;
    }

    private Object createAndPopulate(Class<?> clazz, Map<String, Object> fieldMap) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                try {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    // 类型转换
                    Object value = convertValue(field.getType(), entry.getValue());
                    field.set(instance, value);
                } catch (NoSuchFieldException e) {
                    log.warn("Field [{}] not found in class [{}]", entry.getKey(), clazz.getSimpleName());
                }
            }
            return instance;
        } catch (Exception e) {
            log.error("Failed to create instance of {}", clazz.getName(), e);
            throw new RuntimeException("对象转换失败: " + clazz.getSimpleName(), e);
        }
    }

    private Object convertValue(Class<?> targetType, Object value) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;
        String strVal = value.toString();
        if (targetType == String.class)  return strVal;
        if (targetType == Integer.class || targetType == int.class) return Integer.parseInt(strVal);
        if (targetType == Long.class    || targetType == long.class) return Long.parseLong(strVal);
        if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(strVal);
        if (targetType == BigDecimal.class) return new BigDecimal(strVal);
        return value;
    }
}
```

### 6.2 示例实体类

```java
@Data
@NoArgsConstructor
public class Company {
    private String companyName;
    private String companyCountry;
}

@Data
@NoArgsConstructor
public class CompanyLegalPerson {
    private String companyLegalName;
    private String companyLegalAddress;
}
```

### 6.3 提交接口逻辑

```java
@PostMapping("/{id}/submit")
public Result<Map<String, Object>> submit(@PathVariable Long id) {
    FormInstance instance = formInstanceService.getById(id);

    // 1. 解析 formData JSON -> Map
    Map<String, Object> formData = JSON.parseObject(instance.getFormData(),
        new TypeReference<Map<String, Object>>() {});

    // 2. 转换为实体对象
    Map<String, Object> converted = formDataConverter.convert(formData);

    // 3. 打印日志（后续替换为实际业务逻辑）
    converted.forEach((className, obj) ->
        log.info("【表单提交转换结果】{}: {}", className, JSON.toJSONString(obj)));

    // 4. 更新状态
    formInstanceService.updateStatus(id, FormInstanceStatus.SUBMITTED);

    return Result.success(converted);
}
```

---

## 七、服务类目三级联动设计

### 7.1 国家代码枚举

| 代码 | 国家 | 英文 |
|------|------|------|
| DEU  | 德国 | Germany |
| FRA  | 法国 | France |
| ITA  | 意大利 | Italy |
| ESP  | 西班牙 | Spain |
| POL  | 波兰 | Poland |
| CZE  | 捷克 | Czech Republic |
| GBR  | 英国 | United Kingdom |

### 7.2 三级联动交互

```
一级：VAT服务(01) / EPR服务(02)
          ↓
二级：VAT服务(0101) / 包装法(0201) / WEEE法(0202) / ...
          ↓
三级：VAT新注册申报(010101) / VAT转代理申报(010102) / ...
```

前端调用逻辑：
```javascript
// 选中一级后请求二级
const onL1Change = async (code) => {
  const res = await getServiceChildren(l1Selected.id)
  l2Options.value = res.data
  l3Options.value = []
}
// 选中二级后请求三级
const onL2Change = async (code) => {
  const res = await getServiceChildren(l2Selected.id)
  l3Options.value = res.data
}
```

---

## 八、项目结构建议

### 8.1 后端结构
```
genetics-server/
├── src/main/java/com/genetics/
│   ├── GeneticsApplication.java
│   ├── config/
│   │   └── JacksonConfig.java
│   ├── controller/
│   │   ├── FormControlController.java
│   │   ├── FormTemplateController.java
│   │   ├── FormInstanceController.java
│   │   └── ServiceCategoryController.java
│   ├── service/
│   │   ├── FormControlService.java
│   │   ├── FormTemplateService.java
│   │   └── FormInstanceService.java
│   ├── mapper/
│   │   ├── FormControlMapper.java
│   │   ├── FormTemplateMapper.java
│   │   └── FormInstanceMapper.java
│   ├── entity/
│   │   ├── FormControl.java
│   │   ├── FormTemplate.java
│   │   ├── FormInstance.java
│   │   └── domain/           # 业务实体（由表单转换）
│   │       ├── Company.java
│   │       └── CompanyLegalPerson.java
│   ├── converter/
│   │   └── FormDataConverter.java
│   ├── dto/
│   │   ├── FormControlDTO.java
│   │   ├── FormTemplateDTO.java
│   │   └── FormInstanceDTO.java
│   └── common/
│       └── Result.java
└── src/main/resources/
    ├── application.yml
    └── mapper/
```

### 8.2 前端结构
```
genetics-web/
├── src/
│   ├── api/
│   │   ├── formControl.js
│   │   ├── formTemplate.js
│   │   ├── formInstance.js
│   │   └── serviceCategory.js
│   ├── views/
│   │   ├── control/
│   │   │   └── ControlList.vue          # 控件管理
│   │   ├── template/
│   │   │   ├── TemplateList.vue          # 模板列表
│   │   │   └── TemplateDesigner.vue      # 模板设计器（拖拽画板）
│   │   └── instance/
│   │       ├── InstanceList.vue          # 服务单列表
│   │       └── InstanceForm.vue          # 动态表单（渲染+填写）
│   ├── components/
│   │   ├── DynamicForm/
│   │   │   ├── DynamicForm.vue           # 动态表单主组件
│   │   │   ├── controls/
│   │   │   │   ├── InputControl.vue
│   │   │   │   ├── SelectControl.vue
│   │   │   │   ├── SwitchControl.vue
│   │   │   │   ├── UploadControl.vue
│   │   │   │   ├── TextareaControl.vue
│   │   │   │   └── DateControl.vue
│   │   │   └── ControlRenderer.vue       # 控件分发渲染器
│   │   └── FormDesigner/
│   │       ├── FormDesigner.vue          # 画板主组件
│   │       ├── ControlPanel.vue          # 左侧控件列表
│   │       ├── Canvas.vue                # 右侧画板
│   │       └── GridCell.vue              # 网格单元格
│   └── stores/
│       ├── formDesigner.js               # 设计器状态
│       └── formInstance.js               # 实例填写状态
```

---

## 九、关键约束与注意事项

1. **controlKey 唯一性**：数据库层面通过唯一索引保证，后端提交时做格式校验（必须含一个`.`）

2. **模板版本管理**：模板发布后不可修改 jsonSchema，若需变更需升版本号，避免已存在实例的数据错乱

3. **实体类注册**：`FormDataConverter` 中的 `CLASS_REGISTRY` 需要在新增业务实体时手动注册，后续可扩展为通过自定义注解 `@FormEntity` + Spring 扫描自动注册

4. **文件上传**：Upload 类型控件提交时 value 为文件URL列表，需配合文件服务（OSS/MinIO）使用

5. **数据安全**：`form_data` 存储 JSON 时应过滤敏感字段；提交后状态变更为已提交，禁止再次修改

6. **并发控制**：同一服务单实例的保存操作需加乐观锁（version字段）防止并发覆盖
