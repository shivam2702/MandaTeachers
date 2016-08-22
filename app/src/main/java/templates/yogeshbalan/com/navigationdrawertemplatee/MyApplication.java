package templates.yogeshbalan.com.navigationdrawertemplatee;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shivam on 8/9/2015.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

}
