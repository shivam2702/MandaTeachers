package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterLab;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.labRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;


public class lab extends Fragment {
    public static RecyclerView recyclerView;
    public static RecyclerAdapterLab adapter1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
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
    public static Button submit;
    boolean update;
    boolean chk;


    public static lab newInstance(String param1, String param2) {
        lab fragment = new lab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public lab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_lab, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclelab);
        //FACULTY SUBJECT LIST
        //isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            task.execute(links.labsubjectlistlink);
        } else {
            Message.NotConnected(getActivity());
        }

        submit = (Button) rootView.findViewById(R.id.submitlab);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress2 = new ProgressDialog(getActivity());
                    progress3 = new ProgressDialog(getActivity());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Attendance Submit");
                    final Button submit = (Button) rootView.findViewById(R.id.submitlab);
                    final String btn = submit.getText().toString();
                    if (update == true) {
                        builder.setMessage("Do you want to Update?");
                    } else {
                        builder.setMessage("Do you want to Submit?");
                    }
                    builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (chk == true && update == true) {
                                MyAsyncTaskAttendanceUpdateTwo task = new MyAsyncTaskAttendanceUpdateTwo();
                                task.execute(links.labsubmitlinkTWO);
                            } else if (chk == false && update == false) {
                                MyAsyncTaskAttendanceUpdateOne task = new MyAsyncTaskAttendanceUpdateOne();
                                task.execute(links.labsubmitlinkONE);
                            } else if (chk == true && update == false) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setTitle("Sorry");
                                builder1.setMessage("You can't update\n Please select update mode first");
                                builder1.setPositiveButton("Switch to Update Mode", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        update = true;
                                        submit.setText("Update Record");
                                    }
                                });
                                AlertDialog dialog1 = builder1.show();
                                TextView messageText = (TextView) dialog1.findViewById(android.R.id.message);
                                messageText.setGravity(Gravity.CENTER);
                                dialog1.show();
                            }
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
            Spinner spinner = (Spinner) rootView.findViewById(R.id.labsubject);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LABNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if(pos==0)
                    {
                        recyclerView.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.INVISIBLE);
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Lab") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    }
                    else {
                        recyclerView.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        chk = false;
                        update = false;
                        Button submit = (Button) rootView.findViewById(R.id.submitlab);
                        submit.setText("Submit Record");
                        LabName = LABNAME.get(pos);
                        LectureID = LABID.get(pos);
                        //isInternetPresent = cd.isConnectingToInternet();
                        // check for Internet status
                        if (AppStatus.getInstance(getActivity()).isOnline()) {
                            progress1 = new ProgressDialog(getActivity());
                            MyAsyncTaskstudentlist task1 = new MyAsyncTaskstudentlist();
                            //STUDENT LIST
                            task1.execute(links.labcheck);
                        } else {
                            Message.NotConnected(getActivity());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    public class MyAsyncTaskstudentlist extends AsyncTask<String, Void, String> {
        String response = "";
        public int count = 0;

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "select1=" + URLEncoder.encode(LectureID, "UTF-8");
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
                    } catch (JSONException e) {
                        STUDENTNAME.add(i, json_data4.getString("student_name"));
                        STUDENTROLLNO.add(i, json_data4.getString("student_rollno"));
                        att.add(i, json_data4.getString("attendance"));
                        labb.add(i, json_data4.getString("performance"));
                        file.add(i, json_data4.getString("file"));
                        uni.add(i, json_data4.getString("uniform"));
                        viva.add(i, json_data4.getString("viva"));
                        lectno = json_data4.getString("labno");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress1.dismiss();
            if (lectno != "999") {
                chk = true;
            }

            if (chk == true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Attendance already found");
                builder.setMessage("Do you want to update?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update = true;
                        Button submit = (Button) rootView.findViewById(R.id.submitlab);
                        submit.setText("Update Record");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update = false;
                    }
                });
                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
                dialog.setCancelable(false);
            }
            if (chk == false) {
                update = false;
            }

            adapter1 = new RecyclerAdapterLab(getActivity(), getData());
            recyclerView.setAdapter(adapter1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    // ATTENDACNE UPDATE

    public class MyAsyncTaskAttendanceUpdateOne extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress2.setTitle("Please Wait");
            progress2.setMessage("We are updating your Lab records");
            progress2.show();
            progress2.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext =
                        "select2=" + URLEncoder.encode(LoginPage.ID, "UTF-8") +
                                "&select1=" + URLEncoder.encode(LectureID, "UTF-8");
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
            super.onPostExecute(s);
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
            //isInternetPresent = cd.isConnectingToInternet();
            // check for Internet status
            if (status.equals("pass")) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress3 = new ProgressDialog(getActivity());
                    MyAsyncTaskAttendanceUpdateTwo task = new MyAsyncTaskAttendanceUpdateTwo();
                    task.execute(links.labsubmitlinkTWO);
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
                                    "&uni=" + URLEncoder.encode(uni.get(i), "UTF-8");
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