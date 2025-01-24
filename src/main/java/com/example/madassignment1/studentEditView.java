package com.example.madassignment1;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class studentEditView extends AppCompatActivity {


    EditText address;
    EditText student_id;
    EditText age;
    EditText name;
    EditText lastName;
    EditText course;
    Spinner gender;
String genderValue;
    ListView list;
    Button editButton;
    Button deleteButton;

    TableLayout table;
    ArrayList<String> studentRecords;
    String[] incomingStudent;

    TextView response;
    boolean insertConfirmed;

    DatabaseManager studentManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.student_edit_view);
        list =  findViewById(R.id.list_of_students);
        table = findViewById(R.id.stud_table);

        address = findViewById(R.id.studentedit_address);
        age = findViewById(R.id.studentedit_age);
        name = findViewById(R.id.studentedit_firstname);
        lastName = findViewById(R.id.studentedit_lastname);
        course = findViewById(R.id.studentedit_course);
        student_id = findViewById(R.id.studedit_id);
        gender = findViewById(R.id.studentedit_gender);
        editButton = findViewById(R.id.student_edit_edit_button);
        deleteButton = findViewById(R.id.student_edit_delete_button);

        Intent intent = getIntent();
        studentRecords = new ArrayList<>();
        studentManager = new DatabaseManager(studentEditView.this);

        incomingStudent = intent.getStringArrayExtra("student");

        StringBuilder studrecord = new StringBuilder();
        for (String s : incomingStudent) {
            studrecord.append(s);
        }


        studentRecords.add(studrecord.toString());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(adapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                       genderValue = gender.getSelectedItem().toString();

                                                   }    // If no option selected

                                                   public void onNothingSelected(AdapterView<?> arg0) {
                                                       // TODO Auto-generated method stub

                                                   }
                                               }
        );

        insertConfirmed = studentManager.addStudentRow(Integer.parseInt( incomingStudent[0] ), incomingStudent[1], incomingStudent[2], Integer.parseInt( incomingStudent[3] ), incomingStudent[4], incomingStudent[5], incomingStudent[6] );

        response = findViewById(R.id.confirm_response);

        if (insertConfirmed)
        {
            response.setText("Successful entry");
        } else {
            response.setText("Error upon entry.");
        }

      //  InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(student_age.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        studentManager.close();

      //  list.setAdapter(null);
       if (list != null)
     {
         table.setVisibility(View.GONE);
      }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentRecords);

        list.setAdapter(itemsAdapter);

        /* Autofill upon list selection */
        list.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {

            table.setVisibility(View.VISIBLE);

                    student_id.setText(incomingStudent[0]);
                    name.setText(incomingStudent[1]);
                    lastName.setText(incomingStudent[2]);
                    if (incomingStudent[3].equals("Male")) {

                        gender.setSelection(0);

                    } else if (incomingStudent[3].equals("Female")) {
                        gender.setSelection(1);

                    } else {

                        gender.setSelection(2);
                    }
                    age.setText(incomingStudent[5]);
                    course.setText(incomingStudent[4]);
                    address.setText(incomingStudent[6]);

                    deleteButton.setOnClickListener(view -> {

                       studentRecords.remove(position);
                       itemsAdapter.notifyDataSetChanged();
            });


            editButton.setOnClickListener(view -> {

               String oldStud = (incomingStudent[0] + ", " + incomingStudent[1] + ", " + incomingStudent[2] + ", " + incomingStudent[3] + ", " + incomingStudent[4] + ", " + incomingStudent[5] + ", " + incomingStudent[6]);
              String newStud =  (name.getText().toString()  + ", " + lastName.getText().toString() + ", " + age.getText().toString()  + ", " + course.getText().toString()  + ", " + address.getText().toString());
                studentManager.updateStudentRecord(oldStud, newStud);
              studentRecords.set(position, newStud);
                itemsAdapter.notifyDataSetChanged();
            });


           response.setText("From SQL : " + studentManager.selectStudentRecord(incomingStudent[0]));
        } );
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


    public boolean showRec() {
        table.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        ArrayList<String> tableContent = studentManager.retrieveStudentRows();
        response.setText("The rows in the students table are: \n");
        ArrayAdapter<String> arrayAdpt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableContent);
        list.setAdapter(arrayAdpt);
        return true;
    }


}