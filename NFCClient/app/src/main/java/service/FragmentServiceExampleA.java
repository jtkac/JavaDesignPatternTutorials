package service;

import cache.FragmentCacheExampleA;
import connector.FragmentConnectorExampleA;
import utilities.MainLooper;

public class FragmentServiceExampleA extends BaseCacheService<FragmentConnectorExampleA, FragmentCacheExampleA> {

    public FragmentServiceExampleA(MainLooper mainLooper) {
        super(mainLooper);
    }

    @Override
    public void attach(FragmentConnectorExampleA connector, FragmentCacheExampleA cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new FragmentCacheExampleA();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }



}
