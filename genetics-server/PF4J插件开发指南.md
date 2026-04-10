# PF4J 工作流动作插件开发指南

## 概述

本系统使用 PF4J 框架实现了工作流动作的插件化架构,支持运行时热插拔。所有工作流动作(如提交、审核、驳回等)都被实现为独立插件。

## 架构说明

### 核心组件

```
┌─────────────────────────────────────────┐
│   FormInstanceController                │
│   POST /api/form-instance/{id}/execute  │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│   WorkflowActionDispatcher              │
│   - 查找插件                            │
│   - 构建上下文                          │
│   - 校验流转规则                        │
│   - 执行插件                            │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│   WorkflowPluginManager                 │
│   - 管理插件生命周期                    │
│   - 注册动作插件                        │
│   - 热加载新插件                        │
└──────────────┬──────────────────────────┘
               │
       ┌───────┴───────┐
       ▼               ▼
  SubmitPlugin    AuditPlugin    ... (可扩展)
```

### 目录结构

```
genetics-server/
├── src/main/java/com/genetics/
│   ├── workflow/
│   │   ├── action/                          # 插件接口和核心类
│   │   │   ├── WorkflowActionPlugin.java    # 插件接口
│   │   │   ├── WorkflowActionContext.java   # 执行上下文
│   │   │   ├── WorkflowActionResult.java    # 执行结果
│   │   │   └── AbstractWorkflowAction.java  # 抽象基类
│   │   ├── actions/                         # 内置动作插件
│   │   │   ├── SubmitActionPlugin.java
│   │   │   ├── AuditPassActionPlugin.java
│   │   │   ├── AuditRejectActionPlugin.java
│   │   │   └── ResubmitActionPlugin.java
│   │   ├── WorkflowPluginManager.java       # 插件管理器
│   │   ├── WorkflowActionDispatcher.java    # 动作分发器
│   │   └── PluginFileWatcher.java           # 文件监听器(热加载)
│   └── config/
│       └── WorkflowPluginConfig.java        # PF4J 配置
├── src/main/resources/
│   └── extensions.idx                       # 插件扩展索引
└── plugins/                                 # 外部插件目录(热加载)
```

## 如何开发新插件

### 步骤 1: 创建插件类

在 `com.genetics.workflow.actions` 包下创建新类,继承 `AbstractWorkflowAction`:

```java
package com.genetics.workflow.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

@Slf4j
@Extension  // 必须添加此注解
public class MyCustomActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "myCustomAction";  // 动作编码,唯一标识
    }
    
    @Override
    public String getActionName() {
        return "我的自定义动作";  // 动作显示名称
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 1. 获取服务单实例
        Long instanceId = context.getInstance().getId();
        
        // 2. 获取备注
        String remark = context.getRemark();
        
        // 3. 获取表单数据
        Map<String, Object> formData = context.getFormData();
        
        // 4. 执行业务逻辑
        log.info("执行自定义动作: {}", instanceId);
        
        // 5. 返回结果
        return WorkflowActionResult.success(
            context.getTransition().getTo(),  // 新状态
            "执行成功"                        // 消息
        );
    }
    
    @Override
    public boolean needRemark() {
        return true;  // 是否需要备注
    }
    
    @Override
    public void validate(WorkflowActionContext context) {
        // 自定义校验逻辑
        if (/* 校验失败 */) {
            throw new IllegalArgumentException("校验失败原因");
        }
    }
}
```

### 步骤 2: 注册插件

在 `src/main/resources/extensions.idx` 文件中添加插件全限定名:

```
com.genetics.workflow.actions.SubmitActionPlugin
com.genetics.workflow.actions.AuditPassActionPlugin
com.genetics.workflow.actions.AuditRejectActionPlugin
com.genetics.workflow.actions.ResubmitActionPlugin
com.genetics.workflow.actions.MyCustomActionPlugin  # 添加这一行
```

### 步骤 3: 配置工作流

在模板的工作流配置中添加新的流转规则:

```json
{
  "transitions": [
    {
      "from": 20,
      "to": 30,
      "action": "myCustomAction",
      "actionName": "我的自定义动作",
      "needRemark": true,
      "condition": null
    }
  ]
}
```

### 步骤 4: 重启应用

重启应用后,新插件会自动加载并可用。

## 如何创建外部插件(热加载)

### 步骤 1: 创建独立 Maven 项目

```bash
mkdir workflow-action-custom
cd workflow-action-custom
mvn archetype:generate -DgroupId=com.genetics.actions -DartifactId=custom-action -DarchetypeArtifactId=maven-archetype-quickstart
```

### 步骤 2: 配置 pom.xml

```xml
<dependencies>
    <!-- 引用主项目的插件接口 -->
    <dependency>
        <groupId>com.genetics</groupId>
        <artifactId>genetics-server</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <dependency>
        <groupId>org.pf4j</groupId>
        <artifactId>pf4j</artifactId>
        <version>3.10.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <archive>
                    <manifestEntries>
                        <Plugin-Id>custom-action</Plugin-Id>
                        <Plugin-Version>1.0.0</Plugin-Version>
                        <Plugin-Provider>GeneticsTeam</Plugin-Provider>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 步骤 3: 实现插件

```java
package com.genetics.actions;

