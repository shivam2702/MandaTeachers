package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.AlertDialog;
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
import android.widget.Spinner;
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
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterMidterm;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.midtermRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;


public class MidTerm extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String Selection;
    public static String LectureName;
    public static String Branch;
    public static String Subjectnumber;
    public static ArrayList<String> STUDENTNAME = new ArrayList<String>();
    public static ArrayList<String> STUDENTROLLNO = new ArrayList<String>();
    public static ArrayList<String> BRANCHNAME = new ArrayList<String>();
    public static ArrayList<String> LECTNAME = new ArrayList<String>();
    public static ArrayList<String> SUBJECTNO = new ArrayList<String>();
    public static ArrayList<String> MIDTERM = new ArrayList<String>();
    public static ProgressDialog progress;
    public static ProgressDialog progress1;
    public static ProgressDialog progress2;
    public static Button update;
    RecyclerView recyclerView;
    RecyclerAdapterMidterm adapter;
    View rootView;
    private String mParam1;
    private String mParam2;

    public MidTerm() {
        // Required empty public constructor
    }

    public static MidTerm newInstance(String param1, String param2) {
        MidTerm fragment = new MidTerm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static List<midtermRecycler> getData() {
        List<midtermRecycler> data = new ArrayList<>();
        for (int i = 0; i < STUDENTROLLNO.size(); i++) {
            midtermRecycler current = new midtermRecycler();
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
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_mid_term, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclemidterm);
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            progress = new ProgressDialog(getActivity());
            task.execute(links.subjectlistlink);
        } else {
            Message.NotConnected(getActivity());
        }

        update = (Button) rootView.findViewById(R.id.submitbtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    MyAsyncTaskMidTermUpdate task = new MyAsyncTaskMidTermUpdate();
                    progress2 = new ProgressDialog(getActivity());
                    task.execute(links.midtermlinksubmit);
                } else {
                    Message.NotConnected(getActivity());
                }

            }
        });

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
            Spinner spinner = (Spinner) rootView.findViewById(R.id.subjectlist);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LECTNAME);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    LectureName = LECTNAME.get(pos);
                    Subjectnumber = SUBJECTNO.get(pos);
                    Branch = BRANCHNAME.get(pos);
                    Log.e("Branch:",Branch);
                    Spinner spinner1 = (Spinner) rootView.findViewById(R.id.midtermno);
                    ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.midterm));
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
                            progress1 = new ProgressDialog(getActivity());
                            if (pos != 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                update.setVisibility(View.VISIBLE);
                                if (AppStatus.getInstance(getActivity()).isOnline()) {
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

        public void onPostExecute(String s) {
            try {
                STUDENTNAME.clear();
                STUDENTROLLNO.clear();
                MIDTERM.clear();
                String sub = "subject" + Subjectnumber + "mt" + Selection + "";
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    STUDENTNAME.add(i, json_data.getString("sname"));
                    STUDENTROLLNO.add(i, json_data.getString("rollno"));
                    MIDTERM.add(i, json_data.getString(sub));
                }
                for (int i = 0; i < STUDENTNAME.size(); i++) {
                    if (MIDTERM.get(i).equals("0") || MIDTERM.get(i).equals("1") || MIDTERM.get(i).equals("2") || MIDTERM.get(i).equals("3") || MIDTERM.get(i).equals("4") || MIDTERM.get(i).equals("5") || MIDTERM.get(i).equals("6") || MIDTERM.get(i).equals("7") || MIDTERM.get(i).equals("8") || MIDTERM.get(i).equals("9") || MIDTERM.get(i).equals("10")) {
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
                                    finalvalue = finalvalue + STUDENTNAME.get(i) + "\nMarks=" + MIDTERM.get(i) + "\n\n";
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
            adapter = new RecyclerAdapterMidterm(getActivity(), getData());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }


    public class MyAsyncTaskMidTermUpdate extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress2.setTitle("Please Wait");
            progress2.setMessage("We are updating your MidTerm records");
            progress2.show();
            progress2.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            for (int i = 0; i < STUDENTNAME.size(); i++) {
                try {
                    String mid = "";
                    if (MIDTERM.get(i).equals("") || MIDTERM.get(i).equals(null) || MIDTERM.get(i).equals("-"))
                        mid = "AB";
                    else
                        mid = MIDTERM.get(i);

                    String updatetext =
                            "param1=" + URLEncoder.encode(Branch, "UTF-8") +
                                    "&param2=" + URLEncoder.encode(STUDENTROLLNO.get(i), "UTF-8") +
                                    "&param3=" + URLEncoder.encode(mid, "UTF-8") +
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
            Toast.makeText(getActivity(), "Marks Inserted Successfully", Toast.LENGTH_SHORT).show();
            String finalvalue = "";
            for (int i = 0; i < STUDENTROLLNO.size(); i++) {
                if (MIDTERM.get(i).equals("") || MIDTERM.get(i).equals(null) || MIDTERM.get(i).equals("-"))
                    MIDTERM.set(i, "AB");

                finalvalue = finalvalue + STUDENTNAME.get(i) + "\nMarks=" + MIDTERM.get(i) + "\n\n";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Updated Result");
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
