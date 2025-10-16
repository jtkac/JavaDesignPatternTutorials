package utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {


    public static final String ACCESSIBLE_FOLDERS = "folders_accessible";
    private static final String SHARED_PREFERENCE_NAME = "example_preferences";


    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences get() {
        return sharedPreferences;
    }
}
