package com.genetics.config;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginManager;
import org.pf4j.DefaultPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * PF4J 插件配置
 */
@Slf4j
@Configuration
public class WorkflowPluginConfig {

    @Value("${workflow.plugin.dir:plugins}")
    private String pluginsDir;

    @Bean
    public PluginManager pluginManager() {
        Path path = Paths.get(pluginsDir);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("创建插件目录: {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("创建插件目录失败: {}", path.toAbsolutePath(), e);
        }
        
        log.info("初始化 PF4J PluginManager, 插件目录: {}", path.toAbsolutePath());
        return new DefaultPluginManager(path);
    }
}
