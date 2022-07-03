package com.example.mynotes;

import androidx.cardview.widget.CardView;

import com.example.mynotes.model.Note;

public interface NoteClickListener {
    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);
}
