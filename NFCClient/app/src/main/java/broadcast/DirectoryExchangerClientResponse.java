package broadcast;

import android.os.Handler;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import utilities.WifiDirectSafeChannels;

/**
 * Class passes a documentfile of the client's choice over a wifi direct channel
 */
public class DirectoryExchangerClientResponse implements Runnable {

    public static final int MESSAGE_CHOSEN_DOCUMENT_FILE = 0x400 + 6;

    private boolean cancel = false;
    private Socket socket = null;
    private Handler handler;
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private final Long archingPort;

    public DirectoryExchangerClientResponse(WifiDirectSafeChannels.SafeExitIF safeExitIF, WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF, Long archingPort, Socket socket, Handler handler) {
        this.safeExitIF = safeExitIF;
        this.contextDrivenIF = contextDrivenIF;
        this.archingPort = archingPort;
        this.socket = socket;
        this.handler = handler;
        cancel = false;


    }


    private static final String TAG = "DirectoryExchangerHostDeliver";
    private DocumentFile documentFile;

    /**
     * //shut myself down gracefully
     *             //1. Notify LAN of potential issue due to bufferable connectivity
     *             //2. Verify Cached directory integrity
     *             //3. Disconnect either self or rebuild temporary cache options
     *             //4. On critical failure 3 -> remove cached directory and shutdown all peers
     */
    @Override
    public void run() {
        handler.obtainMessage(MESSAGE_CHOSEN_DOCUMENT_FILE, documentFile);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cancel() {
        cancel = true;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }
}