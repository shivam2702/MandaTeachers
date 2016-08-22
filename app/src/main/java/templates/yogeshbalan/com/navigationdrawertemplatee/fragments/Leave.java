package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterAssignment;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterLeave;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.assignmentRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.leaveRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Home;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

public class Leave extends Fragment {

    public Leave() {
        // Required empty public constructor
    }

    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static Leave newInstance(String param1, String param2) {
        Leave fragment = new Leave();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View rootView;
    RecyclerView recyclerView;
    ProgressDialog progress;
    public static ArrayList<String> Student_rollno = new ArrayList<String>();
    public static ArrayList<String> Reason = new ArrayList<String>();
    public static ArrayList<String> Date = new ArrayList<String>();
    public static ArrayList<String> NoOfDays = new ArrayList<String>();
    public static ArrayList<String> StudentName = new ArrayList<String>();
    public static ArrayList<String> LeaveID = new ArrayList<String>();
    public static ArrayList<String> Status = new ArrayList<String>();
    RecyclerAdapterLeave adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_leave, container, false);
        MainActivity.abcd = 0;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle);
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(getActivity());
            MyAsyncTaskLeaveGet task = new MyAsyncTaskLeaveGet();
            task.execute(links.leaveapp);
        } else {
            Message.NotConnected(getActivity());
        }

        Button btn = (Button) rootView.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    progress = new ProgressDialog(getActivity());
                    MyAsyncTaskLeaveSend task = new MyAsyncTaskLeaveSend();
                    task.execute(links.leaveappsend);
                } else {
                    Message.NotConnected(getActivity());
                }
            }
        });
        return rootView;
    }

    public class MyAsyncTaskLeaveGet extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("Getting Your Leaves ...");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext = "param1=" + LoginPage.department; // + URLEncoder.encode(""+LoginPage.department, "UTF-8");
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
            try {
                LeaveID.clear();
                Student_rollno.clear();
                StudentName.clear();
                Reason.clear();
                Date.clear();
                NoOfDays.clear();
                Status.clear();
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    if (json_data.getString("LeaveID").equals(null) || json_data.getString("LeaveID").equals("") || json_data.getString("LeaveID").equals("null")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Leave Status");
                        builder.setMessage("Wu hu yuppy...\nNo Pending Leaves");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, Home.newInstance("", ""))
                                        .commit();

                            }
                        });
                        AlertDialog dialog = builder.show();
                        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                        dialog.show();
                    } else {
                        LeaveID.add(i, json_data.getString("LeaveID"));
                        Student_rollno.add(i, json_data.getString("Student_rollno"));
                        StudentName.add(i, json_data.getString("StudentName"));
                        Reason.add(i, json_data.getString("Reason"));
                        Date.add(i, json_data.getString("Date"));
                        NoOfDays.add(i, json_data.getString("NoOfDays"));
                        Status.add(i, "Pending");
                    }

                }

            } catch (Exception e) {
                Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
            super.onPostExecute(s);
            adapter = new RecyclerAdapterLeave(getActivity(), getData());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    public static List<leaveRecycler> getData() {
        List<leaveRecycler> data = new ArrayList<>();
        for (int i = 0; i < Student_rollno.size(); i++) {
            leaveRecycler current = new leaveRecycler();
            current.studentname = StudentName.get(i);
            current.Studentroll = Student_rollno.get(i);
            current.LeaveReason = Reason.get(i);
            current.LeaveDates = Date.get(i);
            current.LeaveNoOfDays = NoOfDays.get(i);
            data.add(current);
        }
        return data;
    }

    public class MyAsyncTaskLeaveSend extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("Sending your Response...");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                for (int i = 0; i < Student_rollno.size(); i++) {
                    String updatetext = "param1=" + URLEncoder.encode(LeaveID.get(i), "UTF-8") + "&param2=" + URLEncoder.encode(Status.get(i), "UTF-8");
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
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return response;
        }

        public void onPostExecute(String s) {
            progress.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Done");
            builder.setMessage("You have checked all the Leaves..");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, Home.newInstance("", ""))
                            .commit();

                }
            });
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            dialog.show();
            super.onPostExecute(s);
        }
    }

}



