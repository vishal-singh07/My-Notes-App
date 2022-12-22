package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.adapters.NoteListAdapter
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.viewmodel.NoteViewModel
import com.example.mynotes.viewmodel.NoteViewModelFactory


class MainActivity : AppCompatActivity() {

    val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(
            (application as NoteApplication).database.noteDao(),
        )
    }


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val prefs = this.getSharedPreferences("my_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("isGrid",false).apply()


        val adapter = NoteListAdapter {
            val intent = Intent(this,NoteTakerActivity::class.java)
            intent.putExtra("old_note_id",it.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        viewModel.allNotes.observe(this) { notes ->
            notes.let {
                adapter.setNotesList(it)
            }
        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this,NoteTakerActivity::class.java)
            intent.putExtra("old_note_id",-1)
            startActivity(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText==""){
                    viewModel.allNotes.observe(this@MainActivity) { notes ->
                        notes.let {
                            adapter.setNotesList(it)
                        }
                    }
                } else {
                    viewModel.filterNotes(newText).observe(this@MainActivity) { filteredNotes ->
                        filteredNotes.let {
                            adapter.setNotesList(it)
                        }

                    }
                }
                return false
            }

        })

        binding.layoutSwitch.setOnClickListener {
            //If the layout is not grid
            if(!prefs.getBoolean("isGrid",false))
            {
                prefs.edit().putBoolean("isGrid",true).apply()
                binding.layoutSwitch.setImageResource(R.drawable.ic_grid_24)
                binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            } else {
                prefs.edit().putBoolean("isGrid",false).apply()
                binding.layoutSwitch.setImageResource(R.drawable.ic_linear_24)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }


    }

}