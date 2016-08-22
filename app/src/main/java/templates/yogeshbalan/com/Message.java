package templates.yogeshbalan.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Shivam on 9/21/2015.
 */
public class Message {
    public static String Title1 = "Please Wait";
    public static String Content1 = "We are getting the data....";
    public static ProgressDialog progress;
    public static ProgressDialog progress1;

    public static void Progress_pre(ProgressDialog progress) {
        progress.setTitle("Please Wait");
        progress.setMessage("Loading up your Lectures....");
        progress.show();
        progress.setCancelable(false);
    }

    public static void Progress_student(ProgressDialog progress1) {
        progress1.setTitle("Please Wait");
        progress1.setMessage("Loading up your Students....");
        progress1.show();
        progress1.setCancelable(false);
    }


    public static void NotConnected(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("No Connection");
        builder.setMessage("We are not Connected :( \n Please Check Your Connection and retry :) ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.show();
    }


}
