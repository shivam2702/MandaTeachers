package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;


/**
 * A simple {@link Fragment} subclass.
 */
public class feedback extends Fragment {

    public feedback() {
        // Required empty public constructor
    }

    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static feedback newInstance(String param1, String param2) {
        feedback fragment = new feedback();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static View rootView;
    public static String yourrate, Message;
    public static ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        final EditText feedback = (EditText) rootView.findViewById(R.id.feedback);
        Button submit = (Button) rootView.findViewById(R.id.submitfeedback);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.yourrate);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                yourrate = String.valueOf(rating);
            }
        });
        RatingBar overall = (RatingBar) rootView.findViewById(R.id.overallrate);
        overall.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        progress = new ProgressDialog(getActivity());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message = feedback.getText().toString();
                if (AppStatus.getInstance(getActivity()).isOnline()) {
                    MyAsyncTaskSendFeedback task = new MyAsyncTaskSendFeedback();
                    task.execute(links.feedbacksubmit);
                } else {
                    templates.yogeshbalan.com.Message.NotConnected(getActivity());
                }

            }
        });
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            MyAsynTaskLoadStars task = new MyAsynTaskLoadStars();
            task.execute(links.loadstar);
        } else {
            templates.yogeshbalan.com.Message.NotConnected(getActivity());
        }

        return rootView;
    }

    public static BufferedReader reader = null;
    public static StringBuilder sb = new StringBuilder();
    public static String line = null;
    public static String result = "";
    public static String totalstars;

    public static class MyAsynTaskLoadStars extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("Loading our older Reviews....");
            progress.show();
            progress.setCancelable(false);
        }

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        RatingBar overall = (RatingBar) rootView.findViewById(R.id.overallrate);

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    totalstars = json_data.getString("stars");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Float rate = Float.parseFloat(totalstars);
            overall.setRating(rate);
            progress.dismiss();
            super.onPostExecute(s);
        }
    }

    public class MyAsyncTaskSendFeedback extends AsyncTask<String, Void, String> {
        String response = "";

        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("We are sending your Valuable Feedback...");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                String updatetext =
                        "param1=" + URLEncoder.encode(LoginPage.ID, "UTF-8") +
                                "&param2=" + URLEncoder.encode(LoginPage.name, "UTF-8") +
                                "&param3=" + URLEncoder.encode(yourrate, "UTF-8") +
                                "&param4=" + URLEncoder.encode(Message, "UTF-8");
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
                builder.setTitle("Feedback");
                builder.setMessage("Thank You for you Valuable Feedback.");
                builder.setPositiveButton("Continue use app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Home.newInstance("Home", "H"))
                                .commit();
                    }
                });
                builder.setNegativeButton("Log me out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.show();
                TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                dialog.show();
            }
            if (status.equals("fail")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sorry");
                builder.setMessage("Please Retry");
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