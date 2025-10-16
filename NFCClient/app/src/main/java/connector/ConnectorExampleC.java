package connector;

import cache.CacheExampleB;
import cache.CacheExampleC;

public interface ConnectorExampleC extends ConnectableView, CacheDisplayer<CacheExampleC> {
    void doClickOnItem(String item);
    void showToast(String text);
}
