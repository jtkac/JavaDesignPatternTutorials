package interfaces;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.documentfile.provider.DocumentFile;

import java.util.List;
import java.util.Map;

import cache.BaseCache;
import dataroom.dto.DocumentFileOverarchPortDTO;

public interface WifiDirectOverarchingIF {
    void onSuccess(BaseCache baseCache);
    void onFailure(String message);

    void onRetrieveDocumentFile(DocumentFile documentFile);
    void onRetrieveDocumentFileDirectory(DocumentFileOverarchPortDTO dto);
}
