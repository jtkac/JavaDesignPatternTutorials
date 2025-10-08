package cache;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import adapters.RecyclerAdapterExampleA;

public class CacheExampleC extends BaseCache {
    private RecyclerAdapterExampleA adapter;

    @Bindable
    public RecyclerAdapterExampleA getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerAdapterExampleA adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }
}
