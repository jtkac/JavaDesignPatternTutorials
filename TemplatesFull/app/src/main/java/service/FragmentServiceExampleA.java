package service;

import cache.CacheExampleE;
import cache.FragmentCacheExampleA;
import connector.ConnectorExampleE;
import connector.FragmentConnectorExampleA;
import utilities.UiThreadQueue;

public class FragmentServiceExampleA extends BaseCacheService<FragmentConnectorExampleA, FragmentCacheExampleA> {

    public FragmentServiceExampleA(UiThreadQueue uiThreadQueue) {
        super(uiThreadQueue);
    }

    @Override
    public void attach(FragmentConnectorExampleA connector, FragmentCacheExampleA cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new FragmentCacheExampleA();
        }
        this.cache = cache;
        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }



}
