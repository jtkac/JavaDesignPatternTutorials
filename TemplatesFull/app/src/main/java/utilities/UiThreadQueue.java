package utilities;

import android.os.Handler;
import android.os.Looper;

/**
 * Convenience utility which begins a handler for the main thread rather than calling this on repeat
 */
public class UiThreadQueue extends ThreadQueue {

    public UiThreadQueue() {
        super(new AndroidHandlerRunner(new Handler(Looper.getMainLooper())));
    }

}