package ca.useful.nfcclient;

import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import ca.useful.nfcclient.databinding.ActivityExampleFBinding;
import cache.CacheExampleF;
import connector.ConnectorExampleF;
import interfaces.OnSavePressedIF;
import service.ServiceExampleF;

/**
 * Example Activity F demonstrates a bindable update to a second bindable instance which
 * shows or hides controls in a toolbar
 */
public class ExampleActivityF extends BaseActivity implements ConnectorExampleF {
    @Inject
    ServiceExampleF service;

    private CacheExampleF cache;
    private ActivityExampleFBinding binding;

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_example_f);
        service.attach(this, cache);
    }

    @Override
    public void supplyDependencies() {
        ((ApplicationExtension)getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void show(CacheExampleF cache) {
        this.cache = cache;


        cache.setSaveButtonToggleListener(v -> {
            if (cache.getOnSavePressedIF() == null) {
                cache.setOnSavePressedIF(() -> {
                    showToast("Save Clicked");

                });
            } else {
                cache.setOnSavePressedIF(null);
            }

        });


        cache.setDeleteButtonToggleListener(v -> {
            if (cache.getOnDeletePressedIF() == null) {
                cache.setOnDeletePressedIF(() -> {
                    showToast("Delete Clicked");

                });
            } else {
                cache.setOnDeletePressedIF(null);
            }

        });
        binding.setCache(cache);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
