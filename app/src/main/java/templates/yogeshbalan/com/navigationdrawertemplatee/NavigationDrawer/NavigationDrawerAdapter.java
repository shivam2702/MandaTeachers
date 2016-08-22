package templates.yogeshbalan.com.navigationdrawertemplatee.NavigationDrawer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;

import templates.yogeshbalan.com.navigationdrawertemplate.R;

/**
 * Created by yogesh on 1/4/15.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    String[] mString={};
    ActionBarActivity mActionBarActivity;

    public NavigationDrawerAdapter(String[] mCategoryMap, ActionBarActivity actionBarActivity) {

        this.mString = mCategoryMap;
        this.mActionBarActivity = actionBarActivity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Array.getLength(mString);
    }

    @Override
    public String getItem(int pos) {
        // TODO Auto-generated method stub
        return mString[pos];
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        View rowView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActionBarActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.drawer_item, null);
        } else {
            rowView = convertView;
        }
        TextView navigationTitle = (TextView) rowView.findViewById(R.id.name);
        navigationTitle.setText(mString[pos]);
        if(pos%2!=0)
        {
            LinearLayout lLayout = (LinearLayout)rowView.findViewById(R.id.draw);
            lLayout.setBackgroundColor(Color.parseColor("#1976D2"));
            navigationTitle.setTextColor(Color.parseColor("#FFFFFF"));

        }
        else
        {
            LinearLayout lLayout = (LinearLayout)rowView.findViewById(R.id.draw);
            lLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            navigationTitle.setTextColor(Color.parseColor("#1976D2"));
        }
        return rowView;
    }
}
