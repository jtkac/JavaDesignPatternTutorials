package utilities;

import android.content.Context;

public class StringHelper {
    private Context context;

    public StringHelper(Context context) {
        this.context = context;
    }

    public String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
