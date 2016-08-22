package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterCheckAttendance;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.attendanceRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceView extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView recyclerView;
    public static String LectureName;
    public static String LectureID;
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static ProgressDialog progress3;
    public static ArrayList<String> date = new ArrayList<String>();
    public static ArrayList<String> LECTNAME = new ArrayList<String>();
    public static ArrayList<String> ATTENDANCE = new ArrayList<String>();
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static ArrayList<String> lectno = new ArrayList<String>();
    public static String selectedLecture = "";
    public static Button submit;
    boolean update;
    boolean chk;
    public static Spinner spinnerattendanceDate;
    View rootView;
    private String mParam1;
    private String mParam2;

    public AttendanceView() {
        // Required empty public constructor
    }

    public static AttendanceView newInstance(String param1, String param2) {
        AttendanceView fragment = new AttendanceView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_attendance_view, container, false);
        progress = new ProgressDialog(getContext());
        progress1 = new ProgressDialog(getContext());
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycleattendaceCheck);
        spinnerattendanceDate = (Spinner) rootView.findViewById(R.id.attendanceDate);
        MyAsyncTaskLecture lecture = new MyAsyncTaskLecture();
        lecture.execute(links.subjectlistlink);
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
                LECTNAME.clear();
                JSONArray jArray = new JSONArray(response);
                LECTNAME.add("Please Select Subject");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LECTNAME.add(json_data.getString("Lectname"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
            try {
                Spinner spinner = (Spinner) rootView.findViewById(R.id.attendanceViewSubject);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LECTNAME);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        LectureName = LECTNAME.get(pos);
                        progress1 = new ProgressDialog(getActivity());
                        if (pos == 0) {
                            date.clear();
                            spinnerattendanceDate.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            SnackbarManager.show(
                                    Snackbar.with(rootView.getContext()) // context
                                            .text("Please Select Subject") // text to display
                                            .duration(500)
                                    , getActivity()); // activity where it is displayed
                        } else {
                            spinnerattendanceDate.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            MyAsyncTaskDates dates = new MyAsyncTaskDates();
                            dates.execute(links.specificDate);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            catch (Exception e)
            {

            }

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
                String text = "param1=" + URLEncoder.encode(LectureName, "UTF-8");
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
                date.clear();
                lectno.clear();
                JSONArray jArray = new JSONArray(response);
                date.add("Please Select Date");
                lectno.add("");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String datee = json_data.getString("date");
                    lectno.add(json_data.getString("lectno"));
                    date.add("Lecture No: "+json_data.getString("lectno")+" ("+datee.replaceAll("\\/","/")+")");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, date);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerattendanceDate.setAdapter(spinnerArrayAdapter);
            spinnerattendanceDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    selectedLecture = lectno.get(pos);
                    progress1 = new ProgressDialog(getActivity());
                    if (pos == 0) {
                        SnackbarManager.show(
                                Snackbar.with(rootView.getContext()) // context
                                        .text("Please Select Date") // text to display
                                        .duration(500)
                                , getActivity()); // activity where it is displayed
                    } else {
                        MyAsyncTaskAttendance attendance = new MyAsyncTaskAttendance();
                        attendance.execute(links.attendanceLoad);
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
                String text = "param1=" + URLEncoder.encode(selectedLecture, "UTF-8") + "&param2=" + URLEncoder.encode(LectureName, "UTF-8") ;
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
                ATTENDANCE.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    STUDENTNAME.add(i, json_data.getString("student_name"));
                    STUDENTROLLNO.add(i, json_data.getString("student_rollno"));
                    ATTENDANCE.add(i, json_data.getString("attendance"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();

            RecyclerAdapterCheckAttendance adapter1 = new RecyclerAdapterCheckAttendance(getActivity(), getData());
            recyclerView.setAdapter(adapter1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
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

}
