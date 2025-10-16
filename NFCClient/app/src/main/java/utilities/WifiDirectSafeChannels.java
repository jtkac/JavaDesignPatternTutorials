package utilities;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.documentfile.provider.DocumentFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import cache.BaseCache;
import dataroom.dto.DocumentFileOverarchPortDTO;
import enums.WifiDirectRangeEnum;
import interfaces.WifiDirectOverarchingIF;

public class WifiDirectSafeChannels extends WifiDirectConnectionHelper {

    /**
     * This thread local map dignifies a Port number within the valid range and a channel that may be shutdown on exit
     */
    Map<Long, WifiP2pManager.Channel> channelMap = new HashMap<>();
    private Long currentServerConnectablePort = WifiDirectRangeEnum.MINPORT.getPort();
    private final SharedPreferenceHelper helper;

    private Context context;
    private WifiDirectOverarchingIF generalCallback;
    private BaseCache cache;
    public interface WifiDirectChannelIF {
        Long onChannelAcquired(WifiP2pManager.Channel channel);
    }

    public interface ContextDrivenIF {
        void fetchContextDrivenBaseDirectory(ContextResultIF contextResultIF);
        void onRetrieveDocumentFile(DocumentFile documentFile);
        void onRetrieveDocumentFiles(DocumentFileOverarchPortDTO dto);

        InputStream fetchInputStreamFromDocumentFile(DocumentFile documentFile, ContextResultIF contextResultIF);

        OutputStream fetchOutputStreamFromDocumentFile(DocumentFile documentFile, ContextResultIF contextResultIF);
    }

    public interface ContextResultIF {
        void onResult(DocumentFile documentFile);
        void onCriticalFailure();
        void onFailure(String message);
    }

    public interface SafeExitIF {
        void doSafeExit(Long portNumber);
        void displaySoftError(String message);
    }

    public WifiDirectSafeChannels(StringHelper stringHelper,
                                  SharedPreferenceHelper sharedPreferenceHelper) {
        super(stringHelper);
        this.helper = sharedPreferenceHelper;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    public void startSingleCurrentServer(Context context, BaseCache cache, WifiDirectOverarchingIF callback, String serverName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.generalCallback = callback;
        this.context = context;
        this.cache = cache;
        Long tempValidation = Long.valueOf(currentServerConnectablePort);
        tempValidation += 1L;
        Long endPort = WifiDirectRangeEnum.MAXPORT.getPort();
        if (tempValidation.longValue() <= endPort.longValue()) {
            currentServerConnectablePort = tempValidation;
//            startP2PWifiDirectAdHocNetworkV2(context, cache, callback, serverName, password, new WifiDirectChannelIF() {
            startP2PWifiDirectAdHocNetwork(serverName, currentServerConnectablePort, context, cache, new WifiDirectChannelIF() {
                @Override
                public Long onChannelAcquired(WifiP2pManager.Channel channel) {
                    channelMap.put(currentServerConnectablePort, channel);
                    return currentServerConnectablePort;
                }
            }, new ContextDrivenIF() {
                @Override
                public void fetchContextDrivenBaseDirectory(ContextResultIF contextResultIF) {
                    if (context == null) {
                        contextResultIF.onCriticalFailure();
                    } else if (!helper.get().contains(SharedPreferenceHelper.ACCESSIBLE_FOLDERS)) {
                        contextResultIF.onFailure("Accessible Folders Not Set");
                    } else {
                        DocumentFile df = DocumentFile.fromTreeUri(context, Uri.parse(helper.get().getString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, "")));
                        if (df != null && df.exists() && df.isDirectory()) {
                            contextResultIF.onResult(df);
                        } else {
                            contextResultIF.onFailure("Directory that was set does not exist anymore");
                        }
                    }
                }

                @Override
                public void onRetrieveDocumentFile(DocumentFile documentFile) {
                    generalCallback.onRetrieveDocumentFile(documentFile);
                }

                @Override
                public void onRetrieveDocumentFiles(DocumentFileOverarchPortDTO dto) {
                    generalCallback.onRetrieveDocumentFileDirectory(dto);
                }

                @Override
                public InputStream fetchInputStreamFromDocumentFile(DocumentFile documentFile, ContextResultIF contextResultIF) {
                    if (context == null) {
                        contextResultIF.onCriticalFailure();
                        return null;
                    } else if (!helper.get().contains(SharedPreferenceHelper.ACCESSIBLE_FOLDERS)) {
                        contextResultIF.onFailure("Accessible Folders Not Set");
                        return null;
                    } else {
                        try {
                            return context.getContentResolver().openInputStream(documentFile.getUri());
                        } catch (Exception e) {
                            contextResultIF.onCriticalFailure();
                            return null;
                        }
                    }
                }

                @Override
                public OutputStream fetchOutputStreamFromDocumentFile(DocumentFile documentFile, ContextResultIF contextResultIF) {
                    if (context == null) {
                        contextResultIF.onCriticalFailure();
                        return null;
                    } else if (!helper.get().contains(SharedPreferenceHelper.ACCESSIBLE_FOLDERS)) {
                        contextResultIF.onFailure("Accessible Folders Not Set");
                        return null;
                    } else {
                        try {
                            return context.getContentResolver().openOutputStream(documentFile.getUri());

                        } catch (Exception e) {
                            contextResultIF.onCriticalFailure();
                            return null;
                        }
                    }
                }
            }, new SafeExitIF() {
                @Override
                public void doSafeExit(Long portNumber) {
                    shutdown(portNumber);
                }

                @Override
                public void displaySoftError(String message) {
                    generalCallback.onFailure(message);
                }
            });

        } else {
            callback.onFailure("Too many servers running");
        }
    }

