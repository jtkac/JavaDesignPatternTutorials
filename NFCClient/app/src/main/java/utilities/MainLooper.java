package utilities;

import android.os.Handler;
import android.os.Looper;

/**
 * Convenience utility which begins a handler for the main thread rather than calling this on repeat
 */
public class MainLooper extends ThreadQueue {

    public MainLooper() {
        super(new AndroidHandlerRunner(new Handler(Looper.getMainLooper())));
    }

}