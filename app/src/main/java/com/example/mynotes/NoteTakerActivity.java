package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mynotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteTakerActivity extends AppCompatActivity {
    EditText editText_title,editText_description;
    ImageView imageView_save;
    Note note;
    Boolean isOldNote = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taker);
        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_description);
        imageView_save = findViewById(R.id.imageView_save);
        note = new Note();
        try {
            note = (Note)getIntent().getSerializableExtra("old_note");
            editText_title.setText(note.getTitle());
            editText_description.setText(note.getDescription());
            isOldNote = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String description = editText_description.getText().toString();
                if(description.isEmpty())
                {
                    Toast.makeText(NoteTakerActivity.this, "Please add some note !", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote)
                {
                    note = new Note();
                }

                note.setTitle(title);
                note.setDescription(description);
                note.setDate(formatter.format(date));
                Intent intent = new Intent();
                intent.putExtra("note",note);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });
    }
}