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
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.chkAllotment;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.checkallotment;


/**
 * Created by Shivam on 8/7/2015.
 */
public class RecyclerAdapterChkSubject extends RecyclerView.Adapter<RecyclerAdapterChkSubject.MyViewHolder> {
    List<chkAllotment> data = Collections.emptyList();

    private LayoutInflater inflater;

    public RecyclerAdapterChkSubject(Context context, List<chkAllotment> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customchk, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        chkAllotment current = data.get(position);
        holder.subname.setText(current.subjectname);
    }

    @Override
    public int getItemCount() {
        return checkallotment.LECTNAME.size();

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subname;

        public MyViewHolder(View itemView) {
            super(itemView);
            subname = (TextView) itemView.findViewById(R.id.subname);
        }
    }
}
