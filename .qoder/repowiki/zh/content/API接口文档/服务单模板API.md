# 服务单模板API

<cite>
**本文引用的文件**
- [VAT_EPR_动态表单技术方案.md](file://VAT_EPR_动态表单技术方案.md)
</cite>

## 目录
1. [简介](#简介)
2. [项目结构](#项目结构)
3. [核心组件](#核心组件)
4. [架构总览](#架构总览)
5. [详细组件分析](#详细组件分析)
6. [依赖关系分析](#依赖关系分析)
7. [性能考虑](#性能考虑)
8. [故障排查指南](#故障排查指南)
9. [结论](#结论)
10. [附录](#附录)

## 简介
本文件面向服务单模板管理API的完整使用与实现说明，涵盖模板的创建/保存、查询列表、查询详情、更新以及发布等全流程。重点解释以下核心概念：
- JSON Schema 的布局与控件引用机制
- 模板状态管理（草稿/发布）
- 版本控制策略
- 国家代码与服务类型三级联动
- 控件与模板的解耦设计与运行时渲染

同时提供接口规范、请求/响应示例、错误处理建议与最佳实践，帮助开发者快速理解并正确集成。

## 项目结构
该仓库包含一份完整的技术方案文档，其中定义了服务端与前端的模块划分、数据库表结构、接口规范、核心算法与业务流程。后端采用Spring Boot + MyBatis-Plus，前端采用Vue 3 + Element Plus，整体遵循分层架构与职责分离。

```mermaid
graph TB
subgraph "后端(genetics-server)"
A["控制器<br/>FormControlController/FormTemplateController/FormInstanceController/ServiceCategoryController"]
B["服务层<br/>FormControlService/FormTemplateService/FormInstanceService"]
C["数据访问层<br/>FormControlMapper/FormTemplateMapper/FormInstanceMapper"]
D["实体与DTO<br/>FormControl/FormTemplate/FormInstance<br/>FormControlDTO/FormTemplateDTO/FormInstanceDTO"]
E["转换器<br/>FormDataConverter"]
F["通用返回封装<br/>Result"]
end
subgraph "前端(genetics-web)"
G["API模块<br/>formControl.js/formTemplate.js/formInstance.js/serviceCategory.js"]
H["视图与页面<br/>ControlList.vue/TemplateList.vue/TemplateDesigner.vue/InstanceList.vue/InstanceForm.vue"]
I["动态表单组件<br/>DynamicForm.vue/ControlRenderer.vue/controls/*"]
J["设计器组件<br/>FormDesigner.vue/ControlPanel.vue/Canvas.vue/GridCell.vue"]
K["状态管理<br/>formDesigner.js/formInstance.js"]
end
A --> B --> C --> D
B --> E
A --> F
G --> A
H --> G
I --> G
J --> G
K --> G
```

图表来源
- [VAT_EPR_动态表单技术方案.md: 773-852:773-852](file://VAT_EPR_动态表单技术方案.md#L773-L852)

章节来源
- [VAT_EPR_动态表单技术方案.md: 773-852:773-852](file://VAT_EPR_动态表单技术方案.md#L773-L852)

## 核心组件
- 数据模型
  - 自定义控件表：定义控件元数据（名称、类型、校验规则、默认值、上传配置等），用于模板画板拖拽与运行时渲染。
  - 服务单模板表：存储模板名称、版本、国家代码、服务三级编码、JSON Schema布局与控件引用、状态（草稿/发布）。
  - 服务单实例表：基于模板创建的实例，存储表单数据（Map<controlKey, value>序列化）、状态（草稿/已提交/已审核）。
- 核心算法
  - FormDataConverter：将 Map<controlKey, value> 按 ClassName 分组并通过反射转换为业务实体对象，供提交阶段使用。
- 接口体系
  - 自定义控件API：创建、查询列表、更新、删除。
  - 服务单模板API：创建/保存、查询列表、查询详情、更新、发布。
  - 服务单实例API：根据模板创建实例、保存草稿、提交、查询列表。
  - 服务类目API：透传既存系统的一级/二级/三级联动。

章节来源
- [VAT_EPR_动态表单技术方案.md: 31-163:31-163](file://VAT_EPR_动态表单技术方案.md#L31-L163)
- [VAT_EPR_动态表单技术方案.md: 592-728:592-728](file://VAT_EPR_动态表单技术方案.md#L592-L728)

## 架构总览
服务单模板API贯穿“模板设计—模板发布—实例创建—数据提交”的完整链路。模板设计阶段通过控件列表与画板生成JSON Schema；发布后模板不可再修改布局，保障历史实例数据一致性；实例创建时携带模板Schema与控件详情，前端按Schema动态渲染表单；填写完成后保存草稿或提交，提交阶段触发数据转换与状态更新。

```mermaid
sequenceDiagram
participant Admin as "管理员"
participant FE as "前端"
participant API as "后端API"
participant DB as "数据库"
Admin->>FE : 打开模板设计器
FE->>API : GET /api/form-control/list
API-->>FE : 返回控件列表
FE->>FE : 拖拽控件到画板，配置布局
FE->>FE : 填写模板信息(名称/版本/国家/服务三级编码)
FE->>API : POST /api/form-template (含jsonSchema)
API->>DB : 校验唯一性/保存模板(状态=草稿)
DB-->>API : 成功
API-->>FE : 返回模板ID
FE->>Admin : 显示保存成功
Admin->>FE : 选择模板并点击发布
FE->>API : POST /api/form-template/{id}/publish
API->>DB : 更新状态=发布
DB-->>API : 成功
API-->>FE : 返回发布成功
```

图表来源
- [VAT_EPR_动态表单技术方案.md: 415-435:415-435](file://VAT_EPR_动态表单技术方案.md#L415-L435)
- [VAT_EPR_动态表单技术方案.md: 225-302:225-302](file://VAT_EPR_动态表单技术方案.md#L225-L302)

## 详细组件分析

### 服务单模板API接口规范
以下为服务单模板相关接口的HTTP方法、URL路径、请求参数、响应格式、状态码说明与错误处理要点。为避免泄露具体代码，此处不直接展示代码片段，而以“路径+行号”形式标注来源。

- 创建/保存模板
  - 方法与路径：POST /api/form-template
  - 请求体字段（示例字段与含义见下表）
  - 响应：统一返回结构，包含code/message/data
  - 状态码：200 成功；400 参数错误；500 服务器异常
  - 错误处理：校验controlKey唯一性与格式；校验模板版本与国家/服务编码组合唯一性（建议）
  - 来源：[VAT_EPR_动态表单技术方案.md: 227-254:227-254](file://VAT_EPR_动态表单技术方案.md#L227-L254)

- 查询模板列表
  - 方法与路径：GET /api/form-template/list
  - 查询参数：countryCode、serviceCodeL3、page、size
  - 响应：分页结构，包含total与records
  - 状态码：200 成功；400 参数错误；500 服务器异常
  - 错误处理：参数校验与分页边界检查
  - 来源：[VAT_EPR_动态表单技术方案.md: 256-259:256-259](file://VAT_EPR_动态表单技术方案.md#L256-L259)

- 查询模板详情
  - 方法与路径：GET /api/form-template/{id}
  - 路径参数：id
  - 响应：包含模板基础信息与controlDetails（控件详情数组）
  - 状态码：200 成功；404 未找到；500 服务器异常
  - 错误处理：模板不存在、权限校验（如有）
  - 来源：[VAT_EPR_动态表单技术方案.md: 261-292:261-292](file://VAT_EPR_动态表单技术方案.md#L261-L292)

- 更新模板
  - 方法与路径：PUT /api/form-template/{id}
  - 路径参数：id
  - 请求体字段：同创建接口（除状态字段）
  - 状态码：200 成功；400 参数错误；500 服务器异常
  - 错误处理：发布后禁止修改布局（建议）
  - 来源：[VAT_EPR_动态表单技术方案.md: 294-297:294-297](file://VAT_EPR_动态表单技术方案.md#L294-L297)

- 发布模板
  - 方法与路径：POST /api/form-template/{id}/publish
  - 路径参数：id
  - 响应：统一返回结构
  - 状态码：200 成功；400 参数错误；500 服务器异常
  - 错误处理：发布前校验模板状态、必要字段完整性
  - 来源：[VAT_EPR_动态表单技术方案.md: 299-302:299-302](file://VAT_EPR_动态表单技术方案.md#L299-L302)

章节来源
- [VAT_EPR_动态表单技术方案.md: 225-302:225-302](file://VAT_EPR_动态表单技术方案.md#L225-L302)

### JSON Schema 结构设计与控件引用机制
- 布局与网格
  - layout: grid
  - columns: 列数
  - rows: 行数组，每行包含 rowIndex 与 cells
- 单元格与控件
  - cells: 单元格数组，每个单元格包含 colIndex、colSpan、controlId、controlKey、controlType、label
  - controlKey 与 controlType 用于运行时渲染与校验
- 控件详情
  - 控件详情数组（controlDetails）在模板详情接口返回，包含控件名称、类型、必填、正则、提示、上传配置等
- 控件引用与渲染
  - 前端根据 controlType 渲染对应组件，并依据 controlDetails 动态生成校验规则
  - 表单数据以 Map<controlKey, value> 形式存储于实例表的 form_data 字段

```mermaid
erDiagram
FORM_TEMPLATE {
bigint id PK
varchar template_name
varchar version
varchar country_code
varchar service_code_l1
varchar service_code_l2
varchar service_code_l3
longtext json_schema
tinyint status
}
FORM_CONTROL {
bigint id PK
varchar control_name
varchar control_key UK
varchar control_type
varchar placeholder
varchar tips
tinyint required
varchar regex_pattern
varchar regex_message
int min_length
int max_length
json select_options
json upload_config
varchar default_value
int sort
tinyint enabled
}
FORM_INSTANCE {
bigint id PK
bigint template_id FK
varchar template_name
varchar version
varchar country_code
varchar service_code_l1
varchar service_code_l2
varchar service_code_l3
longtext form_data
tinyint status
}
FORM_TEMPLATE ||--o{ FORM_INSTANCE : "引用"
FORM_TEMPLATE ||--o{ FORM_CONTROL : "通过controlKey关联"
```

图表来源
- [VAT_EPR_动态表单技术方案.md: 68-163:68-163](file://VAT_EPR_动态表单技术方案.md#L68-L163)

章节来源
- [VAT_EPR_动态表单技术方案.md: 482-548:482-548](file://VAT_EPR_动态表单技术方案.md#L482-L548)

### 模板状态管理与版本控制
- 状态
  - 草稿：可编辑、可删除
  - 发布：不可修改布局，可作为实例创建的来源
- 版本控制
  - 版本号字段用于区分相同模板的不同版本
  - 发布后若需变更布局，应新建版本，避免影响已有实例
- 业务约束
  - 发布前校验模板完整性
  - 实例创建时携带模板版本，便于回溯与审计

章节来源
- [VAT_EPR_动态表单技术方案.md: 860](file://VAT_EPR_动态表单技术方案.md#L860)

### 国家代码与服务类型三级联动
- 国家代码枚举
  - 支持国家：DEU、FRA、ITA、ESP、POL、CZE、GBR
- 服务类目三级联动
  - 一级：VAT/ EPR
  - 二级：如包装法、WEEE法等
  - 三级：具体业务场景（如新注册申报、转代理申报）
- 前端调用
  - 选中一级后请求二级列表，清空三级
  - 选中二级后请求三级列表

章节来源
- [VAT_EPR_动态表单技术方案.md: 732-770:732-770](file://VAT_EPR_动态表单技术方案.md#L732-L770)

### 提交与对象转换时序
- 提交流程
  - 前端提交实例ID
  - 后端解析 form_data（Map<controlKey, value>）
  - 使用 FormDataConverter 按 ClassName 分组并反射生成实体对象
  - 更新实例状态为已提交
  - 返回转换后的对象映射

```mermaid
sequenceDiagram
participant Operator as "操作员"
participant FE as "前端"
participant API as "后端"
participant Conv as "FormDataConverter"
Operator->>FE : 点击提交
FE->>API : POST /api/form-instance/{id}/submit
API->>API : 解析 form_data(JSON -> Map)
API->>Conv : convert(Map)
Conv-->>API : Map<ClassName, Object>
API->>API : 更新实例状态=已提交
API-->>FE : 返回转换结果
FE-->>Operator : 提交成功
```

图表来源
- [VAT_EPR_动态表单技术方案.md: 460-478:460-478](file://VAT_EPR_动态表单技术方案.md#L460-L478)
- [VAT_EPR_动态表单技术方案.md: 592-728:592-728](file://VAT_EPR_动态表单技术方案.md#L592-L728)

## 依赖关系分析
- 控制器依赖服务层，服务层依赖数据访问层与实体/DTO
- 控件与模板通过 controlKey 解耦，模板只保存引用，运行时通过详情拼装
- 实例依赖模板的 JSON Schema 与控件详情进行动态渲染
- 提交阶段依赖 FormDataConverter 进行数据对象化

```mermaid
classDiagram
class FormTemplateController
class FormTemplateService
class FormTemplateMapper
class FormTemplate
class FormTemplateDTO
class FormInstanceController
class FormInstanceService
class FormInstanceMapper
class FormInstance
class FormInstanceDTO
class FormDataConverter
FormTemplateController --> FormTemplateService : "依赖"
FormTemplateService --> FormTemplateMapper : "依赖"
FormTemplateService --> FormTemplate : "使用"
FormTemplateService --> FormTemplateDTO : "返回"
FormInstanceController --> FormInstanceService : "依赖"
FormInstanceService --> FormInstanceMapper : "依赖"
FormInstanceService --> FormInstance : "使用"
FormInstanceService --> FormInstanceDTO : "返回"
FormInstanceService --> FormDataConverter : "调用"
FormDataConverter --> FormInstance : "生成对象"
```

图表来源
- [VAT_EPR_动态表单技术方案.md: 773-852:773-852](file://VAT_EPR_动态表单技术方案.md#L773-L852)
- [VAT_EPR_动态表单技术方案.md: 592-728:592-728](file://VAT_EPR_动态表单技术方案.md#L592-L728)

章节来源
- [VAT_EPR_动态表单技术方案.md: 773-852:773-852](file://VAT_EPR_动态表单技术方案.md#L773-L852)

## 性能考虑
- 控件列表查询：建议对 controlType、keyword 建立索引，分页大小限制在合理范围
- 模板查询：对国家代码与服务三级编码建立联合索引，支持分页与筛选
- JSON Schema 大小：模板布局尽量简洁，避免过深嵌套与过多控件
- 实例查询：对模板ID、状态、创建时间建立索引，提升筛选效率
- 提交转换：批量提交时注意内存占用，避免一次性处理过大实例

## 故障排查指南
- 控件唯一性校验失败
  - 现象：创建/更新控件时报唯一性错误
  - 排查：确认 controlKey 是否重复，是否符合“ClassName.fieldName”格式
  - 来源：[VAT_EPR_动态表单技术方案.md: 858](file://VAT_EPR_动态表单技术方案.md#L858)
- 模板发布后仍尝试修改布局
  - 现象：更新接口返回失败或被拒绝
  - 排查：发布后的模板应新建版本再修改布局
  - 来源：[VAT_EPR_动态表单技术方案.md: 860](file://VAT_EPR_动态表单技术方案.md#L860)
- 提交转换异常
  - 现象：提交后转换失败或缺少实体类
  - 排查：确认实体类已在转换器注册，字段名与controlKey一致
  - 来源：[VAT_EPR_动态表单技术方案.md: 862](file://VAT_EPR_动态表单技术方案.md#L862)
- 并发覆盖问题
  - 现象：多人同时保存草稿导致数据丢失
  - 排查：实例保存时增加乐观锁（version字段）
  - 来源：[VAT_EPR_动态表单技术方案.md: 869](file://VAT_EPR_动态表单技术方案.md#L869)

章节来源
- [VAT_EPR_动态表单技术方案.md: 856-869:856-869](file://VAT_EPR_动态表单技术方案.md#L856-L869)

## 结论
服务单模板API通过“控件—模板—实例”的三层解耦设计，实现了高度灵活的表单可视化构建与运行时渲染。模板状态与版本控制确保历史数据稳定，JSON Schema 描述布局与控件引用，结合前端动态渲染与后端对象转换，形成完整的业务闭环。建议在生产环境中完善权限控制、并发保护与监控告警，持续优化查询与转换性能。

## 附录
- 请求/响应示例与最佳实践
  - 创建/保存模板：请求体包含模板基础信息与完整 JSON Schema；响应统一返回模板ID
  - 查询列表：支持按国家与服务三级编码筛选；分页参数 page/size
  - 查询详情：返回模板基础信息与控件详情数组，便于前端渲染
  - 更新模板：除状态外均可更新；发布后建议新建版本再修改布局
  - 发布模板：发布前校验完整性；发布后禁止修改布局
  - 提交实例：提交时将 Map<controlKey, value> 传回后端，后端转换为实体对象并更新状态
  - 最佳实践：控件命名规范、版本管理策略、上传控件的文件服务对接、敏感字段过滤与并发控制

章节来源
- [VAT_EPR_动态表单技术方案.md: 225-302:225-302](file://VAT_EPR_动态表单技术方案.md#L225-L302)
- [VAT_EPR_动态表单技术方案.md: 482-548:482-548](file://VAT_EPR_动态表单技术方案.md#L482-L548)
- [VAT_EPR_动态表单技术方案.md: 592-728:592-728](file://VAT_EPR_动态表单技术方案.md#L592-L728)
- [VAT_EPR_动态表单技术方案.md: 856-869:856-869](file://VAT_EPR_动态表单技术方案.md#L856-L869)