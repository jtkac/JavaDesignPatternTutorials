package service;

import cache.Cache;
import connector.CacheDisplayer;
import connector.ConnectableView;
import utilities.UiThreadQueue;

public abstract class BaseCacheService<CV extends ConnectableView & CacheDisplayer<C>, C extends Cache>
        extends BaseService<CV> implements CacheService<CV, C> {

    protected C cache;

    public BaseCacheService(UiThreadQueue uiThreadQueue) {
        super(uiThreadQueue);
    }

    @Override
    public final void attach(CV connectableView) {
        attach(connectableView, null);
    }

    @Override
    public void attach(CV connectableView, C cache) {
        super.attach(connectableView);
        this.cache = cache;
    }
}