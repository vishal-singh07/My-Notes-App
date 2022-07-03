package com.example.mynotes.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mynotes.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        RoomDB db = RoomDB.getInstance(application);
        noteDAO = db.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }
    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }
    public void addNote(Note note)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.insert(note);
            }
        });
        //Executed when bg task completed
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("HANDLER", "run: course inerted successfully");
            }
        });
    }
    public void updateNote(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.update(note);
            }
        });
    }
    public void deleteNote(Note note) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.delete(note);
            }
        });
    }

}
