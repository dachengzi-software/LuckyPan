package cn.bigorange.wheel.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 线程工具，用于执行线程等
 */
public final class ThreadUtil {

    public static final String TAG = "ThreadUtil";

    private ExecutorService executorService;

    private ThreadUtil() {
    }

    private static final ThreadUtil INSTANCE = new ThreadUtil();

    /**
     * 获取ThreadUtil实例 ,单例模式
     */
    public static ThreadUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 在线程中执行
     *
     * @param runnable 要执行的runnable
     */
    public void execute(Runnable runnable) {
        ExecutorService executorService = getExecutorService();
        if (executorService != null) {
            // 优先使用线程池，提高效率
            executorService.execute(runnable);
        } else {
            // 线程池获取失败，则直接使用线程
            new Thread(runnable).start();
        }
    }

    /**
     * 在主线程中执行
     *
     * @param runnable 要执行的runnable
     */
    public void excuteInMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * 获取缓存线程池
     *
     * @return 缓存线程池服务
     */
    private ExecutorService getExecutorService() {
        if (executorService == null) {
            try {
                executorService = Executors.newCachedThreadPool();
            } catch (Exception e) {
                LogUtils.e(TAG, "create thread service error:" + e.getMessage());
            }
        }
        return executorService;
    }
}