package service;

import cache.CacheExampleA;
import connector.ConnectorExampleA;
import utilities.MainLooper;

/**
 * In a service integrated with annotation processors, for the service to be of recursive functionality
 * Declare default service as BaseCacheService or BaseService
 * In the dagger component AppModule aka the part we have declared as a @module through @singleton declarator
 * instantiate this service example and add the activity you want to make available in the singleton declarator
 *
 * Any activities you declare in the singleton declarator ensure this service is now available to them :)
 */
public class ServiceExampleA extends BaseCacheService<ConnectorExampleA, CacheExampleA> {

    public ServiceExampleA(MainLooper mainLooper) {
        super(mainLooper);
    }

    @Override
    public void attach(ConnectorExampleA connectableView, CacheExampleA cache) {
        super.attach(connectableView, cache);
        if (cache == null) {
            cache = new CacheExampleA();
        }
        this.cache = cache;

        mainLooper.run(() -> this.connectableView.show(this.cache));
    }
}
