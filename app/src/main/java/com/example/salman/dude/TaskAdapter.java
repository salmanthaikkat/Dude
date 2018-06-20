package com.example.salman.dude;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> Tasks;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public TaskAdapter(List<Task> tasks) {
        Tasks = tasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Task task=Tasks.get(position);
        holder.taskTitle.setText(task.getTask_title());
        holder.taskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference=firebaseDatabase.getInstance().getReference("Tasks");
                Query query=databaseReference.orderByChild("task_title").equalTo(task.getTask_title());
                Snackbar snackbar=Snackbar.make(view,"Task Deleted", Snackbar.LENGTH_SHORT);
                snackbar.show();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapShot:dataSnapshot.getChildren()){
                            singleSnapShot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.taskEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EditTaskActivity.class);
                intent.putExtra("TASK_TITLE",task.getTask_title());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public ImageView taskEdit,taskDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            taskTitle=(TextView)itemView.findViewById(R.id.taskTitle);
            taskEdit=(ImageView) itemView.findViewById(R.id.editTaskBtn);
            taskDelete=(ImageView) itemView.findViewById(R.id.deleteTaskBtn);
        }
    }
}
