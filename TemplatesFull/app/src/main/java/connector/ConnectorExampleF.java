package connector;

import cache.CacheExampleF;

public interface ConnectorExampleF extends ConnectableView, CacheDisplayer<CacheExampleF> {
    void showToast(String text);
}
