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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.*;
import templates.yogeshbalan.com.navigationdrawertemplatee.*;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.assignmentRecycler;


public class Assignment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String LectureID;
    public static String Selection;
    public static String LectureName;
    public static String Branch;
    public static String Subjectnumber;
    public static ArrayList<String> LABID = new ArrayList<String>();
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static ArrayList<String> BRANCHNAME = new ArrayList<String>();
    public static ArrayList<String> LECTNAME = new ArrayList<String>();
    public static ArrayList<String> SUBJECTNO = new ArrayList<String>();
    public static String updatetext;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static Button update;
    public static ArrayList<String> ASSIGNMENT = new ArrayList<String>();
    RecyclerView recyclerView;
    RecyclerAdapterAssignment adapter;
    View rootView;
    private String mParam1;
    private String mParam2;

    public Assignment() {
        // Required empty public constructor
    }

    public static List<assignmentRecycler> getData() {
        List<assignmentRecycler> data = new ArrayList<>();
        for (int i = 0; i < STUDENTROLLNO.size(); i++) {
            assignmentRecycler current = new assignmentRecycler();
            current.studentname = STUDENTNAME.get(i);
            current.Studentroll = STUDENTROLLNO.get(i);
            data.add(current);
        }
        return data;
    }

    public static Assignment newInstance(String param1, String param2) {
        Assignment fragment = new Assignment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_assignment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleassignment);
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            task.execute(links.subjectlistlink);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        update = (Button) rootView.findViewById(R.id.submitassignment);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for Internet status
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress2 = new ProgressDialog(getActivity());
                    MyAsyncTaskAssignmentUpdate task = new MyAsyncTaskAssignmentUpdate();
                    task.execute(links.assignmentsubmitlink);
                } else {
                    Message.NotConnected(getActivity());
                }
            }
        });
        return rootView;
    }

    //LECTURE NAMES
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
                BRANCHNAME.clear();
                LECTNAME.clear();
                SUBJECTNO.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LECTNAME.add(json_data.getString("Lectname"));
                    BRANCHNAME.add(json_data.getString("branch"));
                    SUBJECTNO.add(json_data.getString("subjectno"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
            Spinner spinner = (Spinner) rootView.findViewById(R.id.subjectlistassign);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LECTNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    LectureName = LECTNAME.get(pos);
                    Subjectnumber = SUBJECTNO.get(pos);
                    Branch = BRANCHNAME.get(pos);
                    Spinner spinner1 = (Spinner) rootView.findViewById(R.id.assignmentno);
                    ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.assignment));
                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(spinnerArrayAdapter1);
                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            switch (pos) {
                                case 1:
                                    Selection = "1";
                                    break;
                                case 2:
                                    Selection = "2";
                                    break;
                                case 3:
                                    Selection = "3";
                                    break;
                                case 4:
                                    Selection = "4";
                                    break;
                                case 5:
                                    Selection = "5";
                                    break;
                            }
                            if (pos != 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                update.setVisibility(View.VISIBLE);
                                // check for Internet status
                                if (AppStatus.getInstance(getActivity()).isOnline()) {
                                    progress1 = new ProgressDialog(getActivity());
                                    MyAsyncTaskstudentlist task1 = new MyAsyncTaskstudentlist();
                                    task1.execute(links.studentlistacademic);
                                } else {
                                    Message.NotConnected(getActivity());
                                }

                            }
                            if(pos==0)
                            {
                                recyclerView.setVisibility(View.INVISIBLE);
                                update.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    //STUDENT LIST
    public class MyAsyncTaskstudentlist extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            Message.Progress_student(progress1);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(Branch, "UTF-8");
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

        String sub = "subject" + Subjectnumber + "a" + Selection;

        public void onPostExecute(String s) {
            try {
                STUDENTNAME.clear();
                STUDENTROLLNO.clear();
                ASSIGNMENT.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    STUDENTNAME.add(i, json_data.getString("sname"));
                    STUDENTROLLNO.add(i, json_data.getString("rollno"));
                    ASSIGNMENT.add(i, json_data.getString(sub));
                }

                for (int i = 0; i < STUDENTNAME.size(); i++) {
                    if (ASSIGNMENT.get(i).equals("0") || ASSIGNMENT.get(i).equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Updating Result Not Allowed");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, Home.newInstance("Home", "H"))
                                        .commit();
                            }
                        });
                        builder.setNegativeButton("Show Result", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String finalvalue = "";
                                for (int i = 0; i < STUDENTROLLNO.size(); i++) {
                                    String total = "";
                                    if (ASSIGNMENT.get(i).equals("1")) {
                                        total = "Submitted";
                                    } else {
                                        total = "Not Submitted";
                                    }
                                    finalvalue = finalvalue + STUDENTNAME.get(i) + "\nStatus=" + total + "\n\n";
                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setTitle("Show Result");
                                builder1.setMessage(finalvalue);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.container, Home.newInstance("Home", "H"))
                                                .commit();
                                    }
                                });
                                AlertDialog dialog1 = builder1.show();
                                TextView messageText = (TextView) dialog1.findViewById(android.R.id.message);
                                messageText.setGravity(Gravity.CENTER);
                                dialog1.show();
                            }
                        });
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            progress1.dismiss();
            super.onPostExecute(s);
            adapter = new RecyclerAdapterAssignment(getActivity(), getData());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    // ASSIGNMENT UPDATE
    public class MyAsyncTaskAssignmentUpdate extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress2.setTitle("Please Wait");
            progress2.setMessage("We are updating your assignment records");
            progress2.show();
            progress2.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            for (int i = 0; i < STUDENTNAME.size(); i++) {
                try {
                    if (ASSIGNMENT.get(i) != ("1")) {
                        ASSIGNMENT.set(i, "0");
                    }
                    String updatetext =
                            "param1=" + URLEncoder.encode(Branch, "UTF-8") +
                                    "&param2=" + URLEncoder.encode(STUDENTROLLNO.get(i), "UTF-8") +
                                    "&param3=" + URLEncoder.encode(ASSIGNMENT.get(i), "UTF-8") +
                                    "&param4=" + URLEncoder.encode(Selection, "UTF-8") +
                                    "&param5=" + URLEncoder.encode(Subjectnumber, "UTF-8");
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
            progress2.dismiss();
            Toast.makeText(getActivity(), "Assignments Submitted", Toast.LENGTH_SHORT).show();
            String finalvalue = "";
            for (int i = 0; i < STUDENTROLLNO.size(); i++) {
                String total = "";
                if (ASSIGNMENT.get(i) == "1") {
                    total = "Submitted";
                } else {
                    total = "Not Submitted";
                }
                finalvalue = finalvalue + STUDENTNAME.get(i) + "\nStatus=" + total + "\n\n";
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
    }
}

