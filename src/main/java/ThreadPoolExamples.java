import java.util.concurrent.*;

public class ThreadPoolExamples {
    public static void main(String[] args) {
        // 1. 固定大小线程池
        ExecutorService fixedPool = Executors.newFixedThreadPool(5);

        // 2. 单线程线程池
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        // 3. 可缓存线程池
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        // 4. 定时任务线程池
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(3);

        // 5. 自定义线程池（推荐）
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
                2,                      // 核心线程数
                10,                     // 最大线程数
                60L,                    // 空闲线程存活时间
                TimeUnit.SECONDS,       // 时间单位
                new LinkedBlockingQueue<>(100) // 工作队列
        );

        // 提交任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            fixedPool.execute(() -> {
                System.out.println("任务 " + taskId + " 由线程 " +
                        Thread.currentThread().getName() + " 执行");
            });
        }

        // 关闭线程池
        fixedPool.shutdown();
    }
}