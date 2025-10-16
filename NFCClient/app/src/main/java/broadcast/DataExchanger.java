package broadcast;

import android.os.Handler;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import utilities.SharedPreferenceHelper;
import utilities.WifiDirectSafeChannels;

public class DataExchanger implements Runnable {

    public static final int DATAEXCHANGEREXPECTATIONHANDLE = 0x400 + 2;
    public static final int MESSAGE_READ = 0x400 + 1;
    private boolean cancel = false;
    private SocketChannel socket = null;
    private Handler handler;
    private final WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private final WifiDirectSafeChannels.SafeExitIF safeExitIF;
    private final Long archingPort;

    public DataExchanger(WifiDirectSafeChannels.SafeExitIF safeExitIF, WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF, Long archingPort, SocketChannel socket, Handler handler) {
        this.safeExitIF = safeExitIF;
        this.contextDrivenIF = contextDrivenIF;
        this.archingPort = archingPort;
        this.socket = socket;
        this.handler = handler;
        cancel = false;


    }

    private InputStream iStream;
    private OutputStream oStream;
    private static final String TAG = "ChatHandler";

    @Override
    public void run() {
        contextDrivenIF.fetchContextDrivenBaseDirectory(new WifiDirectSafeChannels.ContextResultIF() {
            @Override
            public void onResult(DocumentFile documentFile) {
                try {
                    iStream = contextDrivenIF.fetchInputStreamFromDocumentFile(documentFile, new WifiDirectSafeChannels.ContextResultIF() {
                        @Override
                        public void onResult(DocumentFile documentFile) {
                            //not implemented
                            throw new RuntimeException("Not Implemented");
                        }

                        @Override
                        public void onCriticalFailure() {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (safeExitIF != null) {
                                safeExitIF.doSafeExit(archingPort);
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            safeExitIF.displaySoftError(message);
                        }
                    });
                    if (iStream == null) {
                        return;
                    }
                    oStream = contextDrivenIF.fetchOutputStreamFromDocumentFile(documentFile, new WifiDirectSafeChannels.ContextResultIF() {
                        @Override
                        public void onResult(DocumentFile documentFile) {
                            throw new RuntimeException("Not Implemented");

                        }

                        @Override
                        public void onCriticalFailure() {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (safeExitIF != null) {
                                safeExitIF.doSafeExit(archingPort);
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            safeExitIF.displaySoftError(message);

                        }
                    });
                    if (oStream == null) {
                        return;
                    }
//                    iStream = socket.getInputStream();
//                    oStream = socket.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytes;
                    //passing the whole of a data exchanger object is possible from both a group owner and a client
                    //given I pass a data exchanger with cached data the pointers persist given certain nomenclatures
                    //steps to navigate a directory are then passed
                    //given I have passed some content of directory from A to B in structure I would then need to pass a second variable
                    //given this second variable combined with the first variable is known as a payload
                    //my payload would then include whether the clickable is a folder or a file and the naming of the item

                    //upon receiving the payload the groupownerpool would then pass another message through the dataexchanger
                    //this second message would then contain a verbiaged format of the clickable entity

                    handler.obtainMessage(DATAEXCHANGEREXPECTATIONHANDLE, this)
                            .sendToTarget();

                    while (true && !cancel) {
                        try {
                            // Read from the InputStream
                            bytes = iStream.read(buffer);
                            if (bytes == -1) {
                                break;
                            }

                            // Send the obtained bytes to the UI Activity
                            Log.d(TAG, "Rec:" + String.valueOf(buffer));
                            handler.obtainMessage(MESSAGE_READ,
                                    bytes, -1, buffer).sendToTarget();
                        } catch (IOException e) {
                            Log.e(TAG, "disconnected", e);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCriticalFailure() {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (safeExitIF != null) {
                    safeExitIF.doSafeExit(archingPort);
                }
            }

            @Override
            public void onFailure(String message) {
                if (safeExitIF != null) {
                    safeExitIF.displaySoftError(message);
                }
            }
        });

    }

    public void write(byte[] buffer) {
        try {
            oStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }

    public void cancel() {
        cancel = true;
    }
}