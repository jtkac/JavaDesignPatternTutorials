package connector;

import cache.Cache;

public interface CacheDisplayer<C extends Cache> {
    void show(C cache);
}
