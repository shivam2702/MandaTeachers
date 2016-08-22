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
import android.widget.TextView;


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
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.RecyclerAdapterChkSubject;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.chkAllotment;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

public class checkallotment extends Fragment {
    RecyclerView recyclerView;
    RecyclerAdapterChkSubject adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ProgressDialog progress;
    public static checkallotment newInstance(String param1, String param2) {
        checkallotment fragment = new checkallotment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public checkallotment() {
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
        MainActivity.abcd=0;
        rootView = inflater.inflate(R.layout.fragment_checkallotment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclesubjectview);
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline())
        {
            progress = new ProgressDialog(rootView.getContext());
            MyAsyncTaskLecture task = new MyAsyncTaskLecture();
            task.execute(links.subjectlistlink);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("No Connection");
            builder.setMessage("We are not Connected :( \n Please Check Your Connection and retry :) ");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.show();
            TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            dialog.show();
        }

        return rootView;
    }

    public static ArrayList<String> LECTNAME = new ArrayList<String>();

    public static List<chkAllotment> getData() {
        List<chkAllotment> data = new ArrayList<>();
        for (int i = 0; i < LECTNAME.size(); i++) {
            chkAllotment current = new chkAllotment();
            current.subjectname = LECTNAME.get(i);
            data.add(current);
        }
        return data;
    }

    public class MyAsyncTaskLecture extends AsyncTask<String, Void, String> {
        String response = "";
        @Override
        protected void onPreExecute()
        {
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
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    LECTNAME.add(json_data.getString("Lectname"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
            adapter = new RecyclerAdapterChkSubject(getActivity(), getData());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            progress.dismiss();
        }
    }
}
