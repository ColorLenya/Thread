import java.util.concurrent.*;

public class ThreadExample {
    public static void main(String[] args) {
        /**
         *  创建线程方式1：继承Thread类
         */

        MyThread thread1 = new MyThread("线程1");

        /**
         *   创建线程方式2：实现Runnable接口
         */
        // 创建Runnable实例
        MyRunnable runnable1 = new MyRunnable("任务A");
        Thread thread2 = new Thread(runnable1, "线程2");
        //多个线程共享一个runnable实例
        Thread thread3 = new Thread(runnable1, "线程3");

        /**
       * 创建线程方式3：实现Callable接口
       */
        // 创建Callable实例
        MyCallable callable1 = new MyCallable("计算任务A", 5);
        // 创建FutureTask包装Callable
        FutureTask<String> futureTask1 = new FutureTask<>(callable1);
        // 创建线程执行
        Thread thread4 = new Thread(futureTask1, "线程4");





        /**
         * 线程池（自定义）
         */
        // 5. 自定义线程池（推荐）
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
                2,                      // 核心线程数
                10,                     // 最大线程数
                60L,                    // 空闲线程存活时间
                TimeUnit.SECONDS,       // 时间单位
                new LinkedBlockingQueue<>(100) // 工作队列
        );

//        // 设置守护线程
//        thread1.setDaemon(false);
//        thread2.setDaemon(false);
//
//        // 设置优先级
//        thread1.setPriority(Thread.MAX_PRIORITY);//10
//        thread2.setPriority(Thread.NORM_PRIORITY);//5
//        thread3.setPriority(Thread.MIN_PRIORITY);//1

        // 启动线程
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        // 线程池提交Runnable任务
        for (int i = 5; i < 10; i++) {
             int taskId = i;
            customPool.execute(() -> {
                System.out.println("任务 " + taskId + " 由线程 " +
                        Thread.currentThread().getName() + " 执行");
                String threadName ="线程"+taskId;

                //线程池使用Runnable
                Thread thread5 = new Thread(runnable1, threadName);
                thread5.start();

            });
        }

        // 提交Callable任务
        Future<String> future = customPool.submit(
//                new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                Thread.sleep(1000);
//                return "Callable任务完成结果";
//            }
//        }
                callable1
        );
        // 获取Callable任务结果
        try {
            String result = future.get();
            System.out.println("获取到结果: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 主线程等待子线程结束
        try {
            //线程1有休眠，所以必定慢
            thread1.join();
            thread2.join(1000); // 最多等待1秒
            thread3.join(1000);
            thread4.join();

            // 获取thread4执行结果（会阻塞直到任务完成）
            String result1 = futureTask1.get();
            System.out.println("任务1结果: " + result1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


        customPool.shutdown();
        System.out.println("主线程结束");
    }
}

// 方式1：继承Thread类
class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 开始执行");
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(getName() + ": " + i);
                Thread.sleep(500); // 休眠500毫秒
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " 被中断");
        }
        System.out.println(Thread.currentThread().getName() + " 结束执行");
    }
}

// 方式2：实现Runnable接口
class MyRunnable implements Runnable {
    private String taskName;

    public MyRunnable(String name) {
        this.taskName = name;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 开始执行");
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                // 检查中断状态
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("检测到中断，退出执行");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 结束执行");
    }
}

class MyCallable implements Callable<String> {
    private String taskName;
    private int executionCount;

    public MyCallable(String name, int count) {
        this.taskName = name;
        this.executionCount = count;
    }

    @Override
    public String call() throws Exception {
//        int sum = 0;
        for (int i = 0; i < executionCount; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
//            sum += i;
            Thread.sleep(300);
        }
        System.out.println(Thread.currentThread().getName()+"结束执行");
        return Thread.currentThread().getName()+"结束执行";
    }
}
