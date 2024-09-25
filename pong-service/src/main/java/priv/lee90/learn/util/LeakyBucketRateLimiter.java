package priv.lee90.learn.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 漏桶限流（单机版本实现）
 *
 * @author lee90
 * @date 2024/09/23
 */
public class LeakyBucketRateLimiter {

    /**
     * 桶的容量（即最大积压的请求数）
     */
    private final int capacity;
    /**
     * 请求的最小间隔时间（1秒）
     */
    private final long interval;
    /**
     * 上次漏桶漏出请求的时间
     */
    private long lastLeakTime;
    /**
     * 当前桶中请求数（代表当前积压的请求）
     */
    private AtomicInteger water;

    /**
     * @param capacity  桶的容量
     * @param interval  漏桶的间隔时间
     */
    public LeakyBucketRateLimiter(int capacity, long interval) {
        this.capacity = capacity;
        this.interval = interval;
        // 初始化桶中水为0
        this.water = new AtomicInteger(0);
        // 初始化为当前时间
        this.lastLeakTime = System.currentTimeMillis();
    }

    /**
     * 请求是否被允许
     */
    public synchronized boolean isAllowed() {
        long currentTime = System.currentTimeMillis();

        // 计算时间差，判断是否需要漏掉一个请求
        long elapsedTime = currentTime - lastLeakTime;
        if (elapsedTime >= interval) {
            // 如果超过了时间间隔，漏出请求并更新最后的漏桶时间
            // 漏出的次数
            int leaks = (int) (elapsedTime / interval);
            // 最新水位
            int newWaterLevel = water.get() - leaks;
            // 更新水位
            water.set(Math.max(0, newWaterLevel));
            // 更新最后漏桶时间
            lastLeakTime = currentTime;
        }

        // 如果桶未满，允许请求，并增加水位
        if (water.get() < capacity) {
            water.incrementAndGet();
            return true;
        }

        // 如果桶满了，拒绝请求
        return false;
    }

}