    public void doClientConnectPoll() {

    }

    public void shutdown(Long port) {
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        if (groupOwnerPoolMap.containsKey(port)) {
            groupOwnerPoolMap.get(port).cancel();
        }
        manager.removeGroup(channelMap.get(port), new WifiP2pManager.ActionListener() {
            @Override
            public void onFailure(int reason) {
                if (generalCallback != null) {
                    generalCallback.onFailure("Could not remove group on Port " + port + " Restart device instead to stop");
                }
                //uh oh
            }

            @Override
            public void onSuccess() {
                channelMap.remove(port);
                context.unregisterReceiver(cache.getWifiDirectP2PReceiver());

            }
        });
    }

    public void shutdownAllChannels() {
        if (context != null) {
            WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
            for (Map.Entry<Long, WifiP2pManager.Channel> entry : channelMap.entrySet()) {
                if (groupOwnerPoolMap.containsKey(entry.getKey())) {
                    groupOwnerPoolMap.get(entry.getKey()).cancel();
                }
                manager.stopPeerDiscovery(entry.getValue(), null);
                manager.clearLocalServices(entry.getValue(), null);
                manager.clearServiceRequests(entry.getValue() , null);
                manager.removeGroup(entry.getValue(), new WifiP2pManager.ActionListener() {
                    @Override
                    public void onFailure(int reason) {
                        if (generalCallback != null) {
                            generalCallback.onFailure("Could not remove group on Port " + entry.getKey() + " Restart device instead to stop");
                        }
                        //uh oh
                    }

                    @Override
                    public void onSuccess() {


                    }
                });
            }


            try {
                context.unregisterReceiver(cache.getWifiDirectP2PReceiver());
            } catch (Exception e) {

            }


        }
    }

    public void unregisterBroadcastReceiver() {
        try {
            context.unregisterReceiver(cache.getWifiDirectP2PReceiver());
        } catch (Exception e) {

        }

        try {
            WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);

            manager.stopPeerDiscovery(cache.getP2pChannel(), null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                manager.stopListening(cache.getP2pChannel(), null);
            }
        } catch (Exception e) {

        }
    }
}
