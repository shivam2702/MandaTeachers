package templates.yogeshbalan.com.navigationdrawertemplatee.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.MainActivity;

public class newallotment extends Fragment {
//    String semester;
//    String branch;
//    String classname;
//    String subjectlistforselection[]={"","","","","",""};
//    String subjectname;
//    String subjectnowithclass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_newallotment, container, false);
        MainActivity.abcd=0;
//
//        final Spinner sem = (Spinner) rootView.findViewById(R.id.semester);
//        ArrayAdapter<String> spinnerArrayAdapterSem = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.totsem));
//        spinnerArrayAdapterSem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sem.setAdapter(spinnerArrayAdapterSem);
//        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                switch (pos) {
//                    case 1:
//                        semester = "i";
//                        break;
//                    case 2:
//                        semester = "ii";
//                        break;
//                    case 3:
//                        semester = "iii";
//                        break;
//                    case 4:
//                        semester = "iv";
//                        break;
//                    case 5:
//                        semester = "v";
//                        break;
//                    case 6:
//                        semester = "vi";
//                        break;
//                    case 7:
//                        semester = "vii";
//                        break;
//                    case 8:
//                        semester = "viii";
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//
//        });
//        Spinner Branch = (Spinner) rootView.findViewById(R.id.branch);
//        ArrayAdapter<String> spinnerArrayAdapterBranch = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.totbranch));
//        spinnerArrayAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Branch.setAdapter(spinnerArrayAdapterBranch);
//        final Spinner subject = (Spinner) rootView.findViewById(R.id.subject);
//        Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                switch (pos) {
//                    case 1:
//                        branch = "cs";
//                        break;
//                    case 2:
//                        branch = "ce";
//                        break;
//                    case 3:
//                        branch = "me";
//                        break;
//                    case 4:
//                        branch = "ee";
//                        break;
//                    case 5:
//                        branch = "ec";
//                        break;
//                    case 6:
//                        branch = "ft";
//                        break;
//                }
//                classname = semester + branch;
//                String url = "http://mittest.bugs3.com/allsubjectofclass.php?param1=" + classname + "";
//                VolleySingleton volleySingleton = VolleySingleton.getInstance();
//                RequestQueue requestQueue = volleySingleton.getRequestQueue();
//                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(response.toString(), "");
//                        if (response == null || response.length() == 0) {
//                            Toast.makeText(getActivity(), "Yaha", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        try {
//                            JSONArray array = response.getJSONArray("myoutput");
//                            JSONObject cur = array.getJSONObject(0);
//                            subjectlistforselection[0] = cur.getString("subject1");
//                            subjectlistforselection[1] = cur.getString("subject2");
//                            subjectlistforselection[2] = cur.getString("subject3");
//                            subjectlistforselection[3] = cur.getString("subject4");
//                            subjectlistforselection[4] = cur.getString("subject5");
//                            subjectlistforselection[5] = cur.getString("subject6");
//                            ArrayAdapter<String> spinnerArrayAdapterSubject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjectlistforselection);
//                            spinnerArrayAdapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            subject.setAdapter(spinnerArrayAdapterSubject);
//                            subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                                    subjectname = subjectlistforselection[pos];
//                                    int subjectno = pos + 1;
//                                    subjectnowithclass = semester + branch + "sub" + subjectno;
//                                    classname = semester + branch;
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> arg0) {
//                                }
//
//                            });
//                        } catch (Exception e) {
//                            Toast.makeText(getActivity(), "Catch", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyErrors.handledVolleyError(getActivity(), error);
//                    }
//                });
//                requestQueue.add(request);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//
//        });
//
//            Button submit = (Button)rootView.findViewById(R.id.button);
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (subjectname.equals("") && subjectname.equals(null)) {
//                        Toast.makeText(getActivity(), "Select The Subject first", Toast.LENGTH_SHORT).show();
//                    } else {
//                        String url = "http://mittest.bugs3.com/subjectalloted.php?param1=" + LoginPage.ID + "&param2=" + subjectname + "&param3=" + classname + "&param4=" + subjectnowithclass + "";
//                        VolleySingleton volleySingleton = VolleySingleton.getInstance();
//                        RequestQueue requestQueue = volleySingleton.getRequestQueue();
//                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d(response.toString(), "");
//                                if (response == null || response.length() == 0) {
//                                    Toast.makeText(getActivity(), "Yaha", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                String message;
//                                try {
//                                    JSONArray array = response.getJSONArray("myoutput");
//                                    JSONObject cur = array.getJSONObject(0);
//                                    message = cur.getString("message");
//                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                                } catch (Exception e) {
//                                    Toast.makeText(getActivity(), "Catch", Toast.LENGTH_LONG).show();
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                VolleyErrors.handledVolleyError(getActivity(), error);
//                            }
//                        });
//                        requestQueue.add(request);
//                    }
//                }
//            });
//
        return rootView;
    }
}
