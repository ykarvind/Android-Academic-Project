package com.amurthy.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by macbookpro on 3/19/17.
 */

public class TodolistAdapter extends ArrayAdapter<Todolist> {

    private int layoutResourceId;
    public final static String TAG = "TodolistAdapter";
    private LayoutInflater inflater;
    private List<Todolist> lists;
    public TodoHolder holder;


    public TodolistAdapter(Context context, int layoutResourceId,
                           List<Todolist> lists ) {
        super(context, layoutResourceId, lists);
        this.layoutResourceId = layoutResourceId;
        this.lists = lists;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (null == convertView) {
            Log.d(TAG, "getView: rowView null: position " + position);
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new TodoHolder();
            holder.txttitle = (TextView)convertView.findViewById(R.id.task_title);
            holder.txtdescription = (TextView)convertView.findViewById(R.id.descriptiontext);
            holder.txtduedate = (TextView)convertView.findViewById(R.id.duedatetext);
            holder.txtinformation = (TextView)convertView.findViewById(R.id.infotext);
            // Tags can be used to store data

            convertView.setTag(holder);
        }
        else {
            Log.d(TAG, "getView: rowView !null - reuse holder: position " +
                    position);
            holder = (TodoHolder)convertView.getTag();
        }
        Log.d(TAG, " getView animals " + lists.size());


        try {
            Todolist list = lists.get(position);
            holder.txttitle.setText(list.getTasktitle());
            holder.txtdescription.setText(list.getDescription());
            holder.txtduedate.setText(list.getDuedate());
            holder.txtinformation.setText(list.getInformation());
        } catch(Exception e) {
            Log.e(TAG, " getView lists " + e + " position was : " + position +
                    " lists.size: " + lists.size());
        }
        return convertView;
    }
    // This is used to cache the imageView and TextView of the
    // ImageTextArrayAdapter class
    // so they can be reused for every row in the ListView
    static class TodoHolder {
        TextView txttitle;
        TextView txtdescription;
        TextView txtduedate;
        TextView txtinformation;
    }
}