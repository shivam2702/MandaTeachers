package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
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
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterCheckLab;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterLab;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.labRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

/**
 * A simple {@link Fragment} subclass.
 */
public class labview extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView recyclerView;
    public static View rootView;
    public static RecyclerAdapterLab adapter1;
    public static String LabName="";
    public static String selectedLecture;
    public static Spinner datedropdown;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static ProgressDialog progress3;
    public static ArrayList<String> LABID = new ArrayList<String>();
    public static ArrayList<String> date = new ArrayList<String>();
    public static ArrayList<String> labno = new ArrayList<String>();
    public static ArrayList<String> LABNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static ArrayList<String> att = new ArrayList<String>();
    public static ArrayList<String> labb = new ArrayList<String>();
    public static ArrayList<String> viva = new ArrayList<String>();
    public static ArrayList<String> file = new ArrayList<String>();
    public static ArrayList<String> total = new ArrayList<String>();
    public static ArrayList<String> uni = new ArrayList<String>();
    private String mParam1;
    private String mParam2;


    public labview() {
        // Required empty public constructor
    }

    public static labview newInstance(String param1, String param2) {
        labview fragment = new labview();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_labview, container, false);
        datedropdown = (Spinner) rootView.findViewById(R.id.datelistlabview);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclelabview);

        if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress = new ProgressDialog(getActivity());
                    MyAsyncTaskLecture lecture = new MyAsyncTaskLecture();
                    lecture.execute(links.labsubjectlistlink);
                } else {
                    Message.NotConnected(getActivity());
                }


        return rootView;
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
            Log.i("onPostExecute: ",s);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.subjectlistlabview);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LABNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    LabName = LABNAME.get(pos);
                    if (pos == 0) {
                        date.clear();
                        recyclerView.setVisibility(View.GONE);
                        datedropdown.setVisibility(View.GONE);
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Lab") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        datedropdown.setVisibility(View.VISIBLE);
                        MyAsyncTaskDates dates = new MyAsyncTaskDates();
                        dates.execute(links.labspecificDate);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }
    public class MyAsyncTaskDates extends AsyncTask<String, Void, String> {
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
                String text = "param1=" + URLEncoder.encode(LabName, "UTF-8");
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
                JSONArray jArray = new JSONArray(response);
                date.clear();
                labno.clear();
                date.add("Please Select Date");
                labno.add("");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String datee = json_data.getString("date");
                    labno.add(json_data.getString("labno"));
                    date.add("Lab No: "+json_data.getString("labno")+" ("+datee.replaceAll("\\/","/")+")");
                    Log.e("Date","Lab No: "+json_data.getString("labno")+" ("+datee.replaceAll("\\/","/")+")");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
            Spinner spinner = (Spinner) rootView.findViewById(R.id.datelistlabview);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, date);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    selectedLecture = labno.get(pos);
                    progress1 = new ProgressDialog(getActivity());
                    if (pos == 0) {
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Date") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    } else {
                        MyAsyncTaskAttendance attendance = new MyAsyncTaskAttendance();
                        attendance.execute(links.labattendanceLoad);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }
    }

public class MyAsyncTaskAttendance extends AsyncTask<String, Void, String> {
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
                String text = "param1=" + URLEncoder.encode(selectedLecture, "UTF-8") + "&param2=" + URLEncoder.encode(LabName, "UTF-8") ;
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
                STUDENTNAME.clear();
                STUDENTROLLNO.clear();
                att.clear();
                labb.clear();
                file.clear();
                uni.clear();
                viva.clear();
                total.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    STUDENTNAME.add(i, json_data.getString("student_name"));
                    STUDENTROLLNO.add(i, json_data.getString("student_rollno"));
                    att.add(i, json_data.getString("attendance"));
                    labb.add(i, json_data.getString("performance"));
                    file.add(i, json_data.getString("file"));
                    uni.add(i, json_data.getString("uniform"));
                    viva.add(i, json_data.getString("viva"));
                    total.add(i, json_data.getString("total"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();

            RecyclerAdapterCheckLab adapter1 = new RecyclerAdapterCheckLab(getActivity(), getData());
            recyclerView.setAdapter(adapter1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
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
}
