package com.example.salman.dude;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationViewEx=(BottomNavigationViewEx) findViewById(R.id.navigation);

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
