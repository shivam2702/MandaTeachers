package templates.yogeshbalan.com;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Shivam on 11/22/2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
