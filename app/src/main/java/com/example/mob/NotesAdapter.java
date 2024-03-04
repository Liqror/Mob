package com.example.mob;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notesList;

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, locationTextView;
        public ImageButton deleteButton;

        public NoteViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.titleTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    // Метод для установки списка заметок
    public void setNotes(List<Note> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged(); // Обновить RecyclerView после изменения данных
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.locationTextView.setText(note.getLocation());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Действия при нажатии на кнопку удаления
                // Здесь можно удалить заметку из списка и обновить адаптер
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
