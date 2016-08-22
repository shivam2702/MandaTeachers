package templates.yogeshbalan.com.navigationdrawertemplatee.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import templates.yogeshbalan.com.navigationdrawertemplate.R;

/**
 * Created by Shivam on 12/21/2015.
 */
public class NoticeLayoutClassAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public NoticeLayoutClassAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.noticelayout, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.noticelayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.notice);
        TextView sno = (TextView) rowView.findViewById(R.id.sno);
        LinearLayout lLayout = (LinearLayout)rowView.findViewById(R.id.not);

        if(position%2!=0)
        {
            lLayout.setBackgroundColor(Color.parseColor("#1976D2"));
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            sno.setTextColor(Color.parseColor("#FFFFFF"));

        }
        else
        {
            lLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textView.setTextColor(Color.parseColor("#1976D2"));
            sno.setTextColor(Color.parseColor("#1976D2"));
        }

        textView.setText(values.get(position));
        int loc = position+1;
        sno.setText(""+loc+": ");
        return rowView;
    }

}
