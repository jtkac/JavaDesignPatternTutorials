package utilities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class NFCConnectionServer {
    private Context context;
    private final String nfcWakeLockTag = "nfcclient:NFCServerWL";
    private final long wakelockTimeout = 5000L;
    private PowerManager.WakeLock wakeLock;

//    getDefaultAdapter() is null means you do not have NFC access at runtime
//    ACTION_NDEF_DISCOVERED for broad corresponding file registrated file formats
//    ACTION_TECH_DISCOVERED for file formats not registered (ndef is maybe non descript e file?)
//    ACTION_TAG_DISCOVERED for b[]s
    /*
    ACTION_NDEF_DISCOVERED documentation from Android:
    How to register a broadcast receiver equivalent.
    <intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED"/>
    <category android:name="android.intent.category.DEFAULT"/>
   <data android:scheme="https"
              android:host="developer.android.com"
              android:pathPrefix="/index.html" />
</intent-filter>
     */

    /*
    ACTION_TECH_DISCOVERED for supportable tech formats
    <resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
    <tech-list>
        <tech>android.nfc.tech.IsoDep</tech>
        <tech>android.nfc.tech.NfcA</tech>
        <tech>android.nfc.tech.NfcB</tech>
        <tech>android.nfc.tech.NfcF</tech>
        <tech>android.nfc.tech.NfcV</tech>
        <tech>android.nfc.tech.Ndef</tech>
        <tech>android.nfc.tech.NdefFormatable</tech>
        <tech>android.nfc.tech.MifareClassic</tech>
        <tech>android.nfc.tech.MifareUltralight</tech>
    </tech-list>
</resources>

<activity>
...
<intent-filter>
    <action android:name="android.nfc.action.TECH_DISCOVERED"/>
</intent-filter>

<meta-data android:name="android.nfc.action.TECH_DISCOVERED"
    android:resource="@xml/nfc_tech_filter" />
...
</activity>
     */

    /*
    Likely frowned upon
    ACTION_TAG_DISCOVERED For raw data transfers
     */
    /*
<intent-filter>
    <action android:name="android.nfc.action.TAG_DISCOVERED"/>
</intent-filter>
//register service + broadcast receiver as alternative
     */

    public interface NFCServerCallbacks {
        void onConnect();
        void onError(String message);
    }

    public NFCConnectionServer(Context context) {
        this.context = context;
    }

    public boolean startupNFCServer() {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.LOCATION_MODE_FOREGROUND_ONLY, nfcWakeLockTag);
        wakeLock.acquire(wakelockTimeout);

        return true;
    }

    public boolean shutdownNFCServer() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        return true;
    }
}
