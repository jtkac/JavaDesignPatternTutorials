package cache;

import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;

import androidx.databinding.BaseObservable;

import java.util.HashMap;
import java.util.Map;

import adapters.WifiP2PRecyclerAdapter;
import broadcast.ClientConnectionTierOne;
import broadcast.DataExchanger;
import broadcast.DirectoryExchangerHostDeliver;
import broadcast.GroupOwnerPoolTierOne;

public class BaseCache extends BaseObservable implements Cache {
    private static final long serialVersionUID = 5646936201945592318L;

    //wifi direct properties
    private BroadcastReceiver wifiDirectP2PReceiver;
    private boolean wifiP2pStateEnabled = false;
    private WifiP2pManager.Channel p2pChannel;
    private Map<String, String> buddies = new HashMap<>();

    private WifiP2pManager.DnsSdTxtRecordListener dnsSdTxtRecordListener;
    private WifiP2pManager.DnsSdServiceResponseListener dnsSdServiceResponseListener;
    private WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest;

    private WifiP2pDevice wifiP2pDevice;
    private WifiP2pConfig wifiP2pConfig;

    private WifiP2PRecyclerAdapter wifiP2PRecyclerAdapter;

    private GroupOwnerPoolTierOne groupOwnerPoolTierOne;
    private ClientConnectionTierOne clientConnectionTierOne;
    private DataExchanger dataExchanger;

    private DirectoryExchangerHostDeliver directoryExchangerHostDeliver;

    private Handler handler;

    public BroadcastReceiver getWifiDirectP2PReceiver() {
        return wifiDirectP2PReceiver;
    }

    public void setWifiDirectP2PReceiver(BroadcastReceiver wifiDirectP2PReceiver) {
        this.wifiDirectP2PReceiver = wifiDirectP2PReceiver;
    }

    public boolean isWifiP2pStateEnabled() {
        return wifiP2pStateEnabled;
    }

    public void setWifiP2pStateEnabled(boolean wifiP2pStateEnabled) {
        this.wifiP2pStateEnabled = wifiP2pStateEnabled;
    }

    public WifiP2pManager.Channel getP2pChannel() {
        return p2pChannel;
    }

    public void setP2pChannel(WifiP2pManager.Channel p2pChannel) {
        this.p2pChannel = p2pChannel;
    }

    public Map<String, String> getBuddies() {
        return buddies;
    }

    public void setBuddies(Map<String, String> buddies) {
        this.buddies = buddies;
    }

    public WifiP2pManager.DnsSdTxtRecordListener getDnsSdTxtRecordListener() {
        return dnsSdTxtRecordListener;
    }

    public void setDnsSdTxtRecordListener(WifiP2pManager.DnsSdTxtRecordListener dnsSdTxtRecordListener) {
        this.dnsSdTxtRecordListener = dnsSdTxtRecordListener;
    }

    public WifiP2pManager.DnsSdServiceResponseListener getDnsSdServiceResponseListener() {
        return dnsSdServiceResponseListener;
    }

    public void setDnsSdServiceResponseListener(WifiP2pManager.DnsSdServiceResponseListener dnsSdServiceResponseListener) {
        this.dnsSdServiceResponseListener = dnsSdServiceResponseListener;
    }

    public WifiP2pDnsSdServiceRequest getWifiP2pDnsSdServiceRequest() {
        return wifiP2pDnsSdServiceRequest;
    }

    public void setWifiP2pDnsSdServiceRequest(WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest) {
        this.wifiP2pDnsSdServiceRequest = wifiP2pDnsSdServiceRequest;
    }

    public WifiP2pConfig getWifiP2pConfig() {
        return wifiP2pConfig;
    }

    public void setWifiP2pConfig(WifiP2pConfig wifiP2pConfig) {
        this.wifiP2pConfig = wifiP2pConfig;
    }

    public WifiP2pDevice getWifiP2pDevice() {
        return wifiP2pDevice;
    }

    public void setWifiP2pDevice(WifiP2pDevice wifiP2pDevice) {
        this.wifiP2pDevice = wifiP2pDevice;
    }

    public WifiP2PRecyclerAdapter getWifiP2PRecyclerAdapter() {
        return wifiP2PRecyclerAdapter;
    }

    public void setWifiP2PRecyclerAdapter(WifiP2PRecyclerAdapter wifiP2PRecyclerAdapter) {
        this.wifiP2PRecyclerAdapter = wifiP2PRecyclerAdapter;
    }

    public GroupOwnerPoolTierOne getGroupOwnerPool() {
        return groupOwnerPoolTierOne;
    }

    public void setGroupOwnerPool(GroupOwnerPoolTierOne groupOwnerPoolTierOne) {
        this.groupOwnerPoolTierOne = groupOwnerPoolTierOne;
    }

    public ClientConnectionTierOne getClientConnection() {
        return clientConnectionTierOne;
    }

    public void setClientConnection(ClientConnectionTierOne clientConnectionTierOne) {
        this.clientConnectionTierOne = clientConnectionTierOne;
    }

    public DataExchanger getDataExchanger() {
        return dataExchanger;
    }

    public void setDataExchanger(DataExchanger dataExchanger) {
        this.dataExchanger = dataExchanger;
    }

    public Handler getHandler(Handler.Callback callback) {
        if (handler == null) {
            handler = new Handler(callback);
        }
        return handler;
    }

    public DirectoryExchangerHostDeliver getDirectoryExchanger() {
        return directoryExchangerHostDeliver;
    }

    public void setDirectoryExchanger(DirectoryExchangerHostDeliver directoryExchangerHostDeliver) {
        this.directoryExchangerHostDeliver = directoryExchangerHostDeliver;
    }
}
