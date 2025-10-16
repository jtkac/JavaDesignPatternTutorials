package cache;

import android.view.View;

import androidx.databinding.Bindable;

import adapters.DocumentFileRecyclerAdapter;
import adapters.WifiDirectServerAdapter;
import ca.useful.nfcclient.BR;

public class CacheExampleI extends BaseCache {
    //wifiDirectServerAdapter
    private WifiDirectServerAdapter wifiDirectServerAdapter;

    private DocumentFileRecyclerAdapter documentFileRecyclerAdapter;

    private int visibleError = View.VISIBLE;
    private int visibleHostJoin = View.VISIBLE;
    private int visibleDirectorySelect = View.GONE;

    @Bindable
    public WifiDirectServerAdapter getWifiDirectServerAdapter() {
        return wifiDirectServerAdapter;
    }

    public void setWifiDirectServerAdapter(WifiDirectServerAdapter wifiDirectServerAdapter) {
        this.wifiDirectServerAdapter = wifiDirectServerAdapter;
        notifyPropertyChanged(BR.wifiDirectServerAdapter);
    }

    @Bindable
    public int getVisibleError() {
        return visibleError;
    }

    public void setVisibleError(int visibleError) {
        this.visibleError = visibleError;
        notifyPropertyChanged(BR.visibleError);
    }

    @Bindable
    public DocumentFileRecyclerAdapter getDocumentFileRecyclerAdapter() {
        return documentFileRecyclerAdapter;
    }

    public void setDocumentFileRecyclerAdapter(DocumentFileRecyclerAdapter documentFileRecyclerAdapter) {
        this.documentFileRecyclerAdapter = documentFileRecyclerAdapter;
        notifyPropertyChanged(BR.documentFileRecyclerAdapter);
    }

    @Bindable
    public int getVisibleHostJoin() {
        return visibleHostJoin;
    }

    public void setVisibleHostJoin(int visibleHostJoin) {
        this.visibleHostJoin = visibleHostJoin;
        notifyPropertyChanged(BR.visibleHostJoin);
    }

    @Bindable
    public int getVisibleDirectorySelect() {
        return visibleDirectorySelect;
    }

    public void setVisibleDirectorySelect(int visibleDirectorySelect) {
        this.visibleDirectorySelect = visibleDirectorySelect;
        notifyPropertyChanged(BR.visibleDirectorySelect);
    }
}
