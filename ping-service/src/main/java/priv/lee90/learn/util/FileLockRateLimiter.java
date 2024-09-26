package priv.lee90.learn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 基于文件锁的限流器
 * @author lee90
 */
public class FileLockRateLimiter {

    private final Logger logger = LoggerFactory.getLogger(FileLockRateLimiter.class);

    private final String lockFilePath;
    private final String stateFilePath;
    private final int maxRequestsPerSecond;

    public FileLockRateLimiter(String lockFilePath, String stateFilePath, int maxRequestsPerSecond) {
        this.lockFilePath = lockFilePath;
        this.stateFilePath = stateFilePath;
        this.maxRequestsPerSecond = maxRequestsPerSecond;

        // 初始化状态文件
        try {
            Path path = Paths.get(stateFilePath);
            if (!Files.exists(path)) {
                Files.write(path, "0,0".getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            logger.error("Error creating state file", e);
        }
    }

    public boolean tryAcquire() {
        try (RandomAccessFile lockFile = new RandomAccessFile(lockFilePath, "rw");
             FileChannel fileChannel = lockFile.getChannel()) {

            // 获取文件锁
            FileLock lock = fileChannel.lock();
            try {
                // 读取状态文件，解析上次请求时间和计数
                String[] state = Files.readAllLines(Paths.get(stateFilePath)).get(0).split(",");
                long lastRequestTime = Long.parseLong(state[0]);
                int requestCount = Integer.parseInt(state[1]);

                // 当前时间的秒数
                long currentTime = System.currentTimeMillis() / 1000;

                // 检查是否处于同一秒内
                if (currentTime == lastRequestTime) {
                    if (requestCount < maxRequestsPerSecond) {
                        requestCount++;
                        updateState(currentTime, requestCount);
                        return true;
                    } else {
                        // 超出每秒的请求次数
                        return false; 
                    }
                } else {
                    // 新的秒开始，重置计数
                    updateState(currentTime, 1);
                    return true;
                }

            } finally {
                // 释放文件锁
                lock.release();
            }

        } catch (IOException e) {
            logger.error("Error acquiring lock", e);
        }
        return false;
    }

    private void updateState(long currentTime, int requestCount) throws IOException {
        String newState = currentTime + "," + requestCount;
        Files.write(Paths.get(stateFilePath), newState.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

}
