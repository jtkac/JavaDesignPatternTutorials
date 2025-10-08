package cache;

import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.viewpager.widget.ViewPager;

import adapterviewpager.ViewPagerExampleEStateAdapter;

public class CacheExampleE extends BaseCache {
    private ViewPagerExampleEStateAdapter adapter;
    private ViewPager.OnPageChangeListener pageChangeListener;

    @Bindable
    public ViewPagerExampleEStateAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ViewPagerExampleEStateAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public ViewPager.OnPageChangeListener getPageChangeListener() {
        return pageChangeListener;
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
        notifyPropertyChanged(BR.pageChangeListener);
    }
}
