package service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;

import androidx.annotation.RequiresPermission;
import androidx.documentfile.provider.DocumentFile;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

import adapters.DocumentFileRecyclerAdapter;
import cache.BaseCache;
import cache.CacheExampleI;
import connector.ConnectorExampleI;
import dataroom.dto.DocumentFileOverarchPortDTO;
import interfaces.WifiDirectOverarchingIF;
import utilities.MainLooper;
import utilities.SharedPreferenceHelper;
import utilities.WifiDirectSafeChannels;

public class ServiceExampleI extends BaseCacheService<ConnectorExampleI, CacheExampleI> {
    private final WifiDirectSafeChannels wifiDirectSafeChannels;
    private final SharedPreferenceHelper sharedPreferenceHelper;

    public ServiceExampleI(MainLooper mainLooper,
                           WifiDirectSafeChannels wifiDirectSafeChannels,
                           SharedPreferenceHelper sharedPreferenceHelper) {
        super(mainLooper);
        this.sharedPreferenceHelper = sharedPreferenceHelper;
        this.wifiDirectSafeChannels = wifiDirectSafeChannels;
    }

    @Override
    public void attach(ConnectorExampleI connector, CacheExampleI cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleI();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    public void startOneServer(String serverName, String password, Context context) {
        try {
            wifiDirectSafeChannels.startSingleCurrentServer(context, cache, new WifiDirectOverarchingIF() {

                @Override
                public void onSuccess(BaseCache baseCache) {

                }

                @Override
                public void onFailure(String message) {
                    mainLooper.run(() -> connectableView.showMessage(message));
                }

                @Override
                public void onRetrieveDocumentFile(DocumentFile documentFile) {

                }

                @Override
                public void onRetrieveDocumentFileDirectory(DocumentFileOverarchPortDTO dto) {
                    cache.setVisibleHostJoin(View.GONE);
                    cache.setVisibleDirectorySelect(View.VISIBLE);
                    cache.setDocumentFileRecyclerAdapter(new DocumentFileRecyclerAdapter(connectableView, dto));
                }
            }, serverName, password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        wifiDirectSafeChannels.shutdownAllChannels();
    }

    public void shutdownOne(Long port) {
        wifiDirectSafeChannels.shutdown(port);
    }

    public void unregisterReceiver() {
        wifiDirectSafeChannels.unregisterBroadcastReceiver();
    }

    public void chooseDocumentFile(DocumentFileOverarchPortDTO dto) {
        wifiDirectSafeChannels.sendVirtualDocumentFileThroughHandler(dto);
    }

    public void putAccessFolder(String folderUri) {
        sharedPreferenceHelper.get().edit().putString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, folderUri).apply();
    }

    public String getAccessibleFolderUri() {
        return sharedPreferenceHelper.get().getString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, "");
    }
}
