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
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.assignmentRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.Assignment;

/**
 * Created by Shivam on 8/7/2015.
 */
public class RecyclerAdapterAssignment extends RecyclerView.Adapter<RecyclerAdapterAssignment.MyViewHolder> {
    List<assignmentRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterAssignment(Context context, List<assignmentRecycler> data) {
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customassignment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        assignmentRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        String assposs=Assignment.ASSIGNMENT.get(position);
        if(assposs.equals("1"))
        {
            holder.assignment.setChecked(true);
        }
        else
        {
            holder.assignment.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return Assignment.STUDENTNAME.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView studentname;
        TextView studentrollno;
        CheckBox assignment;
        RelativeLayout layout;
        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name);
            studentrollno = (TextView) itemView.findViewById(R.id.stu_roll);
            assignment = (CheckBox) itemView.findViewById(R.id.assign_Check);
            layout = (RelativeLayout)itemView.findViewById(R.id.assign);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Assignment.ASSIGNMENT.get(getPosition()).equals("1"))
                    {
                        Assignment.ASSIGNMENT.set(getPosition(), "0");
                        assignment.setChecked(false);
                    }
                    else
                    {
                        Assignment.ASSIGNMENT.set(getPosition(),"1");
                        assignment.setChecked(true);
                    }
                }
            });
            assignment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(assignment.isChecked())
            {
                Assignment.ASSIGNMENT.set(getPosition(),"1");
                //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Assignment.ASSIGNMENT.set(getPosition(),"0");
                //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
