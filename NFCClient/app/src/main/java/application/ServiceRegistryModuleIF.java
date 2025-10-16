package application;


import javax.inject.Singleton;

import ca.useful.nfcclient.ExampleActivityA;
import ca.useful.nfcclient.ExampleActivityB;
import ca.useful.nfcclient.ExampleActivityC;
import ca.useful.nfcclient.ExampleActivityD;
import ca.useful.nfcclient.ExampleActivityE;
import ca.useful.nfcclient.ExampleActivityF;
import ca.useful.nfcclient.ExampleActivityG;
import ca.useful.nfcclient.ExampleActivityH;
import ca.useful.nfcclient.ExampleActivityI;
import dagger.Component;
import fragments.FragmentExampleA;

@Singleton
@Component(modules = {ServiceRegistryNetModule.class, ServiceRegistryModule.class})
public interface ServiceRegistryModuleIF {

    void supply(ApplicationExtension applicationExtension);

    void supply(ExampleActivityA exampleActivityA);

    void supply(ExampleActivityB exampleActivityB);

    void supply(ExampleActivityC exampleActivityC);

    void supply(ExampleActivityD exampleActivityD);

    void supply(ExampleActivityE exampleActivityE);

    void supply(FragmentExampleA fragmentExampleA);

    void supply(ExampleActivityF exampleActivityF);

    void supply(ExampleActivityG exampleActivityG);

    void supply(ExampleActivityH exampleActivityH);

    void supply(ExampleActivityI exampleActivityI);


    //registry of various activities and fragments you want to have made your service layer available for
}
