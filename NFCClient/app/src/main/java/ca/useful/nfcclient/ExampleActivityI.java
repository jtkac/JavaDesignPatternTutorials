package ca.useful.nfcclient;

import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.documentfile.provider.DocumentFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import adapters.DocumentFileRecyclerAdapter;
import adapters.WifiDirectServerAdapter;
import application.ApplicationExtension;
import ca.useful.nfcclient.databinding.ActivityExampleIBinding;
import ca.useful.nfcclient.databinding.ViewServerConfigBinding;
import cache.CacheExampleI;
import connector.ConnectorExampleI;
import constants.RequestCodes;
import dataroom.dto.DocumentFileOverarchPortDTO;
import service.ServiceExampleI;

/**
 * Example Activity I makes use out of some wifi direct framework. Wifi direct operates by one phone starting a group for subsequent phones to connect to in a local area network fashion
 */
public class ExampleActivityI extends BaseActivity implements ConnectorExampleI {
    @Inject
    ServiceExampleI service;

    private CacheExampleI cache;
    private ActivityExampleIBinding binding;
    private final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";

    @Override
    public void onCreate(Bundle instate) {
        super.onCreate(instate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_example_i);
        service.attach(this, cache);
    }

    @Override
    public void supplyDependencies() {
        ((ApplicationExtension) getApplication()).getServiceRegistryModuleIF().supply(this);
    }

    @Override
    public void show(CacheExampleI cache) {
        this.cache = cache;

        String uri = service.getAccessibleFolderUri();
        if (!uri.isEmpty()) {
            cache.setWifiDirectServerAdapter(new WifiDirectServerAdapter(this, new HashMap<>()));
            binding.setCache(cache);
            binding.aeiStartPotential.setOnClickListener(v -> {
                checkPermissionOrStartServer();

            });
        } else {
            requestDocumentTreePermissions();
        }

    }

    private void checkPermissionOrStartServer() {
        //33 is nearby wifi devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.NEARBY_WIFI_DEVICES}, RequestCodes.permissionWifiDeviceAndLocation);
            } else {
                service.startOneServer("", "", this);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE}, RequestCodes.permissionWifiDeviceAndLocation);
            } else {
                service.startOneServer("", "", this);

//                askForServerName();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void askForServerName() {
        AlertDialog.Builder wifiServerBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.view_server_config, null);
        ViewServerConfigBinding binding = ViewServerConfigBinding.bind(view);
        wifiServerBuilder.setView(binding.getRoot());

        wifiServerBuilder.setPositiveButton("Create", null);
        wifiServerBuilder.setNegativeButton("Cancel", null);
        wifiServerBuilder.setCancelable(false);
        AlertDialog dialog = wifiServerBuilder.show();


        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (binding.vscServerName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Must have a valid server name", Toast.LENGTH_SHORT).show();

            }
//            if (binding.vscField.getText().toString().isEmpty()) {
//                Toast.makeText(this, "Must have a valid password", Toast.LENGTH_SHORT).show();
//                return;
//            }
            //ensure standards
//            if (binding.vscServerName.getText().toString().length() > 3) {
//                Toast.makeText(this, "Must have at least three characters", Toast.LENGTH_SHORT).show();
//
//            }
            service.startOneServer(binding.vscServerName.getText().toString(), "", this);
            //ensure capability to stop existing groups

            dialog.dismiss();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCodes.permissionWifiDeviceAndLocation) {
            checkPermissionOrStartServer();
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage("Select a Directory to share from / into")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        ExampleActivityI.this.getPrimaryVolume().createOpenDocumentTreeIntent();
                        Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        // Optionally, specify a URI for the directory that should be opened in
                        // the system file picker when it loads.
                        intent2.addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
//                        intent2.putExtra(EXTRA_INITIAL_URI, rootUri);
                        startActivityForResult(intent2, 99);
                    }
                })
                .setCancelable(false)
                .show();
//        } else {
//
//        }

    }

    @Override
    public void showMessage(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    @Override
    public void stopServerOnPort(Long port) {
        service.shutdownOne(port);
    }

    @Override
    public void chooseDocumentFile(DocumentFileOverarchPortDTO dto) {
        service.chooseDocumentFile(dto);
    }

    @Override
    public void onPause() {
        service.unregisterReceiver();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        service.shutdown();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
