package service;

import cache.CacheExampleH;
import connector.ConnectorExampleH;
import utilities.SharedPreferenceHelper;
import utilities.MainLooper;

public class ServiceExampleH extends BaseCacheService<ConnectorExampleH, CacheExampleH> {

    private final SharedPreferenceHelper sharedPreferenceHelper;
    public ServiceExampleH(MainLooper mainLooper,
                           SharedPreferenceHelper sharedPreferenceHelper) {
        super(mainLooper);
        this.sharedPreferenceHelper = sharedPreferenceHelper;
    }

    @Override
    public void attach(ConnectorExampleH connector, CacheExampleH cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleH();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }

    public void putAccessFolder(String accessFolderUri) {
        sharedPreferenceHelper.get().edit().putString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, accessFolderUri).apply();
    }

    public String getAccessibleFolderUri() {
        return sharedPreferenceHelper.get().getString(SharedPreferenceHelper.ACCESSIBLE_FOLDERS, "");
    }
}
