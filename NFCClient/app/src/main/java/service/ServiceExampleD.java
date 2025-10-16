package service;

import ca.useful.nfcclient.R;
import cache.CacheExampleD;
import connector.ConnectorExampleD;
import utilities.MainLooper;
import utilities.RecursiveUtilityFunctionExampleA;
import utilities.StringHelper;

public class ServiceExampleD extends BaseCacheService<ConnectorExampleD, CacheExampleD> {

    //declare recursive service
    //final optional
    private final RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA;

    private final StringHelper stringHelper;

    public ServiceExampleD(MainLooper mainLooper,
                           RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA,
                           StringHelper stringHelper) {
        super(mainLooper);
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
        mainLooper.run(() -> this.connectableView.show(this.cache));
    }

    public void flipClick() {
        cache.setResultWhichFlips(recursiveUtilityFunctionExampleA.givenSomeConditionIsThisOrNot(cache.isResultWhichFlips()));

        cache.setMnemonicToResultFlipper(cache.isResultWhichFlips() ? stringHelper.getString(R.string.yes_flip_click) : stringHelper.getString(R.string.no_flip_click));
    }
}
