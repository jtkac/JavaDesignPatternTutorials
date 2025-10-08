package service;

import com.example.javadesignpatterntutorials.R;
import cache.CacheExampleC;
import cache.CacheExampleD;
import connector.ConnectorExampleC;
import connector.ConnectorExampleD;
import utilities.RecursiveUtilityFunctionExampleA;
import utilities.StringHelper;
import utilities.UiThreadQueue;

public class ServiceExampleD extends BaseCacheService<ConnectorExampleD, CacheExampleD> {

    //declare recursive service
    //final optional
    private final RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA;

    private final StringHelper stringHelper;

    public ServiceExampleD(UiThreadQueue uiThreadQueue,
                           RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA,
                           StringHelper stringHelper) {
        super(uiThreadQueue);
        this.recursiveUtilityFunctionExampleA = recursiveUtilityFunctionExampleA;
        this.stringHelper = stringHelper;
        //when you provide this function through the service registry simply utilize inbuilt dagger functions
    }

    @Override
    public void attach(ConnectorExampleD connector, CacheExampleD cache) {
        super.attach(connector, cache);
        if (cache == null) {
            cache = new CacheExampleD();
        }
        this.cache = cache;
        uiThreadQueue.run(() -> this.connectableView.show(this.cache));
    }

    public void flipClick() {
        cache.setResultWhichFlips(recursiveUtilityFunctionExampleA.givenSomeConditionIsThisOrNot(cache.isResultWhichFlips()));

        cache.setMnemonicToResultFlipper(cache.isResultWhichFlips() ? stringHelper.getString(R.string.yes_flip_click) : stringHelper.getString(R.string.no_flip_click));
    }
}
