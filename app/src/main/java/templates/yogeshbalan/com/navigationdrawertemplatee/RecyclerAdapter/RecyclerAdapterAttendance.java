package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.attendanceRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Attendance;

/**
 * Created by Shivam on 8/17/2015.
 */
public class RecyclerAdapterAttendance extends RecyclerView.Adapter<RecyclerAdapterAttendance.MyViewHolder> {
    List<attendanceRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterAttendance(Context context, List<attendanceRecycler> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customassignment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        attendanceRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        String assposs = Attendance.ATTENDANCE.get(position);
        if (assposs.equals("1")) {
            holder.attendance.setChecked(true);
        } else {
            holder.attendance.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return Attendance.STUDENTROLLNO.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentname;
        TextView studentrollno;
        CheckBox attendance;
        RelativeLayout layout;

        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name);
            studentrollno = (TextView) itemView.findViewById(R.id.stu_roll);
            attendance = (CheckBox) itemView.findViewById(R.id.assign_Check);
            layout = (RelativeLayout)itemView.findViewById(R.id.assign);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Attendance.ATTENDANCE.get(getPosition()).equals("1"))
                    {
                        Attendance.ATTENDANCE.set(getPosition(), "0");
                        attendance.setChecked(false);
                    }
                    else
                    {
                        Attendance.ATTENDANCE.set(getPosition(),"1");
                        attendance.setChecked(true);
                    }
                }
            });
            attendance.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (attendance.isChecked()) {
                Attendance.ATTENDANCE.set(getPosition(), "1");
                //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
            } else {
                Attendance.ATTENDANCE.set(getPosition(), "0");
                //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
