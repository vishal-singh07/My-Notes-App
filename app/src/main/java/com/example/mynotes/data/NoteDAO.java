package com.example.mynotes.data;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mynotes.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Query("SELECT * FROM notes order by id")
    LiveData<List<Note>> getAllNotes();

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
