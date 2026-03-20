# VAT & EPR 动态表单系统

基于 Spring Boot 3.2 + Vue 3 的动态表单系统，支持自定义控件、拖拽式表单设计、服务单全生命周期管理。

## 技术栈

| 层次 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.2 / Java 21 |
| ORM | MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8.0+ |
| 接口文档 | springdoc-openapi 2.4 (Swagger UI) |
| 前端框架 | Vue 3 + Vite 5 |
| UI 组件库 | Element Plus |
| 状态管理 | Pinia |
| 拖拽 | vuedraggable |

## 项目结构

```
genetics/
├── genetics-server/          # 后端 Spring Boot 工程
│   ├── src/main/java/com/genetics/
│   │   ├── config/           # 配置（MyBatis-Plus、Swagger、CORS）
│   │   ├── controller/       # REST 接口
│   │   ├── converter/        # FormDataConverter（表单数据 → 实体）
│   │   ├── dto/              # 请求 / 响应 DTO & VO
│   │   ├── entity/           # 数据库实体
│   │   ├── enums/            # 枚举（控件类型、国家代码、业务状态）
│   │   ├── mapper/           # MyBatis-Plus Mapper
│   │   └── service/          # 业务逻辑
│   └── src/main/resources/
│       ├── application.yml   # 配置文件
│       └── db/init.sql       # 数据库初始化脚本
└── genetics-web/             # 前端 Vue 3 工程
    └── src/
        ├── api/              # Axios 接口封装
        ├── components/
        │   ├── DynamicForm/  # 动态表单渲染引擎
        │   └── FormDesigner/ # 拖拽式表单设计器
        ├── stores/           # Pinia 状态管理
        └── views/            # 页面
            ├── control/      # 控件管理
            ├── template/     # 模板管理 & 设计器
            └── instance/     # 服务单列表 & 填写
```

## 快速开始

### 1. 初始化数据库

在 MySQL 中执行初始化脚本：

```bash
mysql -u root -p < genetics-server/src/main/resources/db/init.sql
```

### 2. 配置数据库连接

修改 `genetics-server/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/genetics_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
```

### 3. 启动后端

```bash
cd genetics-server
mvn spring-boot:run
```

后端启动后访问：
- 接口文档（Swagger UI）：http://localhost:8080/swagger-ui.html

### 4. 启动前端

```bash
cd genetics-web
npm install
npm run dev
```

前端启动后访问：http://localhost:5173

## 核心功能

### 自定义控件
支持 7 种控件类型：`INPUT` / `TEXTAREA` / `NUMBER` / `SELECT` / `SWITCH` / `DATE` / `UPLOAD`

每个控件配置：
- **controlKey**：格式为 `ClassName.fieldName`（如 `Company.companyName`），用于提交时反射映射到业务实体
- 校验规则：必填、最小/最大长度、正则
- 下拉选项、上传配置、说明 TIPS

### 服务单模板（表单设计器）
- 左侧控件面板 + 右侧网格画板
- 拖拽控件到画板，支持 1~4 列布局，每个控件可调整跨列
- 三级联动服务类型（VAT / EPR）
- 国家代码：DEU / FRA / ITA / ESP / POL / CZE / GBR
- 保存为 JSON Schema，版本管理，发布/草稿状态

### 服务单实例
- 根据已发布模板创建服务单
- 渲染动态表单并实时保存草稿
- 业务状态流转：

  | statusId | 名称 |
  |----------|------|
  | 10 | 待提交 |
  | 20 | 待审核 |
  | 30 | 待递交 |
  | 31 | 组织处理 |
  | 32 | 税局处理 |
  | 33 | 当地同事处理 |
  | 40 | 已完成 |
  | 50 | 已驳回 |
  | 99 | 已终止 |

- 服务开始时间 / 服务结束时间
- 提交时将表单数据（`Map<controlKey, value>`）通过反射转换为业务实体对象

### FormDataConverter
提交时的核心转换逻辑：

```
{"Company.companyName": "测试公司", "CompanyLegalPerson.companyLegalName": "张三"}
         ↓
{ Company: {companyName: "测试公司"}, CompanyLegalPerson: {companyLegalName: "张三"} }
```

按 `controlKey` 中的 `ClassName` 分组，通过反射创建对应实体并赋值。

## API 接口

| 模块 | 接口前缀 |
|------|---------|
| 自定义控件 | `GET/POST/PUT/DELETE /api/form-control` |
| 服务单模板 | `GET/POST/PUT /api/form-template` |
| 服务单实例 | `GET/POST/PUT /api/form-instance` |
| 基础数据 | `GET /api/basic` |

完整接口文档见 Swagger UI：http://localhost:8080/swagger-ui.html
