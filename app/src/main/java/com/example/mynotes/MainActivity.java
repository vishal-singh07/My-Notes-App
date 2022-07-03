package com.example.mynotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mynotes.adapters.RVAdapter;
import com.example.mynotes.data.NoteDAO;
import com.example.mynotes.data.RoomDB;
import com.example.mynotes.model.Note;
import com.example.mynotes.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    RVAdapter adapter;
    List<Note> notes ;
    FloatingActionButton fab_add,layout_switch;
    SearchView searchView;
    boolean isGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs==null)
        prefs.edit().putBoolean("isGrid", false).commit();
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);
        layout_switch = findViewById(R.id.layout_switch);
        searchView = findViewById(R.id.searchView);
        getNotes();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteTakerActivity.class);
                startActivityForResult(intent,101);

            }
        });
        layout_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getBoolean("isGrid",false)==false)
                {
                    prefs.edit().putBoolean("isGrid", true).commit();
                    layout_switch.setImageResource(R.drawable.ic_linear_24);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                }
                else
                {
                    prefs.edit().putBoolean("isGrid", false).commit();
                    layout_switch.setImageResource(R.drawable.ic_grid_24);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }


    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        if(notes!=null)
        {
            for(Note note : notes)
            {
                if(note.getTitle().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))||
                        note.getDescription().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT)))
                {
                    filteredList.add(note);
                }
            }
            adapter.filterList(filteredList);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Note new_note = (Note) data.getSerializableExtra("note");
                noteViewModel.addNote(new_note);
                noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> allnotes) {
                        notes = allnotes;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
        else if(requestCode==102)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Note new_note = (Note) data.getSerializableExtra("note");
                noteViewModel.updateNote(new_note);
                noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> allnotes) {
                        notes = allnotes;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private final NoteClickListener listener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this,NoteTakerActivity.class);
            intent.putExtra("old_note",note);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            showPopUpMenu(note,cardView);

        }
    };
    private  void showPopUpMenu(Note note,CardView cardView)
    {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.delete:
                        noteViewModel.deleteNote(note);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Deleted !", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
    public void getNotes()
    {
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> allnotes) {
                notes = allnotes;
                adapter = new RVAdapter(getApplicationContext(),notes,listener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
               adapter.notifyDataSetChanged();
            }
        });
    }
}