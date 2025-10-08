package com.example.javadesignpatterntutorials;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import adapterviewpager.ViewPagerExampleEStateAdapter;
import application.ApplicationExtension;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleEBinding;
import cache.CacheExampleE;
import connector.ConnectorExampleE;
import service.ServiceExampleE;

/**
 * Example Activity E encompasses a frame for fragments that is common recursive design pattern in android and ios apps\
 * In fact simply declaring fragments in the pager adapter may permeate the contents of the pager adapter to new pages
 * This means declaring a fragment in a sub setting of the activity while being able to paginate between fragments
 *
 */
public class ExampleActivityE extends BaseActivity implements ConnectorExampleE {
    @Inject
    ServiceExampleE service;

    private CacheExampleE cache;
    private ActivityExampleEBinding binding;


    @Override
    public void supplyDependencies() {
        ((ApplicationExtension) getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        //binding is a shorthand of all declared id based controls
        binding = DataBindingUtil.setContentView(ExampleActivityE.this, R.layout.activity_example_e);
        //service attach will permeate a bindable cache throughout the service and connector layers.
        //If the cache is uninstantiated will also instantiate a cache in the service layer
        service.attach(this, cache);
    }

    @Override
    public void show(CacheExampleE cache) {
        //rebind cache in case of instantiation which would destroy the pointer
        this.cache = cache;
        //next bind the cache to the expected pointer in xml
        cache.setAdapter(new ViewPagerExampleEStateAdapter(getSupportFragmentManager(), ExampleActivityE.this));
        cache.setPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //getCount will not be null as we are explicitly stating it
                for (int i = 0; i < binding.pagerE.getAdapter().getCount(); i++) {
                    if (i == position) {
                        cache.getAdapter().getItem(position).onResume();
                        //set callbacks is a convenience function related to top toolbar show / hide so we do not
                        //have to redesign a toolbar many times over
//                        setCallbacks();
                    } else {
                        cache.getAdapter().getItem(position).onPause();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.setCache(this.cache);

        binding.pagerTabsE.setupWithViewPager(binding.pagerE);


        //adapter will in fact work with @Bindable annotations as well

    }

    @Override
    public String processResourceIdToString(int resourceId) {
        return getString(resourceId);

    }
}
