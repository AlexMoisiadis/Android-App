package com.example.madassignment1;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class taskEditView extends AppCompatActivity {

    ExpandListAdapter listAdapt;
    ExpandableListView expandListView;
    List<String> headerList;
    HashMap<String, List<String>> childList;
    String taskName;
    String taskLocation;
    String taskStatus;

    TableLayout table;
    Button updateButton;
    Button cancelButton;
    EditText task;
    EditText location;
    Spinner status;
    TextView response;
    DatabaseManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        taskName = intent.getStringExtra("task");
        taskLocation = intent.getStringExtra("location");
        taskStatus = intent.getStringExtra("status");
        expandListView = findViewById(R.id.task_list);
        configExpandList(taskName, taskLocation, taskStatus);
        listAdapt = new ExpandListAdapter(this, headerList, childList);

        taskManager = new DatabaseManager(this);

        expandListView.setAdapter(listAdapt);
        final String[] currItem = new String[1];
        task = findViewById(R.id.edittask_name);
        location = findViewById(R.id.edittask_location);
        status = findViewById(R.id.edittask_status);
        table = findViewById(R.id.edittask_table);
        updateButton = findViewById(R.id.task_update_button);
        cancelButton = findViewById(R.id.task_cancel_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        status.setAdapter(adapter);


        expandListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            table.setVisibility(View.VISIBLE);

            currItem[0] = childList.get(headerList.get(groupPosition)).get(childPosition);

           String[] split = currItem[0].split(", ");
           if (groupPosition == 0)
           {
               status.setSelection(0);
           } else {
               status.setSelection(1);
           }
           task.setText(split[0]);
           location.setText(split[1]);


           updateButton.setOnClickListener(view -> {

               String s = task.getText().toString() + ", " + location.getText().toString();
               int ind = childList.get(headerList.get(groupPosition)).get(childPosition).indexOf(currItem[0]);

               childList.get(headerList.get(groupPosition)).set(ind, s);

               taskManager.updateTaskRecord(currItem[0], s);

           });

            response = findViewById(R.id.confirm_task);

            response.setText("From SQL: " + taskManager.selectTaskRecord(task.toString()));

            cancelButton.setOnClickListener(view -> table.setVisibility(View.GONE));

          return true;
        });


    }


    private void configExpandList(String task, String loc, String stat) {
        headerList = new ArrayList<>();
        childList = new HashMap<>();

        headerList.add("Completed");
        headerList.add("Incomplete");


        List<String> completeTasks = new ArrayList<>();
        List<String> incompleteTasks = new ArrayList<>();

        if (stat.equals("Complete"))
        {
            completeTasks.add(task + ", " + loc);
        } else {
            incompleteTasks.add(task + ", " + loc);
        }

        childList.put(headerList.get(0), completeTasks);
        childList.put(headerList.get(1), incompleteTasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.home_button) {
            goHome();
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    public void goHome() {
        Intent intent = new Intent(this, mainActivity.class);
        startActivity(intent);
    }


    public boolean showRec() {
        TextView response = findViewById(R.id.confirm_task);
        ArrayList<String> tableContent = taskManager.retrieveTaskRows();

        String[] taskSplit = new String[] {tableContent.get(0).toString(), tableContent.get(1).toString(), tableContent.get(2).toString(), tableContent.get(3).toString() };
        configExpandList(taskSplit[1], taskSplit[2], taskSplit[3]);
        response.setText("The rows in the tasks table are: \n");
        return true;
    }

}
