package service;

import com.example.javadesignpatterntutorials.R;
import cache.CacheExampleD;
import cache.CacheExampleE;
import connector.ConnectorExampleD;
import connector.ConnectorExampleE;
import utilities.RecursiveUtilityFunctionExampleA;
import utilities.StringHelper;
import utilities.UiThreadQueue;

public class ServiceExampleE extends BaseCacheService<ConnectorExampleE, CacheExampleE> {

    public ServiceExampleE(UiThreadQueue uiThreadQueue) {
        super(uiThreadQueue);
    }

    @Override
    public void attach(ConnectorExampleE connector, CacheExampleE cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleE();
        }
        this.cache = cache;
        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }

}
