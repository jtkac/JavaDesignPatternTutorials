package application;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dataroom.DatabaseHelper;
import service.FragmentServiceExampleA;
import service.ServiceExampleA;
import service.ServiceExampleB;
import service.ServiceExampleC;
import service.ServiceExampleD;
import service.ServiceExampleE;
import service.ServiceExampleF;
import service.ServiceExampleG;
import service.ServiceExampleH;
import service.ServiceExampleI;
import utilities.MainLooper;
import utilities.NFCConnectionServer;
import utilities.RecursiveUtilityFunctionExampleA;
import utilities.SharedPreferenceHelper;
import utilities.StringHelper;
import utilities.WifiDirectConnectionHelper;
import utilities.WifiDirectSafeChannels;

@Module
public class ServiceRegistryModule {

    private Context appContext;

    public ServiceRegistryModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    ServiceExampleA provideServiceExampleA(MainLooper mainLooper) {
        return new ServiceExampleA(mainLooper);
    }

    @Provides
    ServiceExampleB provideServiceExampleB(MainLooper mainLooper) {
        return new ServiceExampleB(mainLooper);
    }

    @Provides
    ServiceExampleC provideServiceExampleC(MainLooper mainLooper,
                                           StringHelper stringHelper) {
        return new ServiceExampleC(mainLooper, stringHelper);
    }

    @Provides
    ServiceExampleI provideServiceExampleI(MainLooper mainLooper,
                                           WifiDirectSafeChannels server,
                                           SharedPreferenceHelper sharedPreferenceHelper) {
        return new ServiceExampleI(mainLooper, server, sharedPreferenceHelper);
    }

    @Provides
    ServiceExampleD provideServiceExampleD(MainLooper mainLooper,
                                           RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA,
                                           StringHelper stringHelper) {
        return new ServiceExampleD(mainLooper, recursiveUtilityFunctionExampleA, stringHelper);
    }

    @Provides
    ServiceExampleE provideServiceExampleE(MainLooper mainLooper) {
        return new ServiceExampleE(mainLooper);
    }

    @Provides
    FragmentServiceExampleA provideFragmentServiceExampleA(MainLooper mainLooper) {
        return new FragmentServiceExampleA(mainLooper);
    }

    @Provides
    ServiceExampleF provideServiceExampleF(MainLooper mainLooper) {
        return new ServiceExampleF(mainLooper);
    }

    @Provides
    NFCConnectionServer provideNFCConnectionServer() {
        return new NFCConnectionServer(appContext);
    }

    @Provides
    @Singleton
    WifiDirectSafeChannels provideWifiDirectSafeChannels(StringHelper stringHelper,
                                                         SharedPreferenceHelper sharedPreferenceHelper) {
        return new WifiDirectSafeChannels(stringHelper, sharedPreferenceHelper);
    }

    @Provides
    RecursiveUtilityFunctionExampleA provideRecursiveUtilityFunctionExampleA() {
        //this makes the recursive utility function a plugin to the service layer of the application
        //say for example we want to connect the two will create an example d to demonstrate this
        return new RecursiveUtilityFunctionExampleA(appContext);
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(StringHelper stringHelper) {
        return Room.databaseBuilder(appContext, DatabaseHelper.class, "room-example-db")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .build();
        //ThreadLocal implementation?
    }

    @Provides
    ServiceExampleG provideServiceExampleG(MainLooper mainLooper,
                                           DatabaseHelper databaseHelper,
                                           StringHelper stringHelper) {
        return new ServiceExampleG(mainLooper, databaseHelper, stringHelper);
    }

    @Provides
    ServiceExampleH provideServiceExampleH(MainLooper mainLooper,
                                           SharedPreferenceHelper sharedPreferenceHelper) {
        return new ServiceExampleH(mainLooper, sharedPreferenceHelper);
    }

    @Provides
    SharedPreferenceHelper provideSharedPreferenceHelper() {
        return new SharedPreferenceHelper(appContext);
    }

    @Provides
    StringHelper provideStringHelper() {
        return new StringHelper(appContext);
    }

    @Provides
    MainLooper provideUIThreadQueue() {
        return new MainLooper();
    }
}
