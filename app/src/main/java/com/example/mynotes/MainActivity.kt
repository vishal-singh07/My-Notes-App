package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.adapters.NoteListAdapter
import com.example.mynotes.data.SettingsDataStore
import com.example.mynotes.data.dataStore
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.viewmodel.NoteViewModel
import com.example.mynotes.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(
            (application as NoteApplication).database.noteDao(),
        )
    }


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    // Keeps track of which LayoutManager is in use for the [RecyclerView]
    private var isLinearLayoutManager = true

    private lateinit var SettingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

        SettingsDataStore = SettingsDataStore(this.dataStore)
        SettingsDataStore.preferenceFlow.asLiveData().observe(this) { value ->
            isLinearLayoutManager = value
            chooseLayout()
            this.invalidateOptionsMenu()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.layout_menu, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Sets isLinearLayoutManager (a Boolean) to the opposite value
                isLinearLayoutManager = !isLinearLayoutManager

                // Launch a coroutine and write the layout setting in the preference Datastore
                lifecycleScope.launch {
                    SettingsDataStore.saveLayoutToPreferencesStore(isLinearLayoutManager, this@MainActivity)
                }

                // Sets layout and icon
                chooseLayout()
                setIcon(item)

                return true
            }
            // Otherwise, do nothing and use the core event handling

            // when clauses require that all possible paths be accounted for explicitly,
            // for instance both the true and false cases if the value is a Boolean,
            // or an else to catch all unhandled cases.
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_grid_24)
            else ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_linear)
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
//        binding.recyclerView.adapter = LetterAdapter()
    }
}