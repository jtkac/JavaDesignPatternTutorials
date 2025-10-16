package ca.useful.nfcclient;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import ca.useful.nfcclient.databinding.ActivityExampleGBinding;
import cache.CacheExampleG;
import connector.ConnectorExampleG;
import service.ServiceExampleG;

public class ExampleActivityG extends BaseActivity implements ConnectorExampleG {
    @Inject
    ServiceExampleG service;

    private CacheExampleG cache;
    private ActivityExampleGBinding binding;

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_example_g);
        service.attach(this, cache);
    }

    @Override
    public void supplyDependencies() {
        ((ApplicationExtension)getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void show(CacheExampleG cache) {
        this.cache = cache;
        binding.setCache(cache);

        service.performRoomTest();
    }
}
