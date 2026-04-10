package com.genetics.workflow;

import com.genetics.workflow.action.WorkflowActionPlugin;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工作流插件管理器
 * 负责管理所有工作流动作插件的生命周期
 */
@Slf4j
@Component
public class WorkflowPluginManager {

    private final PluginManager pluginManager;
    private final Map<String, WorkflowActionPlugin> actionRegistry = new ConcurrentHashMap<>();

    public WorkflowPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @PostConstruct
    public void init() {
        log.info("开始加载工作流动作插件...");
        
        // 加载并启动所有插件
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        
        // 注册所有动作插件
        registerAllActions();
        
        log.info("工作流动作插件加载完成, 已注册 {} 个动作", actionRegistry.size());
        actionRegistry.forEach((code, plugin) -> 
                log.info("  - {}: {}", code, plugin.getActionName()));
    }

    @PreDestroy
    public void destroy() {
        log.info("停止工作流动作插件...");
        pluginManager.stopPlugins();
        pluginManager.unloadPlugins();
        actionRegistry.clear();
        log.info("工作流动作插件已停止");
    }

    /**
     * 根据动作编码获取插件
     */
    public WorkflowActionPlugin getActionPlugin(String actionCode) {
        return actionRegistry.get(actionCode);
    }

    /**
     * 获取所有已注册的动作插件
     */
    public List<WorkflowActionPlugin> getAllActions() {
        return new ArrayList<>(actionRegistry.values());
    }

    /**
     * 获取所有动作编码列表
     */
    public List<String> getAllActionCodes() {
        return new ArrayList<>(actionRegistry.keySet());
    }

    /**
     * 重新加载插件(用于热加载)
     */
    public synchronized void reloadPlugin(String pluginPath) {
        log.info("开始重新加载插件: {}", pluginPath);
        
        try {
            Path path = Paths.get(pluginPath);
            String pluginId = pluginManager.loadPlugin(path);
            
            if (pluginId != null) {
                pluginManager.startPlugin(pluginId);
                registerAllActions();
                log.info("插件重新加载成功: {}", pluginId);
            } else {
                throw new RuntimeException("插件加载失败,pluginId 为 null");
            }
        } catch (Exception e) {
            log.error("插件重新加载失败: {}", pluginPath, e);
            throw new RuntimeException("插件重新加载失败: " + e.getMessage(), e);
        }
    }

    /**
     * 注册所有动作插件
     */
    private void registerAllActions() {
        actionRegistry.clear();
        
        List<WorkflowActionPlugin> extensions = pluginManager.getExtensions(WorkflowActionPlugin.class);
        
        for (WorkflowActionPlugin plugin : extensions) {
            String actionCode = plugin.getActionCode();
            actionRegistry.put(actionCode, plugin);
            log.debug("注册动作插件: {} -> {}", actionCode, plugin.getActionName());
        }
    }
}
