package broadcast;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import enums.ExchangerPortEnum;
import utilities.WifiDirectSafeChannels;

public class ClientConnectionTierOne extends Thread {

    private static final String TAG = "ClientSocketHandler";
    private Handler handler;
    private DataExchanger dataExchanger;
    private DirectoryExchangerClientReceiver directoryExchangeClientReceiver;
    private InetAddress mAddress;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final Long port;
    public ClientConnectionTierOne(WifiDirectSafeChannels.SafeExitIF safeExitIF, WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF, Long overarchingPort, Handler handler, InetAddress groupOwnerAddress) {
        this.safeExitIF = safeExitIF;
        this.contextDrivenIF = contextDrivenIF;
        this.port = overarchingPort;
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        SocketChannel exchangeSocket = null;
        SocketChannel directorySocket = null;
        try {
            //UnixDomainSocketAddress
            exchangeSocket = SocketChannel.open(new InetSocketAddress(mAddress.getHostAddress(),
                    ExchangerPortEnum.DATAEXCHANGE.getPort()));
            Log.d(TAG, "Launching the I/O handler");
            dataExchanger = new DataExchanger(safeExitIF, contextDrivenIF, port,exchangeSocket, handler);
            new Thread(dataExchanger).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                exchangeSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        try {
            //_testvar._tcp
            //UnixDomainSocketAddress
            directorySocket = SocketChannel.open(new InetSocketAddress(mAddress.getHostAddress(),
                    ExchangerPortEnum.DIRECTORYEXCHANGERDIRECTORYHOSTPORT.getPort()));
            Log.d(TAG, "Launching the I/O handler");
            directoryExchangeClientReceiver = new DirectoryExchangerClientReceiver(safeExitIF, contextDrivenIF, port, directorySocket, handler);
            new Thread(directoryExchangeClientReceiver).start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                directorySocket.close();
            } catch (Exception ex) {

            }
        }
    }

    public DataExchanger getDataExchanger() {
        return dataExchanger;
    }
}
