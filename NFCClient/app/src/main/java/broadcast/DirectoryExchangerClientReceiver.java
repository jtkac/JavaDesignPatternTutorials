package broadcast;

import android.os.Handler;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;

import utilities.WifiDirectSafeChannels;

public class DirectoryExchangerClientReceiver implements Runnable {
    public static final int READ_TRANSPORT_DIRECTORY = 0x400 + 7;

    private boolean cancel = false;
    private SocketChannel socket = null;
    private Handler handler;
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private final Long archingPort;

    public DirectoryExchangerClientReceiver(WifiDirectSafeChannels.SafeExitIF safeExitIF, WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF, Long archingPort, SocketChannel socket, Handler handler) {
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
     * //1. Notify LAN of potential issue due to bufferable connectivity
     * //2. Verify Cached directory integrity
     * //3. Disconnect either self or rebuild temporary cache options
     * //4. On critical failure 3 -> remove cached directory and shutdown all peers
     */
    @Override
    public void run() {
        InputStream is = null;
//        OutputStream os = null;

        try {
            is = socket.socket().getInputStream();
//            os = socket.socket().getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            while (true && !cancel) {
                try {
                    // Read from the InputStream
                    bytes = is.read(buffer);
                    if (bytes == -1) {
//                        break;
                    }

                    handler.obtainMessage(READ_TRANSPORT_DIRECTORY, bytes)
                            .sendToTarget();
                    // Send the obtained bytes to the UI Activity

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//
//                }
//            }
        }
//        contextDrivenIF.fetchContextDrivenBaseDirectory(new WifiDirectSafeChannels.ContextResultIF() {
//            @Override
//            public void onResult(DocumentFile documentFile) {
//                //determine if document file is a directory or individual file
//                //pass corresponding message
//                if (documentFile != null && documentFile.exists()) {
//                    if (documentFile.isDirectory()) {
//                        List<DocumentFile> documentFiles = Arrays.asList(documentFile.listFiles());
//                        DocumentFileOverarchPortDTO dto = new DocumentFileOverarchPortDTO();
//                        dto.documentFiles = documentFiles;
//                        dto.port = archingPort;
//                        handler.obtainMessage(DIRECTORYEXCHANGEREXPECTATIONHANDLE, dto)
//                                .sendToTarget();
//                    } else {
//                        handler.obtainMessage(MESSAGE_FILE, documentFile.getUri())
//                                .sendToTarget();
//                    }
//                } else {
//                    //handle non existant directory stale cache
//                }
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCriticalFailure() {
//                //TODO see java doc
//
//            }
//
//            @Override
//            public void onFailure(String message) {
//                safeExitIF.displaySoftError(message);
//
//            }
//        });


    }

    public void cancel() {
        cancel = true;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }
}