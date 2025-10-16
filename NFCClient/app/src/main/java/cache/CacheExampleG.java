package cache;

import androidx.databinding.Bindable;

import ca.useful.nfcclient.BR;

public class CacheExampleG extends BaseCache{
    private String welcomeText;

    @Bindable
    public String getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
        notifyPropertyChanged(BR.welcomeText);
    }
}
