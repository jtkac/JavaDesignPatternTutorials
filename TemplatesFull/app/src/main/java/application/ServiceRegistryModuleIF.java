package application;


import javax.inject.Singleton;

import com.example.javadesignpatterntutorials.ExampleActivityA;
import com.example.javadesignpatterntutorials.ExampleActivityB;
import com.example.javadesignpatterntutorials.ExampleActivityC;
import com.example.javadesignpatterntutorials.ExampleActivityD;
import com.example.javadesignpatterntutorials.ExampleActivityE;
import com.example.javadesignpatterntutorials.ExampleActivityF;
import com.example.javadesignpatterntutorials.ExampleActivityG;
import com.example.javadesignpatterntutorials.ExampleActivityH;
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


    //registry of various activities and fragments you want to have made your service layer available for
}
