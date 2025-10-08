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
import utilities.RecursiveUtilityFunctionExampleA;
import utilities.SharedPreferenceHelper;
import utilities.StringHelper;
import utilities.UiThreadQueue;

@Module
public class ServiceRegistryModule {

    private Context appContext;

    public ServiceRegistryModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    ServiceExampleA provideServiceExampleA(UiThreadQueue uiThreadQueue) {
        return new ServiceExampleA(uiThreadQueue);
    }

    @Provides
    ServiceExampleB provideServiceExampleB(UiThreadQueue uiThreadQueue) {
        return new ServiceExampleB(uiThreadQueue);
    }

    @Provides
    ServiceExampleC provideServiceExampleC(UiThreadQueue uiThreadQueue,
                                           StringHelper stringHelper) {
        return new ServiceExampleC(uiThreadQueue, stringHelper);
    }

    @Provides
    ServiceExampleD provideServiceExampleD(UiThreadQueue uiThreadQueue,
                                           RecursiveUtilityFunctionExampleA recursiveUtilityFunctionExampleA,
                                           StringHelper stringHelper) {
        return new ServiceExampleD(uiThreadQueue, recursiveUtilityFunctionExampleA, stringHelper);
    }

    @Provides
    ServiceExampleE provideServiceExampleE(UiThreadQueue uiThreadQueue) {
        return new ServiceExampleE(uiThreadQueue);
    }

    @Provides
    FragmentServiceExampleA provideFragmentServiceExampleA(UiThreadQueue uiThreadQueue) {
        return new FragmentServiceExampleA(uiThreadQueue);
    }

    @Provides
    ServiceExampleF provideServiceExampleF(UiThreadQueue uiThreadQueue) {
        return new ServiceExampleF(uiThreadQueue);
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
    ServiceExampleG provideServiceExampleG(UiThreadQueue uiThreadQueue,
                                           DatabaseHelper databaseHelper,
                                           StringHelper stringHelper) {
        return new ServiceExampleG(uiThreadQueue, databaseHelper, stringHelper);
    }

    @Provides
    ServiceExampleH provideServiceExampleH(UiThreadQueue uiThreadQueue,
                                           SharedPreferenceHelper sharedPreferenceHelper) {
        return new ServiceExampleH(uiThreadQueue, sharedPreferenceHelper);
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
    UiThreadQueue provideUIThreadQueue() {
        return new UiThreadQueue();
    }
}
