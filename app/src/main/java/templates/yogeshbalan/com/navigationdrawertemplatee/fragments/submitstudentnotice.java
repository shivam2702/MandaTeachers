package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;


public class submitstudentnotice extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static submitstudentnotice newInstance(String param1, String param2) {
        submitstudentnotice fragment = new submitstudentnotice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private String mParam1;
    private String mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public submitstudentnotice() {
        // Required empty public constructor
    }

    View rootView;
    EditText text;
    String message;
    ProgressDialog progress;
    int selection = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submitstudentnotice, container, false);
        text = (EditText) rootView.findViewById(R.id.stuno);
        final Button submit = (Button) rootView.findViewById(R.id.submitno);
        RadioGroup group;
        RadioButton student;
        RadioButton teacher;
        student = (RadioButton) rootView.findViewById(R.id.student);
        teacher = (RadioButton) rootView.findViewById(R.id.teacher);
        group = (RadioGroup) rootView.findViewById(R.id.groupnotice);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = 1;
                submit.setText("Submit Student's Notice");
                text.setHint("Enter Student's Notice");
                Toast.makeText(rootView.getContext(), "Student Selected", Toast.LENGTH_SHORT).show();
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = 2;
                submit.setText("Submit Faculty's Notice");
                text.setHint("Enter Faculty's Notice");
                Toast.makeText(rootView.getContext(), "Teacher Selected", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = text.getText().toString();
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    if (message.isEmpty()) {
                        Toast.makeText(rootView.getContext(), "Why you are trying to post empty notice?", Toast.LENGTH_SHORT).show();
                    } else {
                        if (message.contains("'")) {
                            Toast.makeText(rootView.getContext(), "Apostrophe ( ' ) sign is not allowed", Toast.LENGTH_SHORT).show();
                        } else {
                            if (selection == 1) {
                                MyAsyncTaskStudentNotice task = new MyAsyncTaskStudentNotice();
                                progress = new ProgressDialog(getActivity());
                                task.execute(links.noticestudentsubmit);
                            } else {
                                MyAsyncTaskFacultyNotice task = new MyAsyncTaskFacultyNotice();
                                progress = new ProgressDialog(getActivity());
                                task.execute(links.noticefacultysubmit);
                            }
                        }

                    }

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
            }
        });
        return rootView;
    }

    public class MyAsyncTaskStudentNotice extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("We are submitting your notice...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext =
                        "param1=" + URLEncoder.encode(message, "UTF-8") +
                                "&param2=" + URLEncoder.encode(LoginPage.name, "UTF-8");
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
            progress.dismiss();
            String status = "";
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    status = jsonObject.getString("status");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("pass")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notice");
                builder.setMessage("Notice Submitted Successfully");
                builder.setPositiveButton("Let's go back to Home", new DialogInterface.OnClickListener() {
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
                builder.setTitle("Notice");
                builder.setMessage("Notice Submition Failed. Please Retry");
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

    }
    public class MyAsyncTaskFacultyNotice extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("Getting Your Data Please Be Patient...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext =
                        "param1=" + URLEncoder.encode(message, "UTF-8") +
                                "&param2=" + URLEncoder.encode(LoginPage.name, "UTF-8");
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
            progress.dismiss();
            String status = "";
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    status = jsonObject.getString("status");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("pass")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notice");
                builder.setMessage("Notice Submitted Successfully");
                builder.setPositiveButton("Let's go back to Home", new DialogInterface.OnClickListener() {
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
                builder.setTitle("Notice");
                builder.setMessage("Notice Submition Failed. Please Retry");
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

    }

}
