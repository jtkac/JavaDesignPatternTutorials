package cache;

import androidx.databinding.Bindable;

import com.example.javadesignpatterntutorials.BR;

public class CacheExampleH extends BaseCache{
    private String baseDirectoryName;

    @Bindable
    public String getBaseDirectoryName() {
        return baseDirectoryName;
    }

    public void setBaseDirectoryName(String baseDirectoryName) {
        this.baseDirectoryName = baseDirectoryName;
        notifyPropertyChanged(BR.baseDirectoryName);
    }
}
