package com.example.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mynotes.data.NoteRepository;
import com.example.mynotes.model.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }
    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }
    public void addNote(Note note)
    {
        noteRepository.addNote(note);
    }
    public void updateNote(Note note)
    {
        noteRepository.updateNote(note);
    }
    public void deleteNote(Note note)
    {
        noteRepository.deleteNote(note);
    }
}
