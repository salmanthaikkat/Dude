package com.example.salman.dude;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    List<Note> Notes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public NoteAdapter(List<Note> notes) {
        Notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Note note=Notes.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference=firebaseDatabase.getInstance().getReference("Notes");
                Query query=databaseReference.orderByChild("noteTitle").equalTo(note.getNoteTitle());
                Snackbar snackbar=Snackbar.make(view,"Note Deleted", Snackbar.LENGTH_SHORT);
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
        holder.noteItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),EditNoteActivity.class);
                intent.putExtra("NOTE_TITLE",note.getNoteTitle());
                intent.putExtra("NOTE_CONTENT",note.getNoteData());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle;
        public ImageView noteDelete;
        public RelativeLayout noteItemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            noteTitle=(TextView)itemView.findViewById(R.id.noteTitle);
            noteDelete=(ImageView)itemView.findViewById(R.id.deleteNoteBtn);
            noteItemView=(RelativeLayout)itemView.findViewById(R.id.noteItemView);
        }
    }
}
