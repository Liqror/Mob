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

        // Добавляем обработчик клика для элемента списка
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(clickedPosition);
                    }
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDeleteClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
