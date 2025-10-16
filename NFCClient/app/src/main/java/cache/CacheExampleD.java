package cache;

import android.view.View;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import adapters.RecyclerAdapterExampleA;

public class CacheExampleD extends BaseCache {
    private boolean resultWhichFlips = false;
    private String mnemonicToResultFlipper;

    private View.OnClickListener flipButtonClickEvent;

    public boolean isResultWhichFlips() {
        return resultWhichFlips;
    }

    public void setResultWhichFlips(boolean resultWhichFlips) {
        this.resultWhichFlips = resultWhichFlips;
    }


    @Bindable
    public String getMnemonicToResultFlipper() {
        return mnemonicToResultFlipper;
    }

    public void setMnemonicToResultFlipper(String mnemonicToResultFlipper) {
        this.mnemonicToResultFlipper = mnemonicToResultFlipper;
        notifyPropertyChanged(BR.mnemonicToResultFlipper);
    }

    @Bindable
    public View.OnClickListener getFlipButtonClickEvent() {
        return flipButtonClickEvent;
    }

    public void setFlipButtonClickEvent(View.OnClickListener flipButtonClickEvent) {
        this.flipButtonClickEvent = flipButtonClickEvent;
        notifyPropertyChanged(BR.flipButtonClickEvent);
    }
}
