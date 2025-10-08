package com.example.javadesignpatterntutorials;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleABinding;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleBBinding;
import cache.CacheExampleA;
import cache.CacheExampleB;
import connector.ConnectorExampleA;
import connector.ConnectorExampleB;
import service.ServiceExampleA;
import service.ServiceExampleB;

public class ExampleActivityB extends BaseActivity implements ConnectorExampleB {
    @Inject
    ServiceExampleB service;

    private CacheExampleB cache;
    private ActivityExampleBBinding binding;


    @Override
    public void supplyDependencies() {
        ((ApplicationExtension) getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        //binding is a shorthand of all declared id based controls
        binding = DataBindingUtil.setContentView(ExampleActivityB.this, R.layout.activity_example_b);
        //service attach will permeate a bindable cache throughout the service and connector layers.
        //If the cache is uninstantiated will also instantiate a cache in the service layer
        service.attach(this, cache);
    }

    @Override
    public void show(CacheExampleB cache) {
        //rebind cache in case of instantiation which would destroy the pointer
        this.cache = cache;
        //next bind the cache to the expected pointer in xml
        binding.setCache(this.cache);

        //the programmatic example from A now changes to
        cache.setHelloWorldText("Hello World New!");

        //adapter will in fact work with @Bindable annotations as well

    }
}
