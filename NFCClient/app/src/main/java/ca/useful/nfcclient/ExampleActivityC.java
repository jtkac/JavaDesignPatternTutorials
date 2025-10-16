package ca.useful.nfcclient;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import adapters.RecyclerAdapterExampleA;
import application.ApplicationExtension;
import ca.useful.nfcclient.databinding.ActivityExampleCBindableAdapterBinding;
import cache.CacheExampleC;
import connector.ConnectorExampleC;
import connector.ConnectorExampleC;
import service.ServiceExampleC;

/**
 *
 */
public class ExampleActivityC extends BaseActivity implements ConnectorExampleC {
    @Inject
    ServiceExampleC service;

    private CacheExampleC cache;
    private ActivityExampleCBindableAdapterBinding binding;


    @Override
    public void supplyDependencies() {
        ((ApplicationExtension) getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        //binding is a shorthand of all declared id based controls
        binding = DataBindingUtil.setContentView(ExampleActivityC.this, R.layout.activity_example_c_bindable_adapter);
        //service attach will permeate a bindable cache throughout the service and connector layers.
        //If the cache is uninstantiated will also instantiate a cache in the service layer
        service.attach(this, cache);
    }

    @Override
    public void show(CacheExampleC cache) {
        //rebind cache in case of instantiation which would destroy the pointer
        this.cache = cache;
        //next bind the cache to the expected pointer in xml
        List<String> list = service.getDefaultList();
        cache.setAdapter(new RecyclerAdapterExampleA(this, list));
        binding.setCache(this.cache);


        //adapter will in fact work with @Bindable annotations as well

    }

    @Override
    public void doClickOnItem(String item) {
        //perform work on var item
        showToast(item);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(ExampleActivityC.this, text, Toast.LENGTH_SHORT).show();
    }
}
