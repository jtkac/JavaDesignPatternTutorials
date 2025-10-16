package service;

import connector.ConnectableView;
import utilities.MainLooper;

public class BaseService<CV extends ConnectableView> implements Service<CV> {

    protected CV connectableView;
    protected final MainLooper mainLooper;

    public BaseService(MainLooper mainLooper) {
        this.mainLooper = mainLooper;
    }

    @Override
    public void attach(CV view) {
        this.connectableView = view;
        mainLooper.setEnabled(true);
    }

    @Override
    public void detach() {
        mainLooper.setEnabled(false);
        connectableView = null;
    }

    public MainLooper getUiThreadQueue() {
        return mainLooper;
    }
}
