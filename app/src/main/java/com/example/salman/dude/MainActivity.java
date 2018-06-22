package com.example.salman.dude;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.navigation);


        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toasty.info(MainActivity.this,"Online",Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(MainActivity.this,"Offline",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(true);

        //Fragment View during app launch
        TaskFragment taskFragment=new TaskFragment();
        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.frameLayout,taskFragment,"Task Fragment");
        fragmentTransaction1.commit();

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_task:
                        TaskFragment taskFragment=new TaskFragment();
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frameLayout,taskFragment,"Task Fragment");
                        fragmentTransaction1.commit();
                        return true;
                    case R.id.navigation_notes:
                        NotesFragment notesFragment=new NotesFragment();
                        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout,notesFragment,"Notes Fragment");
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_profile:
                        ProfileFragment profileFragment=new ProfileFragment();
                        FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frameLayout,profileFragment,"Profile Fragment");
                        fragmentTransaction2.commit();
                        return true;
                }
                return false;
            }
        });
    }

}
