package com.example.mob;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        // Обработчик нажатия на кнопку удаления
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItemWithAnimation(position);
            }

            @Override
            public void onItemClick(int position) {
                // Получаем выбранную заметку
                Note selectedNote = notesList.get(position);

                // Создаем новый экземпляр WriteFragment
                WriteFragment writeFragment = new WriteFragment();

                // Передаем данные выбранной заметки в WriteFragment
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", selectedNote.getId());
                bundle.putString("note_title", selectedNote.getTitle());
//                bundle.putString("note_location", selectedNote.getLocation());
                writeFragment.setArguments(bundle);

                // Получаем ссылку на MainActivity
                NewMainActivity mainActivity = (NewMainActivity) getActivity();
                // Скрываем кнопки в MainActivity
                mainActivity.hideButtonForWrite();

                // Заменяем текущий фрагмент на WriteFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, writeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Инициализируем DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());

        // Загружаем данные из базы данных и обновляем список заметок
        updateNoteList();

        return view;
    }

    public void updateNoteList() {
        // Получаем все заметки из базы данных
        List<Note> notes = dbHelper.getAllNotes();

        // Удаляем все элементы из списка заметок
        notesList.clear();

        // Добавляем все заметки из базы данных в список
        notesList.addAll(notes);

        // Уведомляем адаптер о конкретных изменениях
        adapter.notifyDataSetChanged();
    }

    private void removeItemWithAnimation(int position) {
        // Получаем элемент из списка заметок
        Note removedNote = notesList.get(position);

        // Удаляем элемент из базы данных
        boolean isDeleted = dbHelper.deleteNote(removedNote.getId());

        if (isDeleted) {
            // Удаляем элемент из списка с анимацией
            adapter.removeItem(position);
        } else {
            // Обработка ошибки удаления элемента
            Toast.makeText(getContext(), "Ошибка при удалении заметки", Toast.LENGTH_SHORT).show();
        }
    }
}
