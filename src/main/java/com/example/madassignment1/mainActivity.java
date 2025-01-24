package com.example.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addStudent = findViewById(R.id.button_addstudent);
        Button addTask = findViewById(R.id.button_addtask);
        addStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, studentPage.class);
            startActivity(intent);
        });

        addTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, taskPage.class);
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

        if (item.getItemId() == R.id.home_button) {
            goHome();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goHome() {
        Intent intent = new Intent(this, mainActivity.class);
        startActivity(intent);
    }
}