package com.example.salman.dude;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class AddTaskActivity extends AppCompatActivity {

    ImageView backButton, saveButton;
    EditText taskEdit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        backButton = (ImageView) findViewById(R.id.backBtnTask);
        saveButton = (ImageView) findViewById(R.id.addBtnTask);
        taskEdit = (EditText) findViewById(R.id.task);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String taskData = taskEdit.getText().toString().trim();
                if(taskData.isEmpty()){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Toasty.error(AddTaskActivity.this,"Task can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Task task = new Task(taskData, false);
                    databaseReference = firebaseDatabase.getInstance().getReference("Tasks");
                    databaseReference.push().setValue(task);
                    taskEdit.setText("");
                    Toasty.success(AddTaskActivity.this,"Task Added",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
