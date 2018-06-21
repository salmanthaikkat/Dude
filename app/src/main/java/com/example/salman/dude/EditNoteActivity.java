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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class EditNoteActivity extends AppCompatActivity {
    ImageView backBtn,doneBtn;
    EditText noteTitle,noteContent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        backBtn=(ImageView) findViewById(R.id.backBtnNote);
        doneBtn=(ImageView)findViewById(R.id.addBtnNote);
        noteTitle=(EditText)findViewById(R.id.noteTitleText);
        noteContent=(EditText)findViewById(R.id.noteContentText);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final String userID=firebaseUser.getUid();

        final String note_title=getIntent().getStringExtra("NOTE_TITLE");
        final String note_content=getIntent().getStringExtra("NOTE_CONTENT");
        noteTitle.setText(note_title);
        noteContent.setText(note_content);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(note_title.isEmpty()||note_content.isEmpty()){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Toasty.error(EditNoteActivity.this,"Note title/Content can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    databaseReference=firebaseDatabase.getInstance().getReference("Notes").child(userID);
                    Query query=databaseReference.orderByChild("noteTitle").equalTo(note_title);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapShot:dataSnapshot.getChildren()){
                                String newNoteTitle=noteTitle.getText().toString().trim();
                                String newNoteContent=noteContent.getText().toString().trim();
                                Note note=new Note(newNoteTitle,newNoteContent);
                                singleSnapShot.getRef().setValue(note);
                                noteTitle.setText("");
                                noteContent.setText("");
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toasty.success(EditNoteActivity.this,"Note Edited", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
