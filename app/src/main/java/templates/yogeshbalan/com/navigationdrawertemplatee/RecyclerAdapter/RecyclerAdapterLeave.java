package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Leave;
import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.leaveRecycler;

/**
 * Created by Shivam on 9/15/2015.
 */
public class RecyclerAdapterLeave extends RecyclerView.Adapter<RecyclerAdapterLeave.MyViewHolder> {
    List<leaveRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterLeave(Context context, List<leaveRecycler> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customleave, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        leaveRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        holder.LeaveReason.setText(current.LeaveReason);
        holder.LeaveDates.setText(current.LeaveDates);
        holder.LeaveNoOfDays.setText(current.LeaveNoOfDays);
        holder.pending.setChecked(true);

    }

    @Override
    public int getItemCount() {
        return Leave.Student_rollno.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        TextView studentrollno;
        TextView LeaveReason;
        TextView LeaveDates;
        TextView LeaveNoOfDays;
        RadioButton btn;
        RadioGroup status;
        RadioButton pending;

        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name_leave);
            studentrollno = (TextView) itemView.findViewById(R.id.rollno_leave);
            LeaveReason = (TextView) itemView.findViewById(R.id.lreason);
            LeaveDates = (TextView) itemView.findViewById(R.id.ldates);
            LeaveNoOfDays = (TextView) itemView.findViewById(R.id.lnoofdays);
            status = (RadioGroup) itemView.findViewById(R.id.status);
            pending = (RadioButton) itemView.findViewById(R.id.lpending);

            status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        Leave.Status.set(getPosition(),""+rb.getText());
                        //Toast.makeText(itemView.getContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
