public class ThreadExample {
    public static void main(String[] args) {
        // 创建线程方式1：继承Thread类
        MyThread thread1 = new MyThread("线程1");

        // 创建线程方式2：实现Runnable接口
        Thread thread2 = new Thread(new MyRunnable(), "线程2");

        // 设置守护线程
        thread1.setDaemon(false);
        thread2.setDaemon(false);

        // 设置优先级
        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.NORM_PRIORITY);

        // 启动线程
        thread1.start();
        thread2.start();

        // 主线程等待子线程结束
        try {
            thread1.join();
            thread2.join(1000); // 最多等待1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
