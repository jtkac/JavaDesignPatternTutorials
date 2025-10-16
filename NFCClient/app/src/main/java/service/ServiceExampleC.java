package service;

import java.util.ArrayList;
import java.util.List;

import ca.useful.nfcclient.R;
import cache.CacheExampleC;
import connector.ConnectorExampleC;
import utilities.MainLooper;
import utilities.StringHelper;

public class ServiceExampleC extends BaseCacheService<ConnectorExampleC, CacheExampleC> {

    private final StringHelper stringHelper;
    public ServiceExampleC(MainLooper mainLooper,
                           StringHelper stringHelper) {
        super(mainLooper);
        this.stringHelper = stringHelper;
    }

    @Override
    public void attach(ConnectorExampleC connector, CacheExampleC cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleC();
        }
        this.cache = cache;
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }

    public List<String> getDefaultList() {
        List<String> values = new ArrayList<>();
        values.add(stringHelper.getString(R.string.value_1));
        values.add(stringHelper.getString(R.string.value_2));
        values.add(stringHelper.getString(R.string.value_3));
        values.add(stringHelper.getString(R.string.value_4));
        return values;
    }
}
