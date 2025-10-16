package service;

import connector.ConnectableView;

public interface Service<CV extends ConnectableView> {
    void attach(CV view);

    void detach();
}
