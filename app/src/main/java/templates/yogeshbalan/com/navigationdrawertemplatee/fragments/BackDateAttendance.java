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
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterOldAttendance;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.attendanceRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

public class BackDateAttendance extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView recyclerView;
    public static RecyclerAdapterOldAttendance adapter1;
    public static String LectureName;
    public static String LectureID;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static ProgressDialog progress3;
    public static ArrayList<String> LABID = new ArrayList<String>();
    public static ArrayList<String> LECTNAME = new ArrayList<String>();
    public static ArrayList<String> ATTENDANCE = new ArrayList<String>();
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static String lectno = "999";
    public static String date = "";
    public static String attendanceShow = "";
    public static Button submit;
    public static Button dateselect;
    public static String reason="";
    View rootView;
    private String mParam1;
    private String mParam2;

    public BackDateAttendance() {
        // Required empty public constructor
    }

    public static BackDateAttendance newInstance(String param1, String param2) {
        BackDateAttendance fragment = new BackDateAttendance();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static List<attendanceRecycler> getData() {
        List<attendanceRecycler> data = new ArrayList<>();
        for (int i = 0; i < STUDENTNAME.size(); i++) {
            attendanceRecycler current = new attendanceRecycler();
            current.studentname = STUDENTNAME.get(i);
            current.Studentroll = STUDENTROLLNO.get(i);
            data.add(current);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_back_date_attendance, container, false);
        MainActivity.abcd = 0;
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleattendaceBack);
        submit = (Button) rootView.findViewById(R.id.submitattendanceBack);
        dateselect = (Button) rootView.findViewById(R.id.datebtn);
        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar();
            }
        });
        //FACULTY SUBJECT LIST
        // check for Internet status

        progress3 = new ProgressDialog(getActivity());

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            task.execute(links.subjectlistlink);
        } else {
            Message.NotConnected(getActivity());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for Internet status
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress2 = new ProgressDialog(getActivity());
                    progress3 = new ProgressDialog(getActivity());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Submitting Attendance");
                    Button submit = (Button) rootView.findViewById(R.id.submitattendanceBack);
                    final String btn = submit.getText().toString();
                    builder.setMessage("Do you want to Submit?");
                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyAsyncTaskAttendanceUpdateOne task = new MyAsyncTaskAttendanceUpdateOne();
                            task.execute(links.attendancesubmitlinkONEBack);
                        }
                    });
                    builder.setNegativeButton("Wait Let me Check..", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.show();
                    TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                    dialog.show();
                    dialog.setCancelable(false);

                } else {
                    Message.NotConnected(getActivity());
                }

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
            dateselect.setText(date+"");
            recyclerView.setVisibility(View.VISIBLE);
            if (AppStatus.getInstance(getActivity()).isOnline()) {
                MyAsyncTaskstudentlist task1 = new MyAsyncTaskstudentlist();
                task1.execute(links.attendancecheckback);
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
                LECTNAME.clear();
                JSONArray jArray = new JSONArray(response);
                LABID.add("EMPTY");
                LECTNAME.add("Please Select Subject");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LABID.add(json_data.getString("LabID"));
                    LECTNAME.add(json_data.getString("Lectname"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();

            Spinner spinner = (Spinner) rootView.findViewById(R.id.attendancesubjectBack);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LECTNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    recyclerView.setVisibility(View.GONE);
                    submit = (Button) rootView.findViewById(R.id.submitattendanceBack);
                    dateselect = (Button) rootView.findViewById(R.id.datebtn);
                    dateselect.setText("Select Date");
                    submit.setText("Submit Record");
                    LectureName = LECTNAME.get(pos);
                    LectureID = LABID.get(pos);
                    progress1 = new ProgressDialog(getActivity());
                    // check for Internet status
                    if (pos == 0) {
                        recyclerView.setVisibility(View.GONE);
                        submit.setVisibility(View.INVISIBLE);
                        dateselect.setVisibility(View.GONE);
                        dateselect.setText("Select Date");
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Subject") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    } else {
                        submit.setVisibility(View.INVISIBLE);
                        dateselect.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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
                String text = "select1=" + URLEncoder.encode(LectureID, "UTF-8") + "&dateBack=" + date;
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
            Message.Progress_student(progress1);
        }

        public void onPostExecute(String s) {
            STUDENTNAME.clear();
            STUDENTROLLNO.clear();
            ATTENDANCE.clear();
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data4 = jArray.getJSONObject(i);
                    try {
                        attendanceShow = "NOTFOUND";
                        STUDENTNAME.add(i, json_data4.getString("sname"));
                        STUDENTROLLNO.add(i, json_data4.getString("srollno"));
                        ATTENDANCE.add(i, "0");
                        lectno = "999";
                    } catch (JSONException e) {
                        attendanceShow = "FOUND";
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress1.dismiss();
            if (attendanceShow.equals("FOUND")) {
                submit.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Attendance Already Found",Toast.LENGTH_SHORT).show();
            } else {

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
                            reason = input.getText().toString().trim();
                            submit.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter1 = new RecyclerAdapterOldAttendance(getActivity(), getData());
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
                String updatetext =  "select1=" + URLEncoder.encode(LectureID, "UTF-8") +
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
                    task.execute(links.attendancesubmitlinkTWOBack);
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
            progress3.setTitle("Updating");
            progress3.setMessage("Updating Attendance Please wait....");
            progress3.show();
            progress3.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            Log.e("Point:", "1");
            for (int i = 0; i < STUDENTNAME.size(); i++) {
                try {
                    String updatetext =
                            "memberid=" + URLEncoder.encode(LoginPage.ID, "UTF-8") +
                                    "&LabID=" + URLEncoder.encode(LectureID, "UTF-8") +
                                    "&Lectno=" + URLEncoder.encode(lectno, "UTF-8") +
                                    "&sname=" + URLEncoder.encode(STUDENTNAME.get(i), "UTF-8") +
                                    "&srollno=" + URLEncoder.encode(STUDENTROLLNO.get(i), "UTF-8") +
                                    "&atten=" + URLEncoder.encode(ATTENDANCE.get(i), "UTF-8") +
                                    "&latedate=" + URLEncoder.encode(date, "UTF-8");
                    Log.e("Point:", "2");
                    url = new URL(params[0]);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setFixedLengthStreamingMode(updatetext.getBytes().length);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(updatetext);
                    out.close();
                    Log.e("Point:", "3");
                    Scanner inStream = new Scanner(conn.getInputStream());
                    while (inStream.hasNextLine())
                        response += (inStream.nextLine());
                    Log.e("Point:", "4");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return response;
        }

        public void onPostExecute(String s) {
            JSONArray jArray = null;
            String status = "";
            Log.e("Point:", "5");
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
                    String total = "";
                    if (ATTENDANCE.get(i).equals("1")) {
                        total = "Present";
                    } else {
                        total = "Absent";
                    }
                    finalvalue = finalvalue + STUDENTNAME.get(i) + "\nAttendance=" + total + "\n\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Updated Attendance");
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
            super.onPostExecute(s);
        }

    }
}
