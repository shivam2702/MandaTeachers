package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.Scanner;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.LoginPage;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leaveView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leaveView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public leaveView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leaveView.
     */
    // TODO: Rename and change types and number of parameters
    public static leaveView newInstance(String param1, String param2) {
        leaveView fragment = new leaveView();
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
    public static View rootView;
    public static TextView textView;
    public static ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentMainActivity.abcd=0;
        rootView = inflater.inflate(R.layout.fragment_leave_view, container, false);
        textView=(TextView)rootView.findViewById(R.id.leavechk);
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline())
        {
            progress = new ProgressDialog(getActivity());
            MyAsyncTask task = new MyAsyncTask();
            task.execute(links.leaveall);
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
    public static class MyAsyncTask extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection conn;
            try {
                url = new URL(params[0]);
                String text = "param1=" + URLEncoder.encode(LoginPage.department, "UTF-8");
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

            progress.setTitle("Please Wait");
            progress.setMessage("Loading up....");
            progress.show();
            progress.setCancelable(false);
        }

        protected void onPostExecute(String s) {
            textView.setText("");
            try {
                JSONArray jArray = new JSONArray(response);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    String StudentName = json_data.getString("StudentName");
                    String Reason = json_data.getString("Reason");
                    String Date = json_data.getString("Date");
                    String NoOfDays = json_data.getString("NoOfDays");
                    String Status = json_data.getString("Status");
                    textView.append(StudentName+"\nDemands leave for: "+Reason+"\nFrom: "+Date+"\nFor "+NoOfDays+" Days"+"\nAnd the Status is: "+Status+"\n\n");
                }
            } catch (Exception e) {
                textView.append("No leaves Status Found......");
                e.printStackTrace();
            }
            super.onPostExecute(s);
            progress.dismiss();
        }
    }

}
