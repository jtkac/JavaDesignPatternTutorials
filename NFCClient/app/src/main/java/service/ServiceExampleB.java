package service;

import cache.CacheExampleB;
import connector.ConnectorExampleB;
import utilities.MainLooper;

public class ServiceExampleB extends BaseCacheService<ConnectorExampleB, CacheExampleB> {

    public ServiceExampleB(MainLooper mainLooper) {
        super(mainLooper);
    }

    @Override
    public void attach(ConnectorExampleB connector, CacheExampleB cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleB();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }
}
