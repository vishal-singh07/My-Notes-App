package com.example.mynotes

import android.app.Application
import com.example.mynotes.data.RoomDB

class NoteApplication : Application() {
    // Using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: RoomDB by lazy { RoomDB.getDatabase(this) }
}