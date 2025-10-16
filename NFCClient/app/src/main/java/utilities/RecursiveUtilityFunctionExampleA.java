package utilities;

import android.content.Context;

public class RecursiveUtilityFunctionExampleA {
    private Context context;
    //include other contextual variables such as Database etc.
    //any service register to the service registry

    public RecursiveUtilityFunctionExampleA(Context context) {
        this.context = context;
    }

    public boolean givenSomeConditionIsThisOrNot(boolean start) {
        //do work
        return !start;
    }
}
