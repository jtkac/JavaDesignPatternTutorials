package com.example.javadesignpatterntutorials;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleABinding;
import cache.CacheExampleA;
import connector.ConnectorExampleA;
import service.ServiceExampleA;

public class ExampleActivityA extends BaseActivity implements ConnectorExampleA {
    @Inject
    ServiceExampleA service;

    private CacheExampleA cache;
    private ActivityExampleABinding binding;


    @Override
    public void supplyDependencies() {
        ((ApplicationExtension)getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        //binding is a shorthand of all declared id based controls
        binding = DataBindingUtil.setContentView(ExampleActivityA.this, R.layout.activity_example_a);
        //service attach will permeate a bindable cache throughout the service and connector layers.
        //If the cache is uninstantiated will also instantiate a cache in the service layer
        service.attach(this, cache);
    }

    @Override
    public void show(CacheExampleA cache) {
        //rebind cache in case of instantiation which would destroy the pointer
        this.cache = cache;
        //Configuration B would have us bind the cache to our layout through the expected bindable data tag

        //we could clearly separate x layers though the bindable is technically redundant

        //programmatic setting
        binding.aeaTextHelloWorld.setText("Hello World Again!");
        //now without retrieval of controls, we can simply reference controls given we are not using data tags
        //Tech debt is predictably formed on a single layer which is advantageous

        //Given we have indefinite time would in fact complete a cycle so I am only changing on the cache layer
        //If the cache layer value changes the ui updates
        //That would be the product of using the data tag in xml for recursive changing of a pointer
    }
}
