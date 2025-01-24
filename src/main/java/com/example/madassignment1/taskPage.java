package com.example.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.LinkedTransferQueue;

public class taskPage extends AppCompatActivity {


    String taskName;
    String taskLocation;
    String taskStatus;

    EditText task;
    EditText location;
    Spinner status;

    DatabaseManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskManager = new DatabaseManager(this);

        task = findViewById(R.id.task_name);
        location = findViewById(R.id.task_location);
        status = findViewById(R.id.task_status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        status.setAdapter(adapter);

        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                       taskStatus = status.getSelectedItem().toString();

                                                   }    // If no option selected

                                                   public void onNothingSelected(AdapterView<?> arg0) {
                                                       // TODO Auto-generated method stub

                                                   }
                                               }
        );

        Button addTask = findViewById(R.id.add_task);
        addTask.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, taskEditView.class);
            taskName = task.getText().toString();
            taskLocation = location.getText().toString();

            intent.putExtra("task", taskName);
            intent.putExtra("location", taskLocation);
            intent.putExtra("status", taskStatus);


           taskManager.addTaskRow(taskName, taskLocation, taskStatus);
            startActivity(intent);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.home_button:
                goHome();
                break;
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    public void goHome() {
        Intent intent = new Intent(this, mainActivity.class);
        startActivity(intent);
    }
}