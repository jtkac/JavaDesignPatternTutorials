package utilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Convenience function with start and stop flags as well as callback interfaces
 * As taught in various Java programs
 */
public class ThreadQueue {

    private boolean isEnabled;
    private ThreadRunner threadRunner;
    private ExecutorService service;

    public interface ThreadRunner {
        void run(Runnable runnable);
        void runDelayed(Runnable runnable, long delayMilliseconds);
        void clear();
    }

    public ThreadQueue(ThreadRunner threadRunner) {
        this.threadRunner = threadRunner;
    }

    public void run(Runnable runnable) {
        if (isEnabled()) {
            threadRunner.run(runnable);
        }
    }

    public void runDelayed(Runnable runnable, long delayMilliseconds) {
        if (isEnabled()) {
            threadRunner.runDelayed(runnable, delayMilliseconds);
        }
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;

        if (!isEnabled()){
            clear();
        }
    }

    public void runOnBackground(Runnable runnable) {
        try {
            service = Executors.newSingleThreadExecutor();
            service.execute(runnable);
        } catch (Exception e) {
            if (service != null && !service.isShutdown()) {
                service.shutdownNow();
            }
            service = null;

        }
    }

    public void clear() {
        threadRunner.clear();
    }
}
