package templates.yogeshbalan.com.navigationdrawertemplatee;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.navigationdrawertemplate.BuildConfig;
import templates.yogeshbalan.com.navigationdrawertemplate.GCMClientManager;
import templates.yogeshbalan.com.navigationdrawertemplate.R;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginPage extends AppCompatActivity {
    public static String department;
    public static String ID;
    public static String acc = "HOD";
    static final String TAG = "pavan";
    public static ProgressDialog progress1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    public static String dept="";
    public static String appversion = BuildConfig.VERSION_NAME;
    public boolean isUpdate = true;

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid="";
    String msg;
    public static String name = "";
    public static ProgressDialog progress;
    public static int count = 0;
    public EditText user;
    public EditText pass;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    com.rey.material.widget.CheckBox cb;
    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getRegId();
        user = (EditText) findViewById(R.id.editText_username);
        pass = (EditText) findViewById(R.id.editText2_password);
        Button btn1 = (Button) findViewById(R.id.loginbtn);
        progress1 = new ProgressDialog(LoginPage.this);
        MyAsyncTaskAppVersion versionchk = new MyAsyncTaskAppVersion();
        versionchk.execute(links.appversion);
        progress = new ProgressDialog(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        final String versionCode = BuildConfig.VERSION_NAME;
        String prefVersionCode = pref.getString("versionCode", "");
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_CONTACTS)) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_CONTACTS)) {
                            Toast.makeText(this, "I know you said no, but I'm asking again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
        }
        if(prefVersionCode.equals(versionCode))
        {

        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
            builder.setTitle("Update Log");
            builder.setMessage("1. App Name Changed to MIT CMS\n2. Attendance View Added\n3. Back Date Attendance Added\n4. Lab View Added\n5. Back Date Lab Attendance\n6. Stability Improved");
            builder.setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putString("versionCode", versionCode);
                }
            });
            AlertDialog dialog = builder.show();
            dialog.setCancelable(false);
            dialog.show();

        }
        String prefname = pref.getString("username", null);
        String prefpass = pref.getString("password", null);
        Boolean remember = pref.getBoolean("remember", true);
        Boolean keepmelogin = pref.getBoolean("keepmelogin", false);
        if (keepmelogin == true) {
            name = pref.getString("name", null);
            ID = pref.getString("id", null);
            department = pref.getString("department", null);
            acc = pref.getString("acc", null);
            Intent j = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(j);
        } else {
            editor.remove("subject1");
            editor.remove("subject2");
            editor.remove("subject3");
            editor.remove("subject4");
            editor.remove("subject5");
            editor.remove("subject6");
            context = getApplicationContext();


            user.setText(prefname);
            pass.setText(prefpass);
            cb = (com.rey.material.widget.CheckBox) findViewById(R.id.checkBox);
            cb.setChecked(remember);
            editor.commit();
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username = user.getText().toString().toLowerCase();
                    cb = (com.rey.material.widget.CheckBox) findViewById(R.id.checkBox);
                    if (cb.isChecked()) {
                        editor.putString("username", user.getText().toString().toLowerCase());
                        editor.putString("password", pass.getText().toString().toLowerCase());
                        editor.putBoolean("remember", true);
                        editor.putBoolean("keepmelogin", true);
                        editor.commit();

                    } else {
                        editor.clear();
                        editor.commit();

                    }

                    if (AppStatus.getInstance(LoginPage.this).isOnline()) {
                        progress = new ProgressDialog(LoginPage.this);
                        final MyAsyncTask task = new MyAsyncTask();
                        task.execute(links.login);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (task.getStatus() == AsyncTask.Status.RUNNING || task.getStatus() == AsyncTask.Status.PENDING)
                                    task.cancel(true);
                            }
                        }, 30000);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                        builder.setTitle("No Connection");
                        builder.setMessage("We are not Connected :( \n Please Check Your Connection and try again :) ");
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

            });
        }

    }

    public void getRegId()
    {
        GCMClientManager pushClientManager = new GCMClientManager(this, Util.SENDER_ID);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                regid=registrationId;
                Log.e("Registration id", registrationId);
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }


    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";
        String username = user.getText().toString().toLowerCase();
        String password = pass.getText().toString().toLowerCase();

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(username, "UTF-8") + "&param2=" + URLEncoder.encode(password, "UTF-8") + "&param3=" + URLEncoder.encode(regid, "UTF-8");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(text.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(text);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("We are checking for your Authentication");
            progress.setCancelable(false);
            progress.show();
        }

        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String Status = json_data.getString("Status");
                    if (Status.equals("Sucess")) {
                        name = json_data.getString("LastName");
                        ID = json_data.getString("UserName").toLowerCase();
                        String sbranch = json_data.getString("sbranch").toLowerCase();
                        acc = json_data.getString("position");
                        department = dept(sbranch);
                        dept = json_data.getString("department");
                        editor.putString("name", name);
                        editor.putString("id", ID);
                        editor.putString("department", department);
                        editor.putString("acc", acc);
                        editor.putString("dept",dept);
                        editor.commit();
                        Intent j = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(j);
                    } else if (Status.equals("Fail")) {
                        Toast.makeText(LoginPage.this, "Login Failed. Wrong Combination..", LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginPage.this, "Some Other Error", LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("LoginError:",e.getMessage());
                Toast.makeText(LoginPage.this,"Sorry our Server is down.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            progress.dismiss();
            super.onPostExecute(s);
        }
    }

    public String dept(String sbranch) {
        String departmenttest = "";

        if (sbranch.equals("ece")) {
            departmenttest = "ec";
        }

        if (sbranch.equals("cse")) {
            departmenttest = "cs";
        }

        if (sbranch.equals("ce")) {
            departmenttest = "ce";
        }

        if (sbranch.equals("fd")) {
            departmenttest = "fd";
        }

        if (sbranch.equals("me")) {
            departmenttest = "me";
        }

        if (sbranch.equals("ee")) {
            departmenttest = "ee";
        }
        return departmenttest;
    }

    public class MyAsyncTaskAppVersion extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "";
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(text.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(text);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(String s) {
            String appversiondb;
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    appversiondb = json_data.getString("version");
                    if (appversiondb.equals(appversion)) {
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                        builder.setTitle("Update Found");
                        builder.setMessage("There are some pending update of this app.\n Please Update for proper functioning");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mitbikaner.ac.in/mitteachers.apk"));
                                startActivity(i);
                            }
                        });
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Permission was granted!",
                    //        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission was denied!\nPlease accept for proper functioning",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
