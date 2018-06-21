package com.example.salman.dude;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.transition.Explode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    Button signinBtn,registerBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn=(Button)findViewById(R.id.registerBtn);
        signinBtn=(Button)findViewById(R.id.loginBtn);
        firebaseAuth=FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(LoginActivity.this);
                final View regView=getLayoutInflater().inflate(R.layout.register_dialog,null);
                final TextInputEditText usernameField=(TextInputEditText)regView.findViewById(R.id.regUsernameField);
                final TextInputEditText passwordField=(TextInputEditText)regView.findViewById(R.id.regPasswordField);
                final Button regButton=(Button)regView.findViewById(R.id.dialog_regBtn);
                regButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String username=usernameField.getText().toString().trim();
                        final String password=passwordField.getText().toString().trim();
                        if(username.isEmpty()||password.isEmpty()){
                            Toasty.error(LoginActivity.this,"Please provide credentials",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            regButton.setText("Loading...");
                            firebaseAuth.createUserWithEmailAndPassword(username,password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                bottomSheetDialog.dismiss();
                                                Toasty.success(LoginActivity.this,"Registration Success",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                regButton.setText("REGISTER");
                                                Toasty.error(LoginActivity.this,"Oops!,Please try again",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                bottomSheetDialog.setContentView(regView);
                bottomSheetDialog.show();
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(LoginActivity.this);
                final View loginView=getLayoutInflater().inflate(R.layout.login_dialog,null);
                final TextInputEditText usernameField=(TextInputEditText)loginView.findViewById(R.id.loginUsernameField);
                final TextInputEditText passwordField=(TextInputEditText)loginView.findViewById(R.id.loginPasswordField);
                final Button loginBtn=(Button)loginView.findViewById(R.id.dialog_loginBtn);
                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String username=usernameField.getText().toString().trim();
                        final String password=passwordField.getText().toString().trim();
                        if(username.isEmpty()||password.isEmpty()){
                            Toasty.error(LoginActivity.this,"Please provide credentials",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loginBtn.setText("Loading...");
                            firebaseAuth.signInWithEmailAndPassword(username,password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                                final String userEmail=firebaseUser.getEmail();
                                                final String userID=firebaseUser.getUid();
                                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("My_sharedPref",MODE_PRIVATE);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putString("userEmail",userEmail);
                                                editor.putString("userID",userID);
                                                editor.commit();
                                                bottomSheetDialog.dismiss();
                                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                                getApplicationContext().startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                loginBtn.setText("LOGIN");
                                                Toasty.error(LoginActivity.this,"Oops!Something Wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                bottomSheetDialog.setContentView(loginView);
                bottomSheetDialog.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("My_sharedPref",MODE_PRIVATE);
        String userID=sharedPreferences.getString("userID",null);
        String userEmail=sharedPreferences.getString("userEmail",null);
        if(userID!=null||userEmail!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            getApplicationContext().startActivity(intent);
        }
    }
}
