package com.example.mynotes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.mynotes.model.Note

@Dao
interface NoteDAO {

    @Query("SELECT * from notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: Int): LiveData<Note>

    @Insert(onConflict = REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from notes WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun filterNotes(query: String): LiveData<List<Note>>
}
