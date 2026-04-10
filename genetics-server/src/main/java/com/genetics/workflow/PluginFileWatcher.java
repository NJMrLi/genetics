package com.genetics.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 插件文件监听器
 * 监听 plugins/ 目录变化,实现热加载
 */
@Slf4j
@Component
public class PluginFileWatcher {

    private final WorkflowPluginManager pluginManager;
    
    @Value("${workflow.plugin.dir:plugins}")
    private String pluginsDir;

    private WatchService watchService;
    private ExecutorService executorService;
    private volatile boolean running = false;

    public PluginFileWatcher(WorkflowPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @PostConstruct
    public void start() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(pluginsDir);
            
            // 注册监听事件
            path.register(watchService, 
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            
            // 启动后台线程
            running = true;
            executorService = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r, "plugin-watcher");
                thread.setDaemon(true);
                return thread;
            });
            executorService.submit(this::watchLoop);
            
            log.info("插件文件监听器已启动, 监听目录: {}", path.toAbsolutePath());
            
        } catch (IOException e) {
            log.error("启动插件文件监听器失败", e);
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (executorService != null) {
            executorService.shutdown();
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("关闭 WatchService 失败", e);
            }
        }
        log.info("插件文件监听器已停止");
    }

    private void watchLoop() {
        while (running) {
            try {
                WatchKey key = watchService.take();
                
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    
                    // 忽略 OVERFLOW 事件
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    
                    Path changed = (Path) event.context();
                    String fileName = changed.toString();
                    
                    // 只处理 .jar 文件
                    if (fileName.endsWith(".jar")) {
                        log.info("检测到插件变化: {} - {}", kind.name(), fileName);
                        
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE || 
                            kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            // 延迟一点等待文件复制完成
                            Thread.sleep(500);
                            pluginManager.reloadPlugin(Paths.get(pluginsDir, fileName).toString());
                        }
                    }
                }
                
                // 重置 key
                boolean valid = key.reset();
                if (!valid) {
                    log.warn("WatchKey 失效, 停止监听");
                    break;
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("插件监听线程被中断");
                break;
            } catch (Exception e) {
                log.error("处理文件变化事件失败", e);
            }
        }
    }
}
