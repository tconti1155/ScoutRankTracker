package com.example.thomas.scoutranktracker;
/* the following sites were used as references to create the app
    Expandable List View: http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
    Selecting items in Database: http://zetcode.com/db/sqlite/select/
    SQLite database: https://www.tutorialspoint.com/android/android_sqlite_database.htm
 */

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by Thomas on 9/10/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> regs;// creating a map for regs
    private List<String> ranks;// creating a list of ranks
    public int index;// creating other values
    public boolean reg;
    // setting context, ranks and regs.
    public ExpandableListAdapter(Activity context, List<String> ranks, Map<String, List<String>> regs) {
        this.context = context;
        this.ranks = ranks;
        this.regs = regs;
    }

    // getting the child from the regs map using the ranks list
    public Object getChild(int groupPosition, int childPosition) {
        return regs.get(ranks.get(groupPosition)).get(childPosition);
    }

    //returning the child position.
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    // get the child view from regs
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String ranks = (String) getChild(groupPosition, childPosition);// getting the regs

        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.ranks);// assigning the TextView
        item.setTypeface(null,Typeface.BOLD);// setting type face to bold
        item.setText(ranks);// setting the item
        return convertView;// returning convertView

    }

    // getting the child count
    public int getChildrenCount(int groupPosition) {
        return regs.get(ranks.get(groupPosition)).size();
    }

    //getting the ranks from group
    public Object getGroup(int groupPosition) {
        return ranks.get(groupPosition);
    }

    //getting the groupcount from ranks
    public int getGroupCount() {
        return ranks.size();
    }

    //getting the id for groupPosition
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //creating the group view
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String ranksList = (String) getGroup(groupPosition);//setting the ranklist from group position


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.ranks);//setting TextView
        item.setTypeface(null, Typeface.BOLD);//setting typeface to bold
        item.setText(ranksList);// setting the text


        return convertView;// returning context view
    }

    //checking to see if it has stable IDS
    public boolean hasStableIds()
    {
        return true;
    }

    //checking to see if child is selectable
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

}
