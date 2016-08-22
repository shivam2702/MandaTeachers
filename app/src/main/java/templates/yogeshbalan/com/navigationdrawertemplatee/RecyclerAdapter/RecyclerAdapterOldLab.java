package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.labRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.BackDateLab;

/**
 * Created by Shivam on 8/28/2015.
 */
public class RecyclerAdapterOldLab extends RecyclerView.Adapter<RecyclerAdapterOldLab.MyViewHolder> {
    final private Context context;
    List<labRecycler> data = Collections.emptyList();
    private LayoutInflater inflater;

    public RecyclerAdapterOldLab(Context context, List<labRecycler> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customlab, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        labRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);

        holder.filescore.setSelected(false);
        holder.attendancescore.setSelected(false);
        holder.uniformscore.setSelected(false);
        holder.labscore.setSelected(false);

        String file = BackDateLab.file.get(position);
        String att = BackDateLab.att.get(position);
        String uni = BackDateLab.uni.get(position);
        String viva = BackDateLab.viva.get(position);
        String labb = BackDateLab.labb.get(position);
        boolean blnlab = false;
        blnlab = labb.equals("1.00");
        boolean blnuni = false;
        blnuni = uni.equals("1.00");
        boolean blnfile = false;
        blnfile = file.equals("1.00");
        boolean blnatt = false;
        blnatt = att.equals("1.00");
        if (viva.equals("0.00")) {
            holder.zo.setChecked(true);
        } else if (viva.equals("0.25")) {
            holder.tf.setChecked(true);
        } else if (viva.equals("0.50")) {
            holder.fy.setChecked(true);
        } else if (viva.equals("0.75")) {
            holder.sf.setChecked(true);
        } else if (viva.equals("1.00")) {
            holder.one.setChecked(true);
        } else {
            holder.zo.setChecked(true);
        }
        holder.filescore.setChecked(blnfile);
        holder.attendancescore.setChecked(blnatt);
        holder.uniformscore.setChecked(blnuni);
        holder.labscore.setChecked(blnlab);


    }

    @Override
    public int getItemCount() {
        return BackDateLab.STUDENTROLLNO.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentname;
        TextView studentrollno;
        CheckBox filescore;
        CheckBox attendancescore;
        CheckBox labscore;
        CheckBox uniformscore;
        RadioButton zo;
        RadioButton tf;
        RadioButton fy;
        RadioButton sf;
        RadioButton one;
        RadioGroup group;
        RadioButton btn;

        public MyViewHolder(final View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name_lab);
            studentrollno = (TextView) itemView.findViewById(R.id.stu_roll_lab);
            filescore = (CheckBox) itemView.findViewById(R.id.file_lab);
            attendancescore = (CheckBox) itemView.findViewById(R.id.attendance_lab);
            labscore = (CheckBox) itemView.findViewById(R.id.lab_lab);
            uniformscore = (CheckBox) itemView.findViewById(R.id.uniform_lab);
            zo = (RadioButton) itemView.findViewById(R.id.zero);
            tf = (RadioButton) itemView.findViewById(R.id.twenttfive);
            fy = (RadioButton) itemView.findViewById(R.id.fiifty);
            sf = (RadioButton) itemView.findViewById(R.id.seventyfive);
            one = (RadioButton) itemView.findViewById(R.id.one);
            group = (RadioGroup) itemView.findViewById(R.id.group);
            filescore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filescore.isChecked()) {
                        BackDateLab.file.set(getPosition(), "1");
                        //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
                    } else {
                        BackDateLab.file.set(getPosition(), "0");
                        //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            attendancescore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attendancescore.isChecked()) {
                        BackDateLab.att.set(getPosition(), "1");
                        //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
                    } else {
                        BackDateLab.att.set(getPosition(), "0");
                        //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            labscore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (labscore.isChecked()) {
                        BackDateLab.labb.set(getPosition(), "1");
                        //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
                    } else {
                        BackDateLab.labb.set(getPosition(), "0");
                        //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            uniformscore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uniformscore.isChecked()) {
                        BackDateLab.uni.set(getPosition(), "1");
                        //Toast.makeText(context,"Position is Selected",Toast.LENGTH_SHORT).show();
                    } else {
                        BackDateLab.uni.set(getPosition(), "0");
                        //Toast.makeText(context,"Position is Deselected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            zo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackDateLab.viva.set(getPosition(), "0.00");
                }
            });
            tf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackDateLab.viva.set(getPosition(), "0.25");
                }
            });
            fy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackDateLab.viva.set(getPosition(), "0.50");
                }
            });
            sf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackDateLab.viva.set(getPosition(), "0.75");
                }
            });
            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackDateLab.viva.set(getPosition(), "1.00");
                }
            });


        }
    }
}
