package com.example.thomas.scoutranktracker;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Thomas on 9/10/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String,List<String>> regs;
    private List<String> ranks;
    public Scout scout;
    public ExpandableListAdapter(Activity context, List<String> ranks,Map<String,List<String>> regs){
        this.context=context;
        this.ranks=ranks;
        this.regs=regs;
    }

    public Object getChild(int groupPosition, int childPosition){
        return regs.get(ranks.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        final String ranks = (String) getChild(groupPosition, childPosition);

        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null){
            convertView = inflater.inflate(R.layout.child_item,null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.ranks);
        item.setText(ranks);
        scout.buttonTime();
        return convertView;

    }

    public int getChildrenCount(int groupPosition){
        return regs.get(ranks.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition){
        return ranks.get(groupPosition);
    }

    public int getGroupCount(){
        return ranks.size();
    }

    public long getGroupId(int groupPosition){
        return groupPosition;
    }
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        String ranksList = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.ranks);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(ranksList);
        return convertView;
    }
    public boolean hasStableIds(){
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }
}

