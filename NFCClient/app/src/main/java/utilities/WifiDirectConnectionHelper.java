package utilities;

import static android.view.View.GONE;
import static broadcast.DataExchanger.DATAEXCHANGEREXPECTATIONHANDLE;
import static broadcast.DataExchanger.MESSAGE_READ;
import static broadcast.DirectoryExchangerClientReceiver.READ_TRANSPORT_DIRECTORY;
import static broadcast.DirectoryExchangerClientResponse.MESSAGE_CHOSEN_DOCUMENT_FILE;
import static broadcast.DirectoryExchangerHostDeliver.DIRECTORYEXCHANGEREXPECTATIONHANDLE;
import static broadcast.DirectoryExchangerHostDeliver.MESSAGE_FILE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import adapters.WifiP2PRecyclerAdapter;
import broadcast.ClientConnectionTierOne;
import broadcast.DataExchanger;
import broadcast.GroupOwnerPoolTierOne;
import broadcast.WifiDirectBroadcastReceiver;
import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.ViewSearchListBinding;
import cache.BaseCache;
import dataroom.dto.DocumentFileOverarchPortDTO;

/** *
 *      Hex 8 digit compliance has determined an overclockable breaking certain octet streams due to an interpolative mnemonic
 *      Hex is 6 digits otherwise and a preceding two slots on the a variable / linear layer. This eludes to a resistor being present. Overclocking this pipe
 *      creates a linear based pipe which still works given this stack of code. The parallel matrix underneath is more overwritten by arbitrarily identifying
 *      through brute force and would otherwise have presumed to drop into this matrix. The power differential then requires extra padding breaking a stream.
 *
 *      TCPP2PUnixVirtualAddress extends WifiP2pDnsSdServiceInfo
 *
 *      This should then be possible otherwise you cannot retrieve your home address in a near field communicative setup. That would mean a stronger balanced
 *      resistor. Otherwise an arbitrary protocol will have non-functional sockets.
 *
 *      We can then derive this layer broke a networking socket layer. Consider arbitrarily substituting an ipp protocol. Given we refactor ipp to a
 *      haphazardous protocol, we instead do not call on those mnemonics relating to ipp protocol. Given I can instead say "helloworld" protocol in the format
 *      _ipp._tcp
 *      _testvar._tcp
 *      I can still form this connection to an overclocked strength.
 *      Are we certain this was intended?
 *
 *      Given I can prop open a directory in the content resolver manufacturer layer I can still power through a manufacturer layer. Why otherwise would
 *      there be a bypass?
 *
 *      Hex compliance? Hex being a total mnemonic and no compression matrices are validating in the lower level directories may derive outer directories also
 */
public class WifiDirectConnectionHelper implements WifiP2pManager.ConnectionInfoListener, Handler.Callback { //one level up put in the connectableviewwrapper and pass in the appropriate connectable
    private final String TAG = "WifiDirectConnectionHelper";
    private static final String portListenNomen = "listenport";
    private static final String buddyname = "buddyname";
    private static final String available = "available";

    private static final String tcpConnectionVariable = "_testvar._tcp";
    private String serverName = null;
    protected Map<Long, GroupOwnerPoolTierOne> groupOwnerPoolMap = new HashMap<>();
    protected Map<Long, ClientConnectionTierOne> clientPoolMap = new HashMap<>();

    private final StringHelper stringHelper;
    private BaseCache cache;
    private Context context;
    private WifiDirectSafeChannels.WifiDirectChannelIF channelIF;
    private WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF;
    private WifiDirectSafeChannels.SafeExitIF safeExitIF;

