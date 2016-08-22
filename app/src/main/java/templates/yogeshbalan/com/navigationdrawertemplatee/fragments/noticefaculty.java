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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;
import templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter.NoticeLayoutClassAdapter;
import templates.yogeshbalan.com.navigationdrawertemplatee.links;


public class noticefaculty extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public static BufferedReader reader = null;
    public static StringBuilder sb = new StringBuilder();
    public static String line = null;
    public static String result = "";
    public static TextView tv;
    public static ProgressDialog progress;

    public static noticefaculty newInstance(String param1, String param2) {
        noticefaculty fragment = new noticefaculty();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public noticefaculty() {
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

    public static View rootView;
    public static ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_noticefaculty, container, false);
        list = (ListView) rootView.findViewById(R.id.listnoticeBoardTeacher);

        if (AppStatus.getInstance(getActivity()).isOnline()) {
            progress = new ProgressDialog(rootView.getContext());
            facultynoticeboard task = new facultynoticeboard();
            task.execute(links.facultynoticeboard);
        } else {
            Message.NotConnected(getActivity());
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SnackbarManager.show(
                        Snackbar.with(rootView.getContext()) // context
                                .text("By: " + MainActivity.tname.get(position)) // text to display
                                .duration(500)
                        , getActivity()); // activity where it is displayed
            }
        });
        return rootView;

    }

    public static class facultynoticeboard extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progress.setTitle("Please Wait");
            progress.setMessage("We are getting Faculty Notices");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
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

        @Override
        protected void onPostExecute(String s) {
            MainActivity.notice.clear();
            MainActivity.tname.clear();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    MainActivity.notice.add(json_data.getString("NewNotice"));
                    MainActivity.tname.add(json_data.getString("teachername"));
                }
            } catch (Exception e) {
                Toast.makeText(rootView.getContext(),"No new notices for faculty yet.\nKindly stay tuned....",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            super.onPostExecute(s);
            list.setAdapter(new NoticeLayoutClassAdapter(rootView.getContext(), MainActivity.notice));
            progress.dismiss();
        }
    }

}
