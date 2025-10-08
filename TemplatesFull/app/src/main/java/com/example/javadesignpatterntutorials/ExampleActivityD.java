package com.example.javadesignpatterntutorials;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleDBinding;
import cache.CacheExampleD;
import connector.ConnectorExampleD;
import service.ServiceExampleD;

/**
 * Example D demonstrates the notion that altering one variable may alter a message
 * It then utilizes a recursive binding protocol from XML to Java to update the values
 */
public class ExampleActivityD extends BaseActivity implements ConnectorExampleD {
    @Inject
    ServiceExampleD service;

    private CacheExampleD cache;
    private ActivityExampleDBinding binding;


    @Override
    public void supplyDependencies() {
        ((ApplicationExtension) getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        //binding is a shorthand of all declared id based controls
        binding = DataBindingUtil.setContentView(ExampleActivityD.this, R.layout.activity_example_d);
        //service attach will permeate a bindable cache throughout the service and connector layers.
        //If the cache is uninstantiated will also instantiate a cache in the service layer
        service.attach(this, cache);
    }

    @Override
    public void show(CacheExampleD cache) {
        //rebind cache in case of instantiation which would destroy the pointer
        this.cache = cache;
        //next bind the cache to the expected pointer in xml

        cache.setFlipButtonClickEvent(v -> {
            service.flipClick();
        });

        binding.setCache(this.cache);

        //adapter will in fact work with @Bindable annotations as well

    }

}
