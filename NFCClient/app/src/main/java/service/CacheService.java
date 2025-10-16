package service;

import cache.Cache;
import connector.ConnectableView;

public interface CacheService<CV extends ConnectableView, C extends Cache> extends Service<CV> {
    void attach(CV cacheDisplayer, C cache);
}
