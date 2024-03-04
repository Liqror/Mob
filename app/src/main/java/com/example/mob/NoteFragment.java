package com.example.mob;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class NoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Note> notesList;
    DatabaseHelper dbHelper = new DatabaseHelper(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        // Инициализируем список заметок
        notesList = new ArrayList<>();

        // Создаем и настраиваем RecyclerView
        recyclerView = view.findViewById(R.id.note_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Создаем адаптер и устанавливаем его для RecyclerView
        adapter = new NotesAdapter(notesList);
        recyclerView.setAdapter(adapter);

        // Добавляем тестовые данные заметок
        notesList.add(new Note("Note 1", "Location 1"));
        notesList.add(new Note("Note 2", "Location 2"));


        // Уведомляем адаптер об изменениях
        adapter.notifyDataSetChanged();

        return view;
    }

    public void updateNoteList() {
        // Здесь вы можете выполнить обновление вашего RecyclerView или ListView
        // Загрузить данные из базы данных и установить их в адаптер вашего списка заметок
        // Например, если вы используете RecyclerView:
        List<Note> notes = dbHelper.getAllNotes(); // Метод для загрузки всех заметок из базы данных
        adapter.setNotes(notes); // Установить загруженные заметки в адаптер
        adapter.notifyDataSetChanged(); // Уведомить адаптер о том, что данные были изменены
    }
}