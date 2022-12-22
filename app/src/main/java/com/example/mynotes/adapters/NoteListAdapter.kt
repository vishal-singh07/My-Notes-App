package com.example.mynotes.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R.color
import com.example.mynotes.databinding.NoteItemBinding
import com.example.mynotes.model.Note
import kotlin.random.Random

class NoteListAdapter(private val onItemClicked: (Note) -> Unit) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>(){

    var notes = mutableListOf<Note>()
    fun setNotesList(movies: List<Note>) {
        this.notes = movies.toMutableList()
        notifyDataSetChanged()
    }
    private fun getRandomColor() : Int {
        val colors = listOf(color.color1, color.color2, color.color3, color.color4, color.color5)
        val randomColor = Random.nextInt(0,4)
        return colors[randomColor]
    }
    class NoteViewHolder(private var binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(note: Note)
        {
            binding.tvTitle.text = note.title
            binding.tvDescription.text = note.description
            binding.tvDate.text = note.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NoteItemBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.itemView.setOnClickListener {
            onItemClicked(currentNote)
        }
        holder.bind(currentNote)
        holder.itemView.setBackgroundColor(holder.itemView.resources.getColor(getRandomColor(),null))
    }


    override fun getItemCount(): Int {
        return notes.size
    }
}