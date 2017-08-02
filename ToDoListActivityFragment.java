package com.amurthy.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ToDoListActivityFragment extends Fragment {

    public final static String TAG = "Todolist";

    List<Todolist> lists = new ArrayList<Todolist>();
    ArrayAdapter<Todolist> adapter = null;

    private DBHelper dbHelper = null;

    public ToDoListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        try {
            dbHelper = new DBHelper(getActivity());
            lists = dbHelper.selectAll();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }

        ListView list = (ListView)getActivity().findViewById(R.id.todo_list);

        adapter = new TodolistAdapter(getActivity(),R.layout.row, lists);

        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                onDelete(view, position);
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onShow();

            }


        });

        Button saveButton = (Button) getActivity().findViewById(R.id.btnsave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSave();
            }
        });
    }

    private void onSave() {
        Todolist list = new Todolist();
        EditText tasktitle = (EditText) getActivity().findViewById(R.id.task_title);
        EditText description = (EditText) getActivity().findViewById(R.id.descriptiontext);
        EditText duedate = (EditText) getActivity().findViewById(R.id.duedatetext);
        EditText information = (EditText) getActivity().findViewById(R.id.infotext);

        String task = tasktitle.getText().toString();
        if (TextUtils.isEmpty(task)) {
            showMissingInfoAlert();
        } else {
            list.setTasktitle(tasktitle.getText().toString());
            list.setDescription(description.getText().toString());
            list.setDuedate(duedate.getText().toString());
            list.setInformation(information.getText().toString());

            long listId = 0;
            if (dbHelper != null) {
                listId = dbHelper.insert(list);
                list.setId(listId);
            }

            // Add the object at the end of the array.
            adapter.add(list);
            // Notifies the adapter that the underlying data has changed,
            // any View reflecting the data should refresh itself.
            adapter.notifyDataSetChanged();

            // Remove the soft keyboard after hitting the save button
            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private void onDelete(View view, int position){
        // When clicked, delete the item that was clicked.
        // (Show a toast to indicate what is occurring)
        Todolist list = adapter.getItem(position);
        if (list != null) {
            String item = "deleting: " + list.getTasktitle();
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemClick: " + list.getTasktitle());

            // database delete record
            if (dbHelper != null) dbHelper.deleteRecord(list.getId());

            // Removes the object from the array
            adapter.remove(list);
            // Notifies t that the underlying data has changed
            adapter.notifyDataSetChanged();
        }
    }

    public void showMissingInfoAlert() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);

        if (getActivity().findViewById(R.id.task_title) != null) {
            alertDialogBuilder.setTitle(getResources().getString(R.string.alert_title));
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            // set dialog message
            alertDialogBuilder.setMessage(getResources().getString(R.string.alert_message1))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close current activity
                            dialog.cancel();
                        }
                    });

        }
            else if(getActivity().findViewById(R.id.duedatetext) != null)
            {
                alertDialogBuilder.setTitle(getResources().getString(R.string.alert_date));
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                // set dialog message
                alertDialogBuilder.setMessage(getResources().getString(R.string.alert_message2))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close current activity
                                dialog.cancel();
                            }
                        });
            }
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }

    public void onShow() {

        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle(getResources().getString(R.string.additionalinfo));
            alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            // set dialog message
            alertDialogBuilder.setMessage(getResources().getString(R.string.alert_message1))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close current activity
                            dialog.cancel();
                        }
                    });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();


    }


}