    public WifiDirectConnectionHelper(StringHelper stringHelper) {
        this.stringHelper = stringHelper;
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    public void startP2PWifiDirectAdHocNetwork(String serverName,
                                               Long port,
                                               Context context,
                                               BaseCache cache,
                                               WifiDirectSafeChannels.WifiDirectChannelIF channelIF,
                                               WifiDirectSafeChannels.ContextDrivenIF contextDrivenIF,
                                               WifiDirectSafeChannels.SafeExitIF safeExitIF) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String newServerName = encodeRandomUUIDForServername();
        this.cache = cache;
        this.channelIF = channelIF;
        this.contextDrivenIF = contextDrivenIF;
        this.safeExitIF = safeExitIF;
        this.serverName = serverName;
        this.context = context;
        IntentFilter filters = new IntentFilter();
        filters.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filters.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filters.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filters.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        Map record = new HashMap();
        record.put(portListenNomen, String.valueOf(port));
        record.put(buddyname, serverName != null ? serverName : "Server Connection");
        record.put(available, "visible");
        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance(newServerName, tcpConnectionVariable, record);

        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        cache.setP2pChannel(manager.initialize(context, Looper.getMainLooper(), null));
        cache.setWifiDirectP2PReceiver(new WifiDirectBroadcastReceiver(context, this, cache, null));

        context.registerReceiver(cache.getWifiDirectP2PReceiver(), filters);
        manager.addLocalService(cache.getP2pChannel(), serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                cache.setBuddies(new HashMap<>());
                cache.setDnsSdTxtRecordListener(new WifiP2pManager.DnsSdTxtRecordListener() {
                    @Override
                    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {

                        StringBuilder recordSb = new StringBuilder();
                        boolean first = true;
                        for (Map.Entry<String, String> entry : txtRecordMap.entrySet()) {
                            if (first) {
                                recordSb.append(entry.getKey() + " : " + entry.getValue());
                                first = false;
                            } else {
                                recordSb.append("\n" + entry.getKey() + " : " + entry.getValue());

                            }
                        }

                        if (record.get("buddyname") != null) {
                            cache.getBuddies().put(srcDevice.deviceAddress, record.get("buddyname").toString());
                        }
                    }
                });
                cache.setDnsSdServiceResponseListener(new WifiP2pManager.DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
//                        DroneP2PResponseData data = new DroneP2PResponseData();
//                        data.setInstanceName(instanceName);
//                        data.setRegistrationType(registrationType);
//                        data.setDeviceData(srcDevice.deviceName + " - " + srcDevice.primaryDeviceType);
//                        presenter.saveDroneP2PResponseData(data);
//                        srcDevice.deviceName = cache.getBuddies()
//                                .containsKey(srcDevice.deviceAddress) ? cache.getBuddies()
//                                .get(srcDevice.deviceAddress) : srcDevice.deviceName;
                        srcDevice.deviceName = serverName != null ? serverName : "Generic Server";
                        if (cache.getWifiP2PRecyclerAdapter() != null) {
                            cache.getWifiP2PRecyclerAdapter().addItem(srcDevice);
                        }
                    }
                });
                manager.setDnsSdResponseListeners(cache.getP2pChannel(), cache.getDnsSdServiceResponseListener(), cache.getDnsSdTxtRecordListener());
                cache.setWifiP2pDnsSdServiceRequest(WifiP2pDnsSdServiceRequest.newInstance());

