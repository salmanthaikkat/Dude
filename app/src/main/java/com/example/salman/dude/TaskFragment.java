package com.example.salman.dude;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    List<Task> Tasks=new ArrayList<>();
    List<String> keys=new ArrayList<>();
    FloatingActionButton addTaskButton;
    RelativeLayout taskMainLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView noTaskText;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final String userID=firebaseUser.getUid();
        databaseReference = firebaseDatabase.getInstance().getReference("Tasks").child(userID);
        final View view=inflater.inflate(R.layout.fragment_task, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.task_recyclerView);
        addTaskButton=(FloatingActionButton)view.findViewById(R.id.addTaskBtn);
        taskMainLayout=(RelativeLayout)view.findViewById(R.id.taskMainLayout);
        noTaskText=(ImageView) view.findViewById(R.id.noTaskText);
        taskAdapter=new TaskAdapter(Tasks);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskAdapter);
        if(Tasks.isEmpty()){
            noTaskText.setVisibility(View.VISIBLE);
        }
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Task task=dataSnapshot.getValue(Task.class);
                String keyValue=dataSnapshot.getKey();
                Tasks.add(task);
                keys.add(keyValue);
                taskAdapter.notifyDataSetChanged();
                if(!Tasks.isEmpty()){
                    noTaskText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Task taskValue=dataSnapshot.getValue(Task.class);
                String newTaskTitle=taskValue.getTask_title();
                String key=dataSnapshot.getKey();
                int index=keys.indexOf(key);
                Task task=new Task(newTaskTitle,false);
                Tasks.set(index,task);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Task task=dataSnapshot.getValue(Task.class);
                String task_title=task.getTask_title();
                for(int i=0;i<Tasks.size();i++){
                    if(Tasks.get(i).getTask_title().equals(task_title)){
                        Tasks.remove(i);
                    }
                }
                taskAdapter.notifyDataSetChanged();
                if(Tasks.isEmpty()){
                    noTaskText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddTaskActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
