package com.example.salman.dude;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Note> Notes=new ArrayList<>();
    List<String> Keys=new ArrayList<>();
    FloatingActionButton addNoteBtn;
    ImageView noNoteText;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notes, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final String userID=firebaseUser.getUid();
        databaseReference=firebaseDatabase.getInstance().getReference("Notes").child(userID);
        recyclerView=(RecyclerView)view.findViewById(R.id.note_recyclerView);
        addNoteBtn=(FloatingActionButton)view.findViewById(R.id.addNoteBtn);
        noNoteText=(ImageView)view.findViewById(R.id.noNoteText);
        noteAdapter=new NoteAdapter(Notes);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        if(Notes.isEmpty()){
            noNoteText.setVisibility(View.VISIBLE);
        }

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note note=dataSnapshot.getValue(Note.class);
                String keyValue=dataSnapshot.getKey();
                Notes.add(note);
                Keys.add(keyValue);
                noteAdapter.notifyDataSetChanged();
                if(!Notes.isEmpty()){
                    noNoteText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Note note=dataSnapshot.getValue(Note.class);
                String newNoteTitle=note.getNoteTitle();
                String newNoteContent=note.getNoteData();
                String key=dataSnapshot.getKey();
                int index=Keys.indexOf(key);
                Note note1=new Note(newNoteTitle,newNoteContent);
                Notes.set(index,note1);
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Note note=dataSnapshot.getValue(Note.class);
                String noteTitle=note.getNoteTitle();
                for(int i=0;i<Notes.size();i++){
                    if(Notes.get(i).getNoteTitle().equals(noteTitle)){
                        Notes.remove(i);
                    }
                }
                noteAdapter.notifyDataSetChanged();
                if(!Notes.isEmpty()){
                    noNoteText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddNoteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