                manager.addServiceRequest(cache.getP2pChannel(), cache.getWifiP2pDnsSdServiceRequest(), new WifiP2pManager.ActionListener() {
                    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
                    @Override
                    public void onSuccess() {
                        manager.discoverServices(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
                            @Override
                            public void onSuccess() {
                                doDiscoveryDialog();

                            }

                            @Override
                            public void onFailure(int reason) {
                                deregisterWifiP2pServices();
                                showError("Failed To Start... Is WiFi Dirfect Enabled? Reason: " + reason);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int reason) {
                        deregisterWifiP2pServices();
                        showError("Failed To Start. Is WiFi Direct Enabled? Reason: " + reason);
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                deregisterWifiP2pServices();
                showError("Failure to start P2P Service... Is WiFi Direct Enabled? Reason: " + reason);
            }
        });

    }

    public void deregisterWifiP2pServices() {
        try {
            if (cache.getWifiDirectP2PReceiver() != null) {
                context.unregisterReceiver(cache.getWifiDirectP2PReceiver());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
            manager.clearLocalServices(cache.getP2pChannel(), null);
            manager.stopPeerDiscovery(cache.getP2pChannel(), null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                manager.stopListening(cache.getP2pChannel(), null);
            }
        } catch (Exception e) {

        }

    }

    public void showError(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    public void doDiscoveryDialog() {
        AlertDialog.Builder wifiP2pServiceBuilder = new AlertDialog.Builder(context);
        View wifiP2pServiceView = LayoutInflater.from(context).inflate(R.layout.view_search_list, null);
        ViewSearchListBinding vslBinding = ViewSearchListBinding.bind(wifiP2pServiceView);

        wifiP2pServiceBuilder.setView(wifiP2pServiceView);
        vslBinding.vslSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cache.getWifiP2PRecyclerAdapter().doFilter(vslBinding.vslSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wifiP2pServiceBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deregisterWifiP2pServices();
                dialog.dismiss();
            }
        });
        wifiP2pServiceBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                deregisterWifiP2pServices();
                dialog.dismiss();
            }
        });
        wifiP2pServiceBuilder.setCancelable(false);
        final AlertDialog dialog = wifiP2pServiceBuilder.show();
        cache.setWifiP2PRecyclerAdapter(new WifiP2PRecyclerAdapter(new ArrayList<>(), new WifiP2PRecyclerAdapter.WifiP2PClickedIF() {
            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
            @Override
            public void onClick(WifiP2pDevice device) {
                connectToWifiDirectP2PDevice(device);
                dialog.dismiss();
            }
        }));
        vslBinding.vslRecycler.setAdapter(cache.getWifiP2PRecyclerAdapter());
        vslBinding.vslCreateGroup.setVisibility(GONE);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    public void createGroup() {
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);

        manager.requestGroupInfo(cache.getP2pChannel(), new WifiP2pManager.GroupInfoListener() {
            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                if (group != null) {
                    if (!group.isGroupOwner()) {
//                        showError("Your Group Password is: " + groupPassword + "\n\nThis May Or May Not Be Required. Please Inform the SysAdmin if not and he will remove this function");
                        manager.createGroup(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
                            @Override
                            public void onSuccess() {
                                manager.requestConnectionInfo(cache.getP2pChannel(), WifiDirectConnectionHelper.this);

                                manager.discoverPeers(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i(TAG, "Success create group");
//                                        doDiscoveryDialog();
                                    }

                                    @Override
                                    public void onFailure(int reason) {
                                        Log.i(TAG, "Fail create group");

                                    }
                                });
                            }

                            @Override
                            public void onFailure(int reason) {
//                                                                    deregisterWifiP2pServices();
//                                                                    showToast("P2P Group Creation Failed. Retry Please");
                            }
                        });
                    } else {
                        manager.requestConnectionInfo(cache.getP2pChannel(), WifiDirectConnectionHelper.this);

                        manager.discoverPeers(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                //group is var
                                Log.i(TAG, "Success create group");
//                                doDiscoveryDialog();
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.i(TAG, "Fail create group");

                            }
                        });
                    }
                } else {
//                                                        showError("Your Group Password is: " + groupPassword + "\n\nThis May Or May Not Be Required. Please Inform the SysAdmin if not and he will remove this function");
                    manager.createGroup(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                        @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
                        @Override
                        public void onSuccess() {
                            manager.requestConnectionInfo(cache.getP2pChannel(), WifiDirectConnectionHelper.this);

                            manager.discoverPeers(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i(TAG, "Success create group");
//                                    doDiscoveryDialog();
                                }

                                @Override
                                public void onFailure(int reason) {
                                    Log.i(TAG, "Fail create group");

                                }
                            });
                        }

                        @Override
                        public void onFailure(int reason) {
//                                                                    deregisterWifiP2pServices();
//                                                                    showToast("P2P Group Creation Failed. Retry Please");
                        }
                    });
                }
            }
        });
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    public void connectToWifiDirectP2PDevice(WifiP2pDevice device) {
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);

        cache.setWifiP2pDevice(device);
        cache.setWifiP2pConfig(new WifiP2pConfig());
        cache.getWifiP2pConfig().deviceAddress = device.deviceAddress;
        cache.getWifiP2pConfig().wps.setup = WpsInfo.PBC;

        manager.connect(cache.getP2pChannel(), cache.getWifiP2pConfig(), new WifiP2pManager.ActionListener() {
            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
            @Override
            public void onSuccess() {
//                callback.onSuccess(cache);
//                createGroup();
                manager.requestGroupInfo(cache.getP2pChannel(), new WifiP2pManager.GroupInfoListener() {
                    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup group) {
                        if (group == null) {
                            manager.createGroup(cache.getP2pChannel(), new WifiP2pManager.ActionListener() {
                                @Override
                                public void onFailure(int reason) {

                                }

                                @Override
                                public void onSuccess() {
                                    manager.requestConnectionInfo(cache.getP2pChannel(), WifiDirectConnectionHelper.this);

                                }
                            });
                        } else {
                            manager.requestConnectionInfo(cache.getP2pChannel(), WifiDirectConnectionHelper.this);

                        }
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
//                callback.onFailure("Could not connect to P2P Server");
                showError("Could not connect P2P");
            }
        });
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
//        new ServerAsyncTask(this).execute();
        /*
         * The group owner accepts connections using a server socket and then spawns a
         * client socket for every client. This is handled by {@code
         * GroupOwnerSocketHandler}
         */

        if (p2pInfo.isGroupOwner) {


                try {
                    Long connectionPort = channelIF.onChannelAcquired(cache.getP2pChannel());
                    groupOwnerPoolMap.put(connectionPort, new GroupOwnerPoolTierOne(cache.getHandler(this), safeExitIF, contextDrivenIF, connectionPort, p2pInfo.groupOwnerAddress));
                    groupOwnerPoolMap.get(connectionPort).start();
                    clientPoolMap.put(connectionPort, new ClientConnectionTierOne(safeExitIF, contextDrivenIF, connectionPort, cache.getHandler(this), p2pInfo.groupOwnerAddress));
//                cache.setClientConnection(new ClientConnectionTierOne(safeExitIF, cache.getHandler(this), p2pInfo.groupOwnerAddress));
                    clientPoolMap.get(connectionPort).start();

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }



        } else {
                Long connectionPort = channelIF.onChannelAcquired(cache.getP2pChannel());
                clientPoolMap.put(connectionPort, new ClientConnectionTierOne(safeExitIF, contextDrivenIF, connectionPort, cache.getHandler(this), p2pInfo.groupOwnerAddress));
                clientPoolMap.get(connectionPort).start();



        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Object obj = null;
        DocumentFileOverarchPortDTO dto = null;
        switch (msg.what) {
            case MESSAGE_READ:
                Log.i(TAG, "Found Message MESSAGE_READ");
//                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
//                String readMessage = new String(readBuf, 0, msg.arg1);
//                try {
//                    RoutineCategory category = new Gson().fromJson(readMessage, RoutineCategory.class);
//                    presenter.saveCategory(category);
//                } catch (Exception e) {
//                    try {
//                        Routine routine = new Gson().fromJson(readMessage, Routine.class);
//                        presenter.saveRoutine(routine);
//                    } catch (Exception ex) {
//                        try {
//                            Task task = new Gson().fromJson(readMessage, Task.class);
//                            presenter.saveTask(task);
//                        } catch (Exception exc) {
//                            exc.printStackTrace();
//                            showToast("Couldn't derive type of sent object");
//                        }
//                    }
//                }
//                (chatFragment).pushMessage("Buddy: " + readMessage);
                break;
            case READ_TRANSPORT_DIRECTORY:
                byte[] readBuf = (byte[]) msg.obj;
                //                String readMessage = new String(readBuf, 0, msg.arg1);

                //given this is an array buffer of a transport language from the group pool owner
                String json = new String(readBuf, Charset.defaultCharset());
                dto = new Gson().fromJson(json, DocumentFileOverarchPortDTO.class);
                if (contextDrivenIF != null) {
                    contextDrivenIF.onRetrieveDocumentFiles(dto);
                }
                break;
            case DATAEXCHANGEREXPECTATIONHANDLE:
                obj = msg.obj;
                if (obj instanceof DataExchanger) {
                    cache.setDataExchanger((DataExchanger) obj);
                }
                break;
            case MESSAGE_FILE:
                obj = msg.obj;
                if (obj instanceof DocumentFile) {

                }
                break;
            case DIRECTORYEXCHANGEREXPECTATIONHANDLE:
                obj = msg.obj;
                if (obj instanceof DocumentFileOverarchPortDTO) {
                    dto = (DocumentFileOverarchPortDTO) obj;
//                    cache.setDirectoryExchanger((DirectoryExchangerHostDeliver) obj);
                    if (contextDrivenIF != null) {
                        contextDrivenIF.onRetrieveDocumentFiles(dto);
//                        contextDrivenIF.onRetrieveDocumentFile((DocumentFile) obj);
                    }
                    //display the directory on a front-end file picker

                }

                break;
            case MESSAGE_CHOSEN_DOCUMENT_FILE:
                obj = msg.obj;
                if (obj instanceof DocumentFile) {
                    //this should trigger a host based action as a client has selected a virtual document
                    //given I am a virtual directory to some LAN network; I want to then pass the choice back to the server file system
                    //
                }
                break;

        }
        return true;
    }


    //method to encode uuid to 32 characters
    //method to salt and secret the uuids
    //method to desalt uuids
    //method to ensure server name is compliant with my server nomenclature
    public String encodeRandomUUIDForServername() throws NoSuchAlgorithmException, InvalidKeySpecException {
        UUID uuid = UUID.randomUUID();
        int iterations = 550;
        char[] chars = uuid.toString().toCharArray();
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 32 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
//        return toHex(hash);
        return toHex(salt) + ":" + toHex(hash);
    }

    public String encodePasswordForServer(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UUID uuid = UUID.randomUUID();
        int iterations = 657;
        char[] chars = uuid.toString().toCharArray();
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 32 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
//        return toHex(hash);
        return toHex(salt) + ":" + toHex(hash);
    }

    public boolean validatePassword(String storedPassword, String encryptedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 657;
        String[] parts = encryptedPassword.split(":");
        byte[] salt = fromHex(parts[0]);
        byte[] hash = fromHex(parts[1]);
        PBEKeySpec spec = new PBEKeySpec(storedPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    public String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public byte[] fromHex(String hex) {

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public void sendVirtualDocumentFileThroughHandler(DocumentFileOverarchPortDTO dto) {
        //as a client startup a second tier directory to pass back a message to the first tier's group handler
        //at this point the dto.chosenDocumentFile should point to a tangible file on the exchange
        //pass back a consume call on tier 2 group owner
    }
}

