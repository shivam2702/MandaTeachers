package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.labRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.labview;

/**
 * Created by Shivam on 8/17/2015.
 */
public class RecyclerAdapterCheckLab extends RecyclerView.Adapter<RecyclerAdapterCheckLab.MyViewHolder> {
    List<labRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterCheckLab(Context context, List<labRecycler> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custumlabview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        labRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        holder.filescore.setText(labview.file.get(position));
        holder.attendancescore.setText(labview.att.get(position));
        holder.labscore.setText(labview.labb.get(position));
        holder.uniformscore.setText(labview.uni.get(position));
        holder.viva.setText(labview.viva.get(position));
        holder.total.setText(labview.total.get(position));
    }

    @Override
    public int getItemCount() {
        return labview.STUDENTROLLNO.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        TextView studentrollno;
        TextView filescore;
        TextView attendancescore;
        TextView labscore;
        TextView uniformscore;
        TextView viva;
        TextView total;


        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stuname);
            studentrollno = (TextView) itemView.findViewById(R.id.rollno);
            filescore = (TextView) itemView.findViewById(R.id.file);
            attendancescore = (TextView)itemView.findViewById(R.id.attendance);
            labscore = (TextView) itemView.findViewById(R.id.lab);
            uniformscore = (TextView) itemView.findViewById(R.id.uniform);
            viva = (TextView)itemView.findViewById(R.id.viva);
            total = (TextView)itemView.findViewById(R.id.total);
        }

    }
}
