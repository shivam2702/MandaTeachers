package templates.yogeshbalan.com.navigationdrawertemplatee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.NavigationDrawer.NavigationDrawerFragment;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Assignment;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Attendance;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.AttendanceView;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.BackDateAttendance;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.BackDateLab;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.CalendarAcadmic;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Home;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Leave;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.MidTerm;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.checkallotment;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.feedback;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.lab;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.labview;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.leaveView;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.noticefaculty;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.studentnotice;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.submitstudentnotice;

import static android.widget.Toast.LENGTH_SHORT;

//import templates.yogeshbalan.com.MandaTeacher.fragments.Home;
//import templates.yogeshbalan.com.navigationdrawertemplate.R;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static int abcd = 1;
    public static ArrayList<String> notice = new ArrayList<String>();
    public static ArrayList<String> tname = new ArrayList<String>();
    public static ProgressDialog progress1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    private String myTitle;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        if (LoginPage.name.equals(null) || LoginPage.name.equals("")) {
            progress1 = new ProgressDialog(MainActivity.this);
            DeRegister deRegister = new DeRegister();
            deRegister.execute(links.deregister);
        }
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                toolbar.setTitle(mTitle);
                toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            }
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                mTitle = "Attendance";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Attendance.newInstance("Attendance", "A"))
                        .commit();
                break;
            case 2:
                mTitle = "Attendance View";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AttendanceView.newInstance("Attendance", "A"))
                        .commit();
                break;
            case 3:
                mTitle = "Back Date Attendance";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BackDateAttendance.newInstance("Attendance", "A"))
                        .commit();
                break;
            case 4:
                mTitle = "Subject Allotted";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, checkallotment.newInstance("Subject Allotment", "S"))
                        .commit();
                break;
            case 5:
                mTitle = "Mid Terms";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MidTerm.newInstance("Mid-Terms", "M"))
                        .commit();
                break;
            case 6:
                mTitle = "Assignment";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Assignment.newInstance("Assignment", "As"))
                        .commit();
                break;
            case 7:
                mTitle = "Student's Notice Board";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, studentnotice.newInstance("Notice Board Student", "N"))
                        .commit();
                break;
            case 8:
                mTitle = "Faculty's Notice Board";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, noticefaculty.newInstance("Notice Board Faculty", "N"))
                        .commit();
                break;

            case 9:
                mTitle = "Academic Calendar";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CalendarAcadmic.newInstance("Academic Calendar", "A"))
                        .commit();
                break;
            case 10:
                mTitle = "Lab Records";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, lab.newInstance("Lab Records", "LR"))
                        .commit();
                break;

            case 11:
                mTitle = "Lab View";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, labview.newInstance("Lab View", "LV"))
                        .commit();
                break;
            case 12:
                mTitle = "Back Date Lab";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BackDateLab.newInstance("Back Date Lab", "BDL"))
                        .commit();
                break;

            case 13:
                mTitle = "Submit Notice";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, submitstudentnotice.newInstance("", ""))
                        .commit();
                break;

            case 14:
                mTitle = "Leave Portal";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Leave.newInstance("", ""))
                        .commit();
                break;

            case 15:
                mTitle = "Leave Check";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, leaveView.newInstance("", ""))
                        .commit();
                break;
            case 0:
            default:
                mTitle = "Home";
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Home.newInstance("Home", "H"))
                        .commit();
                break;


        }


    }

    public void onSectionAttached(int number) {
//        switch (number) {
//            case 1:
//                mTitle = getString(R.string.Home);
//                break;
//            case 2:
//                mTitle = getString(R.string.Email);
//                break;
//            case 3:
//                mTitle = getString(R.string.Gallery);
//            case 4:
//                mTitle = getString(R.string.Social);
//            case 5:
//                mTitle = getString(R.string.Phone);
//            case 6:
//                mTitle = getString(R.string.Media);
//                break;
//        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (abcd == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Home.newInstance("", ""))
                    .commit();

        } else {
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Give Your Feedback to Developer Shivam Mathur.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//Feedback
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, feedback.newInstance("feedback", "H"))
                    .commit();
            return true;
        }
//Logout
        if (id == R.id.action_settings) {
            progress1 = new ProgressDialog(MainActivity.this);
            DeRegister deRegister = new DeRegister();
            deRegister.execute(links.deregister);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public class DeRegister extends AsyncTask<String, Void, String> {
        String response = "";

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

        protected void onPreExecute() {
            progress1.setTitle("Please Wait");
            progress1.setMessage("We are Logging you out..");
            progress1.setCancelable(false);
            progress1.show();
        }

        protected void onPostExecute(String s) {
            pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            editor = pref.edit();
            editor.putBoolean("keepmelogin", false);
            editor.commit();
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Hope to see you soon.....", LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

}