import com.genetics.workflow.action.AbstractWorkflowAction;
import com.genetics.workflow.action.WorkflowActionContext;
import com.genetics.workflow.action.WorkflowActionResult;
import org.pf4j.Extension;

@Extension
public class CustomActionPlugin extends AbstractWorkflowAction {
    
    @Override
    public String getActionCode() {
        return "customAction";
    }
    
    @Override
    public String getActionName() {
        return "自定义外部插件";
    }
    
    @Override
    protected WorkflowActionResult doExecute(WorkflowActionContext context) {
        // 实现业务逻辑
        return WorkflowActionResult.success(
            context.getTransition().getTo(),
            "外部插件执行成功"
        );
    }
}
```

### 步骤 4: 创建 extensions.idx

在 `src/main/resources/extensions.idx` 中添加:

```
com.genetics.actions.CustomActionPlugin
```

### 步骤 5: 编译并部署

```bash
mvn clean package
cp target/custom-action-1.0.0.jar /path/to/genetics-server/plugins/
```

### 步骤 6: 热加载

系统会自动检测 `plugins/` 目录的变化并加载新插件,无需重启应用。

查看日志确认加载成功:

```
检测到插件变化: ENTRY_CREATE - custom-action-1.0.0.jar
开始重新加载插件: /path/to/plugins/custom-action-1.0.0.jar
插件重新加载成功: custom-action
注册动作插件: customAction -> 自定义外部插件
```

## API 调用示例

### 执行动作

```bash
POST /api/form-instance/123/execute
Content-Type: application/json

{
  "action": "submit",
  "remark": "提交审核",
  "actionFormData": {
    "extra_field": "value"
  }
}
```

### 响应

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "success": true,
    "newStatus": 20,
    "message": "提交成功",
    "data": {
      "Company": {
        "companyName": "测试公司"
      }
    }
  }
}
```

## 内置动作列表

| 动作编码 | 动作名称 | 说明 | 需要备注 |
|---------|---------|------|---------|
| submit | 提交 | 将表单数据转换为业务实体并提交 | 否 |
| auditPass | 审核通过 | 审核通过,流转到下一状态 | 否 |
| auditReject | 审核驳回 | 驳回并重置为草稿状态 | 是 |
| resubmit | 重新提交 | 驳回后重新提交 | 否 |

## 注意事项

### 1. 插件接口必须在主应用中
插件接口(`WorkflowActionPlugin`)和核心类必须在主应用中,不能放在外部插件中。

### 2. 依赖注入
PF4J 插件不受 Spring 管理,需要通过 `WorkflowActionContext` 传递 Spring Bean:

```java
FormDataConverter converter = context.getFormDataConverter();
ObjectMapper mapper = context.getObjectMapper();
```

### 3. 事务管理
`WorkflowActionDispatcher.dispatch()` 方法已添加 `@Transactional` 注解,插件执行在事务中。

### 4. 异常处理
插件中的异常会被统一捕获并转换为 `BusinessException`:

```java
try {
    return plugin.execute(context);
} catch (Exception e) {
    throw new BusinessException("动作执行失败: " + e.getMessage());
}
```

### 5. 热加载限制
- 只支持新增插件,不支持修改已有插件
- 修改已有插件需要重启应用
- 插件文件必须是 `.jar` 格式

## 调试技巧

### 查看已加载的插件

在启动日志中查看:

```
开始加载工作流动作插件...
工作流动作插件加载完成, 已注册 4 个动作
  - submit: 提交
  - auditPass: 审核通过
  - auditReject: 审核驳回
  - resubmit: 重新提交
```

### 启用 PF4J 调试日志

在 `application.yml` 中设置:

```yaml
logging:
  level:
    org.pf4j: debug
```

### 测试插件

```bash
# 1. 查看可用动作
GET /api/workflow/actions/list

# 2. 执行动作
POST /api/form-instance/1/execute
{ "action": "submit" }
```

## 常见问题

### Q: 插件没有被加载?
A: 检查以下几点:
1. `extensions.idx` 文件是否正确
2. 插件类是否添加了 `@Extension` 注解
3. 插件类是否实现了 `WorkflowActionPlugin` 接口
4. 查看启动日志是否有错误

### Q: 热加载不生效?
A: 检查:
1. 插件文件是否在 `plugins/` 目录下
2. 文件扩展名是否为 `.jar`
3. 查看日志是否有 "检测到插件变化" 的输出

### Q: 插件执行失败?
A: 检查:
1. 工作流配置中是否添加了相应的流转规则
2. 当前状态是否允许执行此动作
3. 插件的 `validate()` 方法是否通过

## 最佳实践

1. **单一职责**: 每个插件只负责一个动作的逻辑
2. **充分校验**: 在 `validate()` 方法中进行参数校验
3. **详细日志**: 记录关键步骤的日志,便于排查问题
4. **异常处理**: 抛出明确的异常信息,不要吞掉异常
5. **测试覆盖**: 为每个插件编写单元测试

## 参考资源

- [PF4J 官方文档](https://pf4j.org/)
- [PF4J GitHub](https://github.com/pf4j/pf4j)
- 项目代码: `genetics-server/src/main/java/com/genetics/workflow/`
