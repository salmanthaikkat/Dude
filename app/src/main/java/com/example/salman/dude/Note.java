package com.example.salman.dude;

public class Note {
    String NoteTitle,NoteData;

    public Note() {
    }

    public Note(String noteTitle, String noteData) {
        NoteTitle = noteTitle;
        NoteData = noteData;
    }

    public String getNoteTitle() {
        return NoteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        NoteTitle = noteTitle;
    }

    public String getNoteData() {
        return NoteData;
    }

    public void setNoteData(String noteData) {
        NoteData = noteData;
    }
}
