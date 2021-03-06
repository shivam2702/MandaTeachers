package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterOldLab;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.labRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackDateLab extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView recyclerView;
    public static RecyclerAdapterOldLab adapter1;
    public static String LabName;
    public static String LectureID;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static ProgressDialog progress3;
    public static ArrayList<String> LABID = new ArrayList<String>();
    public static ArrayList<String> LABNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static ArrayList<String> att = new ArrayList<String>();
    public static ArrayList<String> labb = new ArrayList<String>();
    public static ArrayList<String> viva = new ArrayList<String>();
    public static ArrayList<String> file = new ArrayList<String>();
    public static ArrayList<String> uni = new ArrayList<String>();
    public static String lectno = "999";
    public static Button datebtn;
    public static View rootView;
    public static String date;
    public static String reason;
    public static String attendanceShow = "";
    public static Button submitAttendance;
    boolean update;
    boolean chk;
    private String mParam1;
    private String mParam2;

    public BackDateLab() {
        // Required empty public constructor
    }

    public static BackDateLab newInstance(String param1, String param2) {
        BackDateLab fragment = new BackDateLab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static List<labRecycler> getData() {
        List<labRecycler> data = new ArrayList<>();
        for (int i = 0; i < STUDENTNAME.size(); i++) {
            labRecycler current = new labRecycler();
            current.studentname = STUDENTNAME.get(i);
            current.Studentroll = STUDENTROLLNO.get(i);
            data.add(current);
        }
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_back_date_lab, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerLabBackDate);
        progress = new ProgressDialog(getContext());
        progress1 = new ProgressDialog(getContext());
        progress2 = new ProgressDialog(getContext());
        progress3 = new ProgressDialog(getContext());
        datebtn = (Button) rootView.findViewById(R.id.BackDateLabBtn);
        submitAttendance = (Button) rootView.findViewById(R.id.submitAttendance);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            task.execute(links.labsubjectlistlink);
        } else {
            Message.NotConnected(getActivity());
        }

        datebtn = (Button) rootView.findViewById(R.id.BackDateLabBtn);
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar();
            }
        });
        submitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTaskAttendanceUpdateOne updateOne= new MyAsyncTaskAttendanceUpdateOne();
                updateOne.execute(links.LabattendancesubmitlinkONEBack);
            }
        });
        return rootView;
    }

    void calendar() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new mDateSetListener(), mYear, mMonth, mDay);
        dialog.setCancelable(false);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    public class MyAsyncTaskLecture extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            Message.Progress_pre(progress);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(LoginPage.ID, "UTF-8");
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

        protected void onPostExecute(String s) {
            try {
                LABID.clear();
                LABNAME.clear();
                JSONArray jArray = new JSONArray(response);
                LABID.add("EMPTY");
                LABNAME.add("Please Select Lab");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LABID.add(json_data.getString("LabID"));
                    LABNAME.add(json_data.getString("Labname"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
            Spinner spinner = (Spinner) rootView.findViewById(R.id.labsubjectBack);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LABNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    recyclerView.setVisibility(View.GONE);
                    Button dateselect = (Button) rootView.findViewById(R.id.BackDateLabBtn);
                    dateselect.setText("Select Date");
                    if (pos == 0) {
                        recyclerView.setVisibility(View.GONE);
                        submitAttendance.setVisibility(View.INVISIBLE);
                        datebtn.setVisibility(View.GONE);
                        datebtn.setText("Select Date");
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Lab") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    } else {
                        datebtn.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        submitAttendance.setVisibility(View.INVISIBLE);
                        LabName = LABNAME.get(pos);
                        LectureID = LABID.get(pos);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            String monthdate = "";
            switch (mDay) {
                case 1:
                    monthdate = "01";
                    break;
                case 2:
                    monthdate = "02";
                    break;
                case 3:
                    monthdate = "03";
                    break;
                case 4:
                    monthdate = "04";
                    break;
                case 5:
                    monthdate = "05";
                    break;
                case 6:
                    monthdate = "06";
                    break;
                case 7:
                    monthdate = "07";
                    break;
                case 8:
                    monthdate = "08";
                    break;
                case 9:
                    monthdate = "09";
                    break;
                default:
                    monthdate = "" + mDay;
            }
            String month = "";
            switch (mMonth) {
                case 0:
                    month = "01";
                    break;
                case 1:
                    month = "02";
                    break;
                case 2:
                    month = "03";
                    break;
                case 3:
                    month = "04";
                    break;
                case 4:
                    month = "05";
                    break;
                case 5:
                    month = "06";
                    break;
                case 6:
                    month = "07";
                    break;
                case 7:
                    month = "08";
                    break;
                case 8:
                    month = "09";
                    break;
                case 9:
                    month = "10";
                    break;
                case 10:
                    month = "11";
                    break;
                case 11:
                    month = "12";
                    break;

                default:
                    month = "" + mMonth;
            }
            date = monthdate + "/" + (month) + "/" + mYear;
            datebtn.setText(date + "");
            recyclerView.setVisibility(View.VISIBLE);
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                MyAsyncTaskstudentlist task1 = new MyAsyncTaskstudentlist();
                task1.execute(links.Labattendancecheckback);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("No Connection");
                builder.setMessage("We are not Connected :( \n Please Check Your Connection and retry :) ");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog1 = builder.show();
                TextView messageText = (TextView) dialog1.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog1.show();
            }

        }
    }

    public class MyAsyncTaskstudentlist extends AsyncTask<String, Void, String> {
        public int count = 0;
        String response = "";

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "select1=" + URLEncoder.encode(LabName, "UTF-8") + "&dateBack=" + date;
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
            Log.e("Response:", response);
            return response;
        }

        protected void onPreExecute() {
            Message.Progress_student(progress1);
        }

        public void onPostExecute(String s) {
            STUDENTNAME.clear();
            STUDENTROLLNO.clear();
            att.clear();
            labb.clear();
            file.clear();
            uni.clear();
            viva.clear();
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data4 = jArray.getJSONObject(i);
                    try {
                        STUDENTNAME.add(i, json_data4.getString("sname"));
                        STUDENTROLLNO.add(i, json_data4.getString("srollno"));
                        att.add(i, "0");
                        labb.add(i, "0");
                        file.add(i, "0");
                        uni.add(i, "0");
                        viva.add(i, "0.00");
                        lectno = "999";
                        attendanceShow = "NOTFOUND";
                        Log.e("TRY", ".");
                    } catch (JSONException e) {
                        attendanceShow = "FOUND";
                        e.printStackTrace();
                        Log.e("CATCH", ".");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error ", e.getMessage());
            }
            super.onPostExecute(s);
            progress1.dismiss();
            if (attendanceShow.equals("FOUND")) {
                recyclerView.setVisibility(View.GONE);
                submitAttendance.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Attendance Already Found", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Late Attendance?");
                alertDialog.setMessage("Enter Reason");

                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                reason = input.getText().toString();
                                recyclerView.setVisibility(View.VISIBLE);
                                submitAttendance.setVisibility(View.VISIBLE);
                                datebtn.setVisibility(View.VISIBLE);
                                adapter1 = new RecyclerAdapterOldLab(getActivity(), getData());
                                recyclerView.setAdapter(adapter1);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }
                        });
                alertDialog.show();
            }
        }
    }

    public class MyAsyncTaskAttendanceUpdateOne extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress2.setTitle("Updating");
            progress2.setMessage("Updating Attendance Please wait....");
            progress2.show();
            progress2.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext = "select1=" + URLEncoder.encode(LectureID, "UTF-8") +
                        "&select2=" + URLEncoder.encode(LoginPage.ID, "UTF-8") +
                        "&select3=" + URLEncoder.encode(reason, "UTF-8") +
                        "&select4=" + URLEncoder.encode(date, "UTF-8");
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(updatetext.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(updatetext);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        public void onPostExecute(String s) {
            String status = "";
            super.onPostExecute(s);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    status = json_data.getString("status");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progress2.dismiss();
            // check for Internet status
            if (status.equals("pass")) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    MyAsyncTaskAttendanceUpdateTwo task = new MyAsyncTaskAttendanceUpdateTwo();
                    task.execute(links.LabattendancesubmitlinkTWOBack);
                } else {
                    Message.NotConnected(getActivity());
                }
            }
            if (status.equals("fail")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error");
                builder.setMessage("There is some error please try again.\nContinue facing the problem then use portal and contact Developer.");
                builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.show();
                dialog.setCancelable(false);
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
            }
        }
    }

    public class MyAsyncTaskAttendanceUpdateTwo extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress3.setTitle("Please Wait");
            progress3.setMessage("We are updating your attendance records");
            progress3.show();
            progress3.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            for (int i = 0; i < STUDENTNAME.size() || i < STUDENTROLLNO.size(); i++) {
                try {
                    String updatetext =
                                    "memberid=" + URLEncoder.encode(LoginPage.ID, "UTF-8") +
                                    "&LabID=" + URLEncoder.encode(LectureID, "UTF-8") +
                                    "&Lectno=" + URLEncoder.encode(lectno, "UTF-8") +
                                    "&sname=" + URLEncoder.encode(STUDENTNAME.get(i), "UTF-8") +
                                    "&srollno=" + URLEncoder.encode(STUDENTROLLNO.get(i), "UTF-8") +
                                    "&att=" + URLEncoder.encode(att.get(i), "UTF-8") +
                                    "&lab=" + URLEncoder.encode(labb.get(i), "UTF-8") +
                                    "&viva=" + URLEncoder.encode(viva.get(i), "UTF-8") +
                                    "&file=" + URLEncoder.encode(file.get(i), "UTF-8") +
                                    "&uni=" + URLEncoder.encode(uni.get(i), "UTF-8") +
                                    "&latedate=" + URLEncoder.encode(date, "UTF-8");
                    url = new URL(params[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setFixedLengthStreamingMode(updatetext.getBytes().length);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(updatetext);
                    out.close();
                    Scanner inStream = new Scanner(conn.getInputStream());
                    while (inStream.hasNextLine())
                        response += (inStream.nextLine());
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }
            return response;
        }

        public void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONArray jArray = null;
            String status = "";
            try {
                jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    status = json_data.getString("status");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progress3.dismiss();
            if (status.equals("sucess")) {
                String finalvalue = "";
                for (int i = 0; i < STUDENTROLLNO.size(); i++) {
                    Float total = (Float.parseFloat(att.get(i))) + (Float.parseFloat(labb.get(i))) + (Float.parseFloat(viva.get(i))) + (Float.parseFloat(file.get(i))) + (Float.parseFloat(uni.get(i)));
                    finalvalue = finalvalue + STUDENTNAME.get(i) + "      Total=" + total + "\n\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Updated Record");
                builder.setMessage(finalvalue);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Home.newInstance("Home", "H"))
                                .commit();
                    }
                });
                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
            }
            if (status.equals("fail")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Failed");
                builder.setMessage("Contact Developer");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Home.newInstance("Home", "H"))
                                .commit();
                    }
                });
                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
                dialog.setCancelable(false);
            }
        }
    }
}
