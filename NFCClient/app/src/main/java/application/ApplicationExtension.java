package application;

import android.app.Application;

import androidx.room.Room;

import dataroom.DatabaseHelper;

public class ApplicationExtension extends Application {
    //logging tag optional now
    private ServiceRegistryModuleIF serviceRegistryModuleIF;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceRegistryModuleIF = DaggerServiceRegistryModuleIF.builder()
                .serviceRegistryModule(new ServiceRegistryModule(this))
                .serviceRegistryNetModule(new ServiceRegistryNetModule(this))
                //TLDR: Kotlin good for JSON pages intake
                //Java good for Bulk page intake
                //Java good for no internet access
                //React Good for No Internet Access Semi Bulk Intake

                //Due to Kotlin's inherent reflective iteration of transport language content and the
                // differential of explicit speeds over reflection it is recommended for large data bulk to not
                // utilize kotlin if offline is desired and data can be bulk loaded
                //in testing server capabilities an ORM over explicit naming showed .004 seconds per record is attributable
                //to reflection. Removing the reflection in favor of a disposition of explicit calls instead removed
                //.004 seconds out of what is likely somewhere around .00400001 seconds
                //given I am always online and may paginate at any given time Kotlin is a superior paginator for intaking
                //and consuming data. This is because it behaves more like a recycler.
                .build();
        // can set urls relative to build variants here

        serviceRegistryModuleIF.supply(this);
        //declare dagger here
    }

    public ServiceRegistryModuleIF getServiceRegistryModuleIF() {
        return serviceRegistryModuleIF;
    }
}
