package service;

import cache.CacheExampleB;
import connector.ConnectorExampleB;
import utilities.UiThreadQueue;

public class ServiceExampleB extends BaseCacheService<ConnectorExampleB, CacheExampleB> {

    public ServiceExampleB(UiThreadQueue uiThreadQueue) {
        super(uiThreadQueue);
    }

    @Override
    public void attach(ConnectorExampleB connector, CacheExampleB cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleB();
        }
        this.cache = cache;
        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }
}
