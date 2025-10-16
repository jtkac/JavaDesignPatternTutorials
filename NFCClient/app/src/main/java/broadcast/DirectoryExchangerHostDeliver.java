package broadcast;

import android.os.Handler;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataroom.dto.DocumentFileOverarchPortDTO;
import utilities.WifiDirectSafeChannels;

public class DirectoryExchangerHostDeliver implements Runnable {

    public static final int DIRECTORYEXCHANGEREXPECTATIONHANDLE = 0x400 + 5;
    public static final int MESSAGE_DIRECTORY = 0x400 + 3;
    public static final int MESSAGE_FILE = 0x400 + 4;
    private boolean cancel = false;
    private SocketChannel socket = null;
    private Handler handler;
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private final Long archingPort;

    public DirectoryExchangerHostDeliver(WifiDirectSafeChannels.SafeExitIF safeExitIF, WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF, Long archingPort, SocketChannel socket, Handler handler) {
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
            contextDrivenIF.fetchContextDrivenBaseDirectory(new WifiDirectSafeChannels.ContextResultIF() {
                @Override
                public void onResult(DocumentFile documentFile) {
                    //determine if document file is a directory or individual file
                    //pass corresponding message
                    if (documentFile != null && documentFile.exists()) {
                        if (documentFile.isDirectory()) {
                            List<DocumentFile> documentFiles = Arrays.asList(documentFile.listFiles());
                            List<DocumentFileOverarchPortDTO.DocumentDetail> documentDetails = new ArrayList<>();
                            for (DocumentFile file : documentFiles) {
                                DocumentFileOverarchPortDTO.DocumentDetail detail = new DocumentFileOverarchPortDTO.DocumentDetail();
                                detail.name = file.getName();
                                detail.type = file.getType();
                                detail.uri = file.getUri().toString();
                                documentDetails.add(detail);
                            }
                            DocumentFileOverarchPortDTO dto = new DocumentFileOverarchPortDTO();
                            dto.documentDetails = documentDetails;
                            dto.port = archingPort;
//                            handler.obtainMessage(DIRECTORYEXCHANGEREXPECTATIONHANDLE, dto)
//                                    .sendToTarget();
                            String transport = new Gson().toJson(dto);
                            doSend(transport);

                        } else {
                            
//                            handler.obtainMessage(MESSAGE_FILE, documentFile.getUri())
//                                    .sendToTarget();
                        }
                    } else {
                        //handle non existant directory stale cache
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCriticalFailure() {
                    //TODO see java doc

                }

                @Override
                public void onFailure(String message) {
                    safeExitIF.displaySoftError(message);

                }
            });


    }

    private void doSend(String transport) {

        OutputStream outputStream = null;
        try {
            outputStream = socket.socket().getOutputStream();
            outputStream.write(transport.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancel() {
        cancel = true;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }
}