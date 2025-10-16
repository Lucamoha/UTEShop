package com.uteshop.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class TmpImageCleanupListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String tmpPath = sce.getServletContext().getRealPath("/uploads/tmp");
        Path tmpDir = Paths.get(tmpPath);

        // Tạo thread định kỳ
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> cleanupOldFiles(tmpDir), 0, 24, TimeUnit.HOURS);

        System.out.println("[TmpImageCleanup] Đã bật dọn ảnh tạm mỗi 24h tại: " + tmpDir);
    }

    private void cleanupOldFiles(Path tmpDir) {
        try {
            if (!Files.exists(tmpDir)) return;

            Instant now = Instant.now();
            long deletedCount = Files.list(tmpDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        try {
                            // Tính tuổi file
                            long ageHours = (now.toEpochMilli() - Files.getLastModifiedTime(path).toMillis()) / 3600000;
                            return ageHours > 24; // Xóa nếu cũ hơn 24h
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .peek(path -> {
                        try {
                            Files.deleteIfExists(path);
                            System.out.println("[TmpImageCleanup] Đã xóa file tạm: " + path.getFileName());
                        } catch (IOException e) {
                            System.err.println("[TmpImageCleanup] Không thể xóa: " + path.getFileName());
                        }
                    })
                    .count();

            if (deletedCount > 0)
                System.out.println("[TmpImageCleanup] Đã dọn " + deletedCount + " ảnh tạm cũ.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}
