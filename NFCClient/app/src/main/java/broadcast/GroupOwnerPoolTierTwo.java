package broadcast;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import enums.ExchangerPortEnum;
import utilities.WifiDirectSafeChannels;

public class GroupOwnerPoolTierTwo extends Thread {

    ServerSocket socket = null;
    private final int THREAD_COUNT = 10;
    private boolean cancel = false;
    private Handler handler;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private static final String TAG = "GroupOwnerSocketHandler";
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final Long archingPort;

    public GroupOwnerPoolTierTwo(Handler handler,
                                 WifiDirectSafeChannels.SafeExitIF safeExitIF,
                                 WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF,
                                 Long archingPort) throws IOException {
        this.safeExitIF = safeExitIF;
        this.contextDrivenIF = contextDrivenIF;
        this.archingPort = archingPort;
        this.handler = handler;
        cancel = false;

    }


    /**
     * A ThreadPool for client sockets.
     */
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public void run() {
        try {
            socket = new ServerSocket(ExchangerPortEnum.DIRECTORYEXCHANGERDIRECTORYCLIENTPORT.getPort());

        } catch (IOException e) {
            e.printStackTrace();
            pool.shutdownNow();
        }
        while (true && !cancel) {
            try {
                // A blocking operation. Initiate a ChatManager instance when
                // there is a new connection
                pool.execute(new DirectoryExchangerClientResponse(safeExitIF, contextDrivenIF, archingPort, socket.accept(), handler));

//                pool.execute(new DirectoryExchangerHostDeliver(safeExitIF, contextDrivenIF, archingPort, socketDirectory.accept(), handler));
//                pool.execute(new DataExchanger(safeExitIF, contextDrivenIF, archingPort, socket.accept(), handler));

            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                pool.shutdownNow();
                break;
            }
        }
    }

    public void cancel() {
        cancel = true;
    }
}
