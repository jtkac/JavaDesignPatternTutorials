package broadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.annotation.RequiresPermission;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import cache.BaseCache;
import cache.Cache;
import utilities.WifiDirectSafeChannels;


public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private BaseCache cache;
    private Context context;
    private WifiDirectSafeChannels.WifiDirectChannelIF channelAcquirer;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;
    private WifiP2pManager manager;

    public interface PeersIF {
        void onPeersAvailable(List<WifiP2pDevice> peers);
    }

    public WifiDirectBroadcastReceiver(Context context,
                                       WifiP2pManager.ConnectionInfoListener connectionInfoListener,
                                       BaseCache baseCache,
                                       WifiDirectSafeChannels.WifiDirectChannelIF channelAcquirer) {
        this.context = context;
        this.cache = baseCache;
        this.channelAcquirer = channelAcquirer;
        manager = (WifiP2pManager)context.getSystemService(Context.WIFI_P2P_SERVICE);
        this.connectionInfoListener = connectionInfoListener;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                cache.setWifiP2pStateEnabled(true);
            } else {
                cache.setWifiP2pStateEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed! We should probably do something about
            // that.
            if (manager != null) {
                //given we receive a list of potential peers, we may then choose one or the other corresponding peer in order to instantiate the group
                //Given we have chosen one on one device we then simply complete the "Handshake" process on the second device by choosing the corresponding
                //device that has already been chosen from. This will require consistent naming conventions
                manager.requestPeers(cache.getP2pChannel(), new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        //make configurable password pro security but its wifi direct ergo within a mile
                        //Make static password and crossreference
                        List<WifiP2pDevice> allPeers = new ArrayList<>(peerList.getDeviceList());
                        if (cache.getWifiP2PRecyclerAdapter() != null) {
                            cache.getWifiP2PRecyclerAdapter().refreshItems(allPeers);
                        }
                    }
                });
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed! We should probably do something about
            // that.
            if (manager == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP
//                view.updateConnectionText("Connected");
//                view.updateConnectionText0("Connected");
                manager.requestConnectionInfo(cache.getP2pChannel(), connectionInfoListener);
            } else {
                // It's a disconnect
//                view.updateConnectionText("Disconnected");
//                view.updateConnectionText0("Disconnected");
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

            //My device from device changed action
            WifiP2pDevice device = ((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
//
//            try {
//                createGroup(device);
//                unregisterThis();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void unregisterThis() {
        context.unregisterReceiver(this);

    }
}