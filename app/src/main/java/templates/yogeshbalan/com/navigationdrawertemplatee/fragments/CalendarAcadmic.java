package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import templates.yogeshbalan.com.AppStatus;
import templates.yogeshbalan.com.Message;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;


public class CalendarAcadmic extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static String cal = "";
    ProgressDialog progressDialog;

    public static CalendarAcadmic newInstance(String param1, String param2) {
        CalendarAcadmic fragment = new CalendarAcadmic();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarAcadmic() {
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
        MainActivity.abcd = 0;
        rootView = inflater.inflate(R.layout.fragment_calendar_acadmic, container, false);
        // check for Internet status
        if (AppStatus.getInstance(getActivity()).isOnline()) {
            SubsamplingScaleImageView img = (SubsamplingScaleImageView) rootView.findViewById(R.id.pic);
            img.setImage(ImageSource.resource(R.drawable.cal));

        } else {
            Message.NotConnected(getActivity());
        }

        return rootView;
    }
}
