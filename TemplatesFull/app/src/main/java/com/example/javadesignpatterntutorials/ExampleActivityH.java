package com.example.javadesignpatterntutorials;

import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.documentfile.provider.DocumentFile;

import javax.inject.Inject;

import application.ApplicationExtension;
import com.example.javadesignpatterntutorials.databinding.ActivityExampleHBinding;
import cache.CacheExampleH;
import connector.ConnectorExampleH;
import service.ServiceExampleH;

public class ExampleActivityH extends BaseActivity implements ConnectorExampleH {
    @Inject
    ServiceExampleH service;

    private CacheExampleH cache;
    private ActivityExampleHBinding binding;

    private final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";


    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_example_h);
        service.attach(this, cache);
    }

    @Override
    public void supplyDependencies() {
        ((ApplicationExtension)getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void show(CacheExampleH cache) {
        this.cache = cache;
        binding.setCache(cache);

        String uri = service.getAccessibleFolderUri();
        if (!uri.isEmpty()) {
            DocumentFile accessibleDirectory = DocumentFile.fromTreeUri(this, Uri.parse(uri));
            cache.setBaseDirectoryName(accessibleDirectory != null ? accessibleDirectory.getName() : "");
        } else {
            requestDocumentTreePermissions();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            //get back the document tree URI (in this case we expect the documents root directory)
            Uri uri = data.getData();
            //now we grant permanent persistant permissions to our contentResolver and we are free to open up sub directory Uris as we please until the app is uninstalled
            service.putAccessFolder(uri.toString());
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getApplicationContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
            //simply recreate the activity although you could call the second half of on create at this point
            recreate();
        }
    }

    private void requestDocumentTreePermissions() {
        // Choose a directory using the system's file picker.
        new AlertDialog.Builder(this)
                .setMessage("Document Directory Access Message")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
                        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

                        //String startDir = "Android";
                        //String startDir = "Download"; // Not choosable on an Android 11 device
                        //String startDir = "DCIM";
                        //String startDir = "DCIM/Camera";  // replace "/", "%2F"
                        //String startDir = "DCIM%2FCamera";
                        String startDir = "Documents";
                        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

                        String scheme = uri.toString();


                        scheme = scheme.replace("/root/", "/document/");
//            scheme = scheme.replace("/root/primary", "");
                        scheme += "%3A" + startDir;

                        uri = Uri.parse(scheme);
                        Uri rootUri = DocumentsContract.buildDocumentUri(
                                EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                                uri.toString()
                        );

                        getPrimaryVolume().createOpenDocumentTreeIntent()
                                .putExtra(EXTRA_INITIAL_URI, rootUri);
                        Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        // Optionally, specify a URI for the directory that should be opened in
                        // the system file picker when it loads.
                        intent2.addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                        intent2.putExtra(EXTRA_INITIAL_URI, rootUri);
                        startActivityForResult(intent2, 99);
                    }
                })
                .setCancelable(false)
                .show();


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private StorageVolume getPrimaryVolume() {
        StorageManager sm = (StorageManager) getSystemService(STORAGE_SERVICE);
        return sm.getPrimaryStorageVolume();
    }

}
