package broadcast;

import android.os.Handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import enums.ExchangerPortEnum;
import utilities.WifiDirectSafeChannels;

public class GroupOwnerPoolTierOne extends Thread {

    ServerSocketChannel socket = null;
    ServerSocketChannel socketDirectory = null;
    private final int THREAD_COUNT = 10;
    private boolean cancel = false;
    private Handler handler;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private static final String TAG = "GroupOwnerSocketHandler";
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final Long archingPort;
    private InetAddress groupOwnerAddress;
    public GroupOwnerPoolTierOne(Handler handler,
                                 WifiDirectSafeChannels.SafeExitIF safeExitIF,
                                 WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF,
                                 Long archingPort, InetAddress groupOwnerAddress) throws IOException {
        this.safeExitIF = safeExitIF;
        this.contextDrivenIF = contextDrivenIF;
        this.groupOwnerAddress = groupOwnerAddress;
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

            socket = ServerSocketChannel.open();
            socket.bind(new InetSocketAddress(groupOwnerAddress, ExchangerPortEnum.DATAEXCHANGE.getPort()));

            socketDirectory = ServerSocketChannel.open();
            socketDirectory.bind(new InetSocketAddress(groupOwnerAddress, ExchangerPortEnum.DIRECTORYEXCHANGERDIRECTORYHOSTPORT.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
            pool.shutdownNow();
        }
        while (true && !cancel) {
            try {
                // A blocking operation. Initiate a ChatManager instance when
                // there is a new connection

                pool.execute(new DirectoryExchangerHostDeliver(safeExitIF, contextDrivenIF, archingPort, socketDirectory.accept(), handler));
                pool.execute(new DataExchanger(safeExitIF, contextDrivenIF, archingPort, socket.accept(), handler));

            } catch (IOException e) {
                try {
                    if (socket != null)
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
