package cache;

import androidx.databinding.Bindable;

import adapters.DocumentFileRecyclerAdapter;
import ca.useful.nfcclient.BR;

public class CacheExampleH extends BaseCache{
    private String baseDirectoryName;
    private DocumentFileRecyclerAdapter documentFileRecyclerAdapter;
    @Bindable
    public String getBaseDirectoryName() {
        return baseDirectoryName;
    }

    public void setBaseDirectoryName(String baseDirectoryName) {
        this.baseDirectoryName = baseDirectoryName;
        notifyPropertyChanged(BR.baseDirectoryName);
    }

    @Bindable
    public DocumentFileRecyclerAdapter getDocumentFileRecyclerAdapter() {
        return documentFileRecyclerAdapter;
    }

    public void setDocumentFileRecyclerAdapter(DocumentFileRecyclerAdapter documentFileRecyclerAdapter) {
        this.documentFileRecyclerAdapter = documentFileRecyclerAdapter;
        notifyPropertyChanged(BR.documentFileRecyclerAdapter);
    }
}
