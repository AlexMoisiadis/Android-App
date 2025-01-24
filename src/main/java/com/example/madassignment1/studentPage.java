package com.example.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class studentPage extends AppCompatActivity {


    String genderValue;
    String student_firstname;
    String student_lastname;
    String student_age;
    String student_course;
    String student_address;
    String student_id;
    EditText address;
    EditText id;
    EditText age;
    EditText name;
    EditText lastName;
    EditText course;
    String[] student;
    String[] studentData;
    TextView response;
    boolean insertConfirmed;

    DatabaseManager studentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner genderChoice = findViewById(R.id.gender);
        address = findViewById(R.id.student_address);
        age = findViewById(R.id.student_age);
        name = findViewById(R.id.student_firstname);
        lastName = findViewById(R.id.student_lastname);
        course = findViewById(R.id.student_course);
        id = findViewById(R.id.stud_id);
        response = findViewById(R.id.confirm_response);
        response.setText("");

        studentManager = new DatabaseManager(studentPage.this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderChoice.setAdapter(adapter);

        genderChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                       genderValue = genderChoice.getSelectedItem().toString();

                                                   }    // If no option selected

                                                   public void onNothingSelected(AdapterView<?> arg0) {
                                                       // TODO Auto-generated method stub

                                                   }
                                               }
        );


        Button addStudent = findViewById(R.id.student_add_button);
        addStudent.setOnClickListener(v -> {
            student_address = address.getText().toString();
            student_age = age.getText().toString();
            student_course = course.getText().toString();
            student_firstname = name.getText().toString();
            student_lastname = lastName.getText().toString();
            student_id = id.getText().toString();
            studentData = new String[] {student_id, student_firstname, student_lastname, student_age, genderValue, student_course, student_address};
            Intent intent = new Intent(this, studentEditView.class);

            address.setText("");
            id.setText("");
            name.setText("");

            intent.putExtra("student", studentData);
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