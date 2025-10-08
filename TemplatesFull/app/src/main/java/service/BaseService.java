package service;

import connector.ConnectableView;
import utilities.UiThreadQueue;

public class BaseService<CV extends ConnectableView> implements Service<CV> {

    protected CV connectableView;
    protected final UiThreadQueue uiThreadQueue;

    public BaseService(UiThreadQueue uiThreadQueue) {
        this.uiThreadQueue = uiThreadQueue;
    }

    @Override
    public void attach(CV view) {
        this.connectableView = view;
        uiThreadQueue.setEnabled(true);
    }

    @Override
    public void detach() {
        uiThreadQueue.setEnabled(false);
        connectableView = null;
    }

    public UiThreadQueue getUiThreadQueue() {
        return uiThreadQueue;
    }
}
