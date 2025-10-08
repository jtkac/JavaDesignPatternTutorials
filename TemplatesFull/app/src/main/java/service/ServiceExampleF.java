package service;

import cache.CacheExampleF;
import connector.ConnectorExampleF;
import utilities.UiThreadQueue;

public class ServiceExampleF extends BaseCacheService<ConnectorExampleF, CacheExampleF> {

    public ServiceExampleF(UiThreadQueue uiThreadQueue) {
        super(uiThreadQueue);
    }

    @Override
    public void attach(ConnectorExampleF connectableView, CacheExampleF cache) {
        super.attach(connectableView, cache);
        if (cache == null) {
            cache = new CacheExampleF();
        }
        this.cache = cache;

        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }
}
