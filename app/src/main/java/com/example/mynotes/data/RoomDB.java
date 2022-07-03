package com.example.mynotes.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mynotes.model.Note;

@Database(entities = Note.class,version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract NoteDAO noteDAO();
    private static RoomDB database_Instance;
    private static final String db_name = "NotesDB";

    public synchronized static RoomDB getInstance(Context context)
    {
        if(database_Instance==null)
        {
            database_Instance = Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,db_name)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return database_Instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Note note1 = new Note();
            note1.setId(1);
            note1.setTitle("First note");
            note1.setDescription("This is the first note");
            note1.setDate("22/33/44");
            //Insert some values to DB at the time of creation
        }
    };


}
