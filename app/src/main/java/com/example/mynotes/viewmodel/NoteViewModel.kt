package com.example.mynotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.NoteDAO
import com.example.mynotes.model.Note
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(private val noteDao: NoteDAO) : ViewModel() {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun updateNote(
        noteId: Int,
        noteTitle: String,
        noteDescription: String
    ) {
        val updatedNote = getUpdatedNote(noteId,noteTitle,noteDescription)
        updateNote(updatedNote)
    }
    private fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    private fun getUpdatedNote(noteId: Int,noteTitle: String,noteDescription: String) : Note {
        return Note(
            id = noteId,
            title = noteTitle,
            description = noteDescription,
           )

    }

    fun addNewNote(noteTitle: String,noteDescription: String) {
        val newNote = getNewNoteEntry(noteTitle,noteDescription)
        addNote(newNote)
    }

    private fun getNewNoteEntry(noteTitle: String, noteDescription: String) :Note {
        return Note(
            title = noteTitle,
            description = noteDescription,
        )
    }

    private fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNote(id)
    }

    fun filterNotes(query: String): LiveData<List<Note>> {
        return noteDao.filterNotes(query)
    }

    fun isNoteValid(noteTitle: String, noteDescription: String): Boolean {
        if(noteTitle.isBlank() || noteDescription.isBlank()){
            return false
        }
        return true
    }
    /*
    class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
     */
}

class NoteViewModelFactory(private val noteDao: NoteDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}