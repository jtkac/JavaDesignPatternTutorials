package dataroom.dto;

import androidx.documentfile.provider.DocumentFile;

import java.util.List;

public class DocumentFileOverarchPortDTO {
    public Long port;
//    public List<DocumentFile> documentFiles;

    public List<DocumentDetail> documentDetails;
//    public DocumentFile chosenDocumentFile;
    public DocumentDetail chosenDocumentDetail;
    public static class DocumentDetail {
        public String name;
        public String uri;
        public String type;
    }
}
