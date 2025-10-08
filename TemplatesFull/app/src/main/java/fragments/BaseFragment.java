package fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    @Override
    public void onViewCreated(View view, Bundle onSaveInstanceState) {
        super.onViewCreated(view,  onSaveInstanceState);
        supplyDependencies();
    }

    public abstract void supplyDependencies();

    protected void setEnabled(View view, boolean enabled) {
        view.setAlpha(enabled ? 1.0f : 0.5f);
        view.setEnabled(enabled);
    }
}
