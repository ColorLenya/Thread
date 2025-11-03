import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// 生产环境推荐写法
public class RecommendedThreadUsage {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void processTasks(List<Runnable> tasks) {
        for (Runnable task : tasks) {
            executor.execute(task);
        }
    }

    public <T> List<T> processCallableTasks(List<Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        List<Future<T>> futures = executor.invokeAll(tasks);
        List<T> results = new ArrayList<>();
        for (Future<T> future : futures) {
            results.add(future.get());
        }
        return results;
    }

    public void shutdown() {
        executor.shutdown();
    }
}