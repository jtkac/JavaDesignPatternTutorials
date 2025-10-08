package service;

import com.example.javadesignpatterntutorials.R;
import cache.CacheExampleG;
import connector.ConnectorExampleG;
import dataroom.DatabaseHelper;
import dataroom.ExampleDBOLE;
import utilities.StringHelper;
import utilities.UiThreadQueue;

public class ServiceExampleG extends BaseCacheService<ConnectorExampleG, CacheExampleG> {
    private final DatabaseHelper databaseHelper;
    private final StringHelper stringHelper;
    public ServiceExampleG(UiThreadQueue uiThreadQueue,
                           DatabaseHelper databaseHelper,
                           StringHelper stringHelper) {
        super(uiThreadQueue);
        this.databaseHelper = databaseHelper;
        this.stringHelper = stringHelper;
    }

    @Override
    public void attach(ConnectorExampleG connector, CacheExampleG cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleG();
        }
        this.cache = cache;

        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }

    public void performRoomTest() {

        ExampleDBOLE example = new ExampleDBOLE();
        example.setName(stringHelper.getString(R.string.hello_world));
        example.setDescription(stringHelper.getString(R.string.value_1));
        databaseHelper.exampleDbOLE().insertAll(example);

        cache.setWelcomeText(example.getName());
    }
}
