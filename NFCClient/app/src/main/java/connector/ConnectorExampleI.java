package connector;

import androidx.documentfile.provider.DocumentFile;

import cache.CacheExampleI;
import dataroom.dto.DocumentFileOverarchPortDTO;

public interface ConnectorExampleI extends ConnectableView, CacheDisplayer<CacheExampleI> {
    void showMessage(String message);

    void stopServerOnPort(Long port);

    void chooseDocumentFile(DocumentFileOverarchPortDTO dto);
}
