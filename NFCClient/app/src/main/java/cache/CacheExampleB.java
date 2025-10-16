package cache;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class CacheExampleB extends BaseCache {

    private String helloWorldText;

    @Bindable
    public String getHelloWorldText() {
        return helloWorldText;
    }

    public void setHelloWorldText(String helloWorldText) {
        this.helloWorldText = helloWorldText;
        notifyPropertyChanged(BR.helloWorldText);
    }
}
