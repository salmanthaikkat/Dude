package com.example.salman.dude;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class AddNoteActivity extends AppCompatActivity {

    ImageView backBtn,addBtn;
    EditText noteTitleText,noteContentText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView openCameraBtn;
    ImageView textImageView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        backBtn=(ImageView)findViewById(R.id.backBtnNote);
        addBtn=(ImageView)findViewById(R.id.addNoteBtn);
        noteTitleText=(EditText) findViewById(R.id.noteTitleText);
        noteContentText=(EditText)findViewById(R.id.noteContentText);
        openCameraBtn=(ImageView) findViewById(R.id.openCameraBtn);
        textImageView=(ImageView)findViewById(R.id.textImageView);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final String userID=firebaseUser.getUid();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteTitle=noteTitleText.getText().toString().trim();
                String noteContent=noteContentText.getText().toString().trim();
                if(noteTitle.isEmpty()||noteContent.isEmpty()){
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Toasty.error(AddNoteActivity.this,"Note Title/Note Content can't be Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    Note note=new Note(noteTitle,noteContent);
                    databaseReference=firebaseDatabase.getInstance().getReference("Notes").child(userID);
                    databaseReference.push().setValue(note);
                    noteTitleText.setText("");
                    noteContentText.setText("");
                    Toasty.success(AddNoteActivity.this,"Note added Succesfully",Toast.LENGTH_SHORT).show();
                }
            }
        });

        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                ImageView icon=(ImageView)layout.findViewById(R.id.toast_icon);
                icon.setImageResource(R.drawable.ic_caution_sign);
                text.setText("This is a Beta Feature\nAccuracy is less");

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap=(Bitmap)data.getExtras().get("data");
        textImageView.setImageBitmap(bitmap);

        TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Toast.makeText(AddNoteActivity.this,"Dependecy not operational",Toast.LENGTH_SHORT).show();
        }
        else{
            Frame frame=new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items=textRecognizer.detect(frame);
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<items.size();i++){
                TextBlock myItem=items.valueAt(i);
                stringBuilder.append(myItem.getValue());
                stringBuilder.append("\n");
            }
            noteContentText.setText(stringBuilder.toString());
        }

    }
}
