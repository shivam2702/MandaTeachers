package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import templates.yogeshbalan.com.navigationdrawertemplate.R;
import templates.yogeshbalan.com.navigationdrawertemplatee.Recyclers.midtermRecycler;
import templates.yogeshbalan.com.navigationdrawertemplatee.fragments.MidTerm;

/**
 * Created by Shivam on 8/8/2015.
 */
public class RecyclerAdapterMidterm extends RecyclerView.Adapter<RecyclerAdapterMidterm.MyViewHolder> {
    List<midtermRecycler> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapterMidterm(Context context, List<midtermRecycler> data) {
        this.context= context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custommidterm, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        midtermRecycler current = data.get(position);
        holder.studentname.setText(current.studentname);
        holder.studentrollno.setText(current.Studentroll);
        String assposs=MidTerm.MIDTERM.get(position);
        if(assposs.equals("") || assposs.equals(null) || assposs.equals("AB") || assposs.equals("-") || assposs.equals("A") || assposs.equals("B") || assposs.equals("BA") || assposs.equals("su") || assposs.equals("s") || assposs.equals("u") || assposs.equals("us"))
        {
            holder.numbers.setText("");
        }
        else
        {
            holder.numbers.setText(assposs);
        }
    }

    @Override
    public int getItemCount() {

        return MidTerm.STUDENTROLLNO.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView studentname;
        TextView studentrollno;
        EditText numbers;

        public MyViewHolder(View itemView) {
            super(itemView);
            studentname = (TextView) itemView.findViewById(R.id.stu_name_mid);
            studentrollno = (TextView) itemView.findViewById(R.id.stu_roll_mid);
            numbers = (EditText) itemView.findViewById(R.id.number_mid);
            numbers.addTextChangedListener(new TextWatcher() {
                String num;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //num=numbers.getText().toString();
                    //Toast.makeText(context,"1."+num,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String num = numbers.getText().toString();
                    if(num.equals("") || num.equals(null) || num.equals("AB") || num.equals("A") || num.equals("B") || num.equals("BA") || num.equals("-") || num.equals("su") || num.equals("s") || num.equals("u") || num.equals("nu") || num.equals("n"))
                    {

                    }
                    else
                    {
                        int n=(Integer.parseInt(numbers.getText().toString()));
                        switch(n) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                                MidTerm.MIDTERM.set(getPosition(),""+n);
                                break;
                            default:
                                if(n>10)
                                {
                                    numbers.setText("10");
                                    Toast.makeText(context, "Invalid Input, Maximum Number allowed is 10", Toast.LENGTH_SHORT).show();
                                    MidTerm.MIDTERM.set(getPosition(),"10");
                                }
                                break;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //num=numbers.getText().toString();
                    //Toast.makeText(context,"3."+num,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
