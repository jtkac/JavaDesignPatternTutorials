package service;

import cache.CacheExampleF;
import connector.ConnectorExampleF;
import utilities.MainLooper;

public class ServiceExampleF extends BaseCacheService<ConnectorExampleF, CacheExampleF> {

    public ServiceExampleF(MainLooper mainLooper) {
        super(mainLooper);
    }

    @Override
    public void attach(ConnectorExampleF connectableView, CacheExampleF cache) {
        super.attach(connectableView, cache);
        if (cache == null) {
            cache = new CacheExampleF();
        }
        this.cache = cache;

        mainLooper.run(() -> this.connectableView.show(this.cache));
    }
}
