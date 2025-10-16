package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import application.ApplicationExtension;
import ca.useful.nfcclient.R;
import ca.useful.nfcclient.databinding.FragmentExampleABinding;
import cache.FragmentCacheExampleA;
import connector.FragmentConnectorExampleA;
import service.FragmentServiceExampleA;

public class FragmentExampleA extends BaseFragment implements FragmentConnectorExampleA {
    @Inject
    FragmentServiceExampleA service;

    private FragmentCacheExampleA cache;
    private FragmentExampleABinding binding;

    public static FragmentExampleA newInstance() {
        FragmentExampleA fragment = new FragmentExampleA();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle inState) {
        super.onCreateView(inflater, parent, inState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_example_a, parent, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle inState) {
        super.onViewCreated(view, inState);
        service.attach(this, cache);
    }

    @Override
    public void supplyDependencies() {
        ((ApplicationExtension)getActivity().getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void show(FragmentCacheExampleA cache) {
        this.cache = cache;

        binding.setCache(cache);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cache != null) {
            show(cache);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
