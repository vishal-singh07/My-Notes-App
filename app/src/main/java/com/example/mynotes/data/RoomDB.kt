package com.example.mynotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun noteDao(): NoteDAO;

    companion object {
        @Volatile
        private var INSTANCE: RoomDB?= null

        fun getDatabase(context: Context): RoomDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "note_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }

}

/*

 */