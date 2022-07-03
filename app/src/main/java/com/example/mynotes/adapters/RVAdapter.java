package com.example.mynotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.NoteClickListener;
import com.example.mynotes.R;
import com.example.mynotes.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RVAdapter extends RecyclerView.Adapter<MyViewHolder>{

    Context context;
    List<Note> notes;
    NoteClickListener listener;

    public RVAdapter(Context context,List<Note> notes, NoteClickListener listener) {
        this.context = context;
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(notes.get(position).getTitle());
        holder.tvTitle.setSelected(true);//for horizontal scrolling

        holder.tvDescription.setText(notes.get(position).getDescription());

        holder.tvDate.setText(notes.get(position).getDate());
        holder.tvDate.setSelected(true);//for horizontal scrolling

        holder.note_container.setCardBackgroundColor(holder.itemView.getResources().getColor(getRandomColor(),null));//55:26

        holder.note_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(notes.get(holder.getAdapterPosition()));
            }
        });
        holder.note_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(notes.get(holder.getAdapterPosition()),holder.note_container );
                return true;
            }
        });
    }

    public int getRandomColor()
    {
        List<Integer> colors = new ArrayList<>();
        colors.add(R.color.color1);
        colors.add(R.color.color2);
        colors.add(R.color.color3);
        colors.add(R.color.color4);
        colors.add(R.color.color5);
        Random random = new Random();
        int random_color = random.nextInt(colors.size());
        return colors.get(random_color);
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void filterList(List<Note> filteredList) {
        notes = filteredList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    CardView note_container;
    TextView tvTitle,tvDescription,tvDate;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        note_container = itemView.findViewById(R.id.noteContainer);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvDescription = itemView.findViewById(R.id.tvDescription);
        tvDate = itemView.findViewById(R.id.tvDate);

    }
}
