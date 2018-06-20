package com.example.salman.dude;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class EditTaskActivity extends AppCompatActivity {

    ImageView backBtn,saveBtn;
    EditText taskTitle;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        backBtn=(ImageView)findViewById(R.id.backBtn);
        saveBtn=(ImageView)findViewById(R.id.editBtn);
        taskTitle=(EditText)findViewById(R.id.task);
        final String task_title=getIntent().getStringExtra("TASK_TITLE");
        taskTitle.setText(task_title);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task_title.isEmpty()){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Toasty.error(EditTaskActivity.this,"Task can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    databaseReference=firebaseDatabase.getInstance().getReference("Tasks");
                    Query query=databaseReference.orderByChild("task_title").equalTo(task_title);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapShot:dataSnapshot.getChildren()){
                                String newTask=taskTitle.getText().toString().trim();
                                Task task=new Task(newTask,false);
                                singleSnapShot.getRef().setValue(task);
                                taskTitle.setText("");
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toasty.success(EditTaskActivity.this,"Task Edited", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
