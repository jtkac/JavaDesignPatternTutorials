package connector;

import cache.CacheExampleE;

public interface ConnectorExampleE extends ConnectableView, CacheDisplayer<CacheExampleE> {

    String processResourceIdToString(int resourceId);
}
