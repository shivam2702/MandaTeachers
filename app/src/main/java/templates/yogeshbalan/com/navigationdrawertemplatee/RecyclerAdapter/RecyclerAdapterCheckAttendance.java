package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.attendanceRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.AttendanceView;

/**
 * Created by Shivam on 8/17/2015.
 */
public class RecyclerAdapterCheckAttendance extends RecyclerView.Adapter<RecyclerAdapterCheckAttendance.MyViewHolder> {
    List<attendanceRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterCheckAttendance(Context context, List<attendanceRecycler> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custombackattendance, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        attendanceRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        String assposs = AttendanceView.ATTENDANCE.get(position);
        if (assposs.equals("1")) {
            holder.attendancestatus.setText("Present");
                holder.attendancestatus.setTextColor(Color.parseColor("#106E5B"));
        } else {
            holder.attendancestatus.setText("Absent");
            holder.attendancestatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return AttendanceView.STUDENTROLLNO.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        TextView studentrollno;
        TextView attendancestatus;
        RelativeLayout layout;

        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name_back);
            studentrollno = (TextView) itemView.findViewById(R.id.stu_roll_back);
            attendancestatus = (TextView) itemView.findViewById(R.id.attendance_back);
            layout = (RelativeLayout)itemView.findViewById(R.id.assign_back);

        }

    }
}
