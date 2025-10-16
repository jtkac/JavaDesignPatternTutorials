package service;

import cache.CacheExampleE;
import connector.ConnectorExampleE;
import utilities.MainLooper;

public class ServiceExampleE extends BaseCacheService<ConnectorExampleE, CacheExampleE> {

    public ServiceExampleE(MainLooper mainLooper) {
        super(mainLooper);
    }

    @Override
    public void attach(ConnectorExampleE connector, CacheExampleE cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleE();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }

}
