package com.example.mynotes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.databinding.ActivityNoteTakerBinding
import com.example.mynotes.model.Note
import com.example.mynotes.viewmodel.NoteViewModel
import com.example.mynotes.viewmodel.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteTakerActivity : AppCompatActivity() {

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(
            (application as NoteApplication).database.noteDao(),
        )
    }

    lateinit var note: Note
    private var _binding: ActivityNoteTakerBinding? = null
    private val binding get() = _binding!!
    private var oldNoteId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteTakerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        oldNoteId = intent.getIntExtra("old_note_id",0)
        if(oldNoteId>0)
        {
            binding.imageViewDelete.visibility = View.VISIBLE
            viewModel.retrieveNote(oldNoteId).observe(this) { selectedNote ->
                note = selectedNote
                bind(note)
            }
        } else {
            binding.imageViewSave.setOnClickListener {
                addNewNote()
            }
        }

    }

    private fun bind(note: Note) {
        binding.apply {
            editTextTitle.setText(note.title)
            editTextDescription.setText(note.description)
            imageViewSave.setOnClickListener {
                updateNote()
            }
            imageViewDelete.setOnClickListener {
                showConfirmationDialog()
            }
        }
    }

    private fun addNewNote() {
        if (isEntryValid()) {
            viewModel.addNewNote(
                binding.editTextTitle.text.toString(),
                binding.editTextDescription.text.toString(),
            )
            finish()
        }
    }

    private fun updateNote() {
        if (isEntryValid()) {
            viewModel.updateNote(
                oldNoteId,
                binding.editTextTitle.text.toString(),
                binding.editTextDescription.text.toString(),
            )
            finish()
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isNoteValid(
            binding.editTextTitle.text.toString(),
            binding.editTextDescription.text.toString()
        )
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteNote()
            }
            .show()
    }

    private fun deleteNote() {
        viewModel.deleteNote(note)
    }


}