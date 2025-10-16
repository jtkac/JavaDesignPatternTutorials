package utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;

import androidx.documentfile.provider.DocumentFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class DocumentFileUtilities {

    public static DocumentFile findFileInDirectoryMatchingName(Context mContext, Uri mUri, String name) {
        final ContentResolver resolver = mContext.getContentResolver();
        final Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(mUri,
                DocumentsContract.getDocumentId(mUri));
        Cursor c = null;
        try {
            c = resolver.query(childrenUri, new String[]{
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.COLUMN_LAST_MODIFIED

            }, DocumentsContract.Document.COLUMN_DISPLAY_NAME + " LIKE '?%'", new String[]{name}, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                final String filename = c.getString(1);
                final String mimeType = c.getString(2);
                final Long lastModified = c.getLong(3);
                if (filename.contains(name)) {
                    final String documentId = c.getString(0);
                    final Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(mUri,
                            documentId);

                    return DocumentFile.fromTreeUri(mContext, documentUri);
                }
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return null;
    }

    public static InputStream openDocumentFileInputStream(Context context, DocumentFile documentFile) {
        if (documentFile != null && documentFile.exists()) {
            try {
                return context.getContentResolver().openInputStream(documentFile.getUri());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static OutputStream openDocumentFileOutputStream(Context context, DocumentFile documentFile) {
        if (documentFile != null && documentFile.exists()) {
            try {
                return context.getContentResolver().openOutputStream(documentFile.getUri());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
