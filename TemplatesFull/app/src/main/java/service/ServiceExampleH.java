package service;

import cache.CacheExampleH;
import connector.ConnectorExampleH;
import utilities.SharedPreferenceHelper;
import utilities.UiThreadQueue;

public class ServiceExampleH extends BaseCacheService<ConnectorExampleH, CacheExampleH> {

    private final SharedPreferenceHelper sharedPreferenceHelper;
    public ServiceExampleH(UiThreadQueue uiThreadQueue,
                           SharedPreferenceHelper sharedPreferenceHelper) {
        super(uiThreadQueue);
        this.sharedPreferenceHelper = sharedPreferenceHelper;
    }

    @Override
    public void attach(ConnectorExampleH connector, CacheExampleH cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleH();
        }
        this.cache = cache;
        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }

    public void putAccessFolder(String accessFolderUri) {
        sharedPreferenceHelper.get().edit().putString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, accessFolderUri).apply();
    }

    public String getAccessibleFolderUri() {
        return sharedPreferenceHelper.get().getString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, "");
    }
}
