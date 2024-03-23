package com.example.mob;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class WriteFragment extends Fragment {

    private LinedEditText linedEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        linedEditText = view.findViewById(R.id.edittxt_multilines);

        Bundle args = getArguments();
        if (args != null) {
            int noteId = args.getInt("note_id", -1);
            String noteTitle = args.getString("note_title");

            // Заполняем поля данными заметки
            EditText titleEditText = view.findViewById(R.id.edittxt_multilines);
            titleEditText.setText(noteTitle);

            if (noteId != -1) {
                NewMainActivity activity = (NewMainActivity) getActivity();
                if (activity != null) {
                    activity.setNoteId(noteId);
                }
                // Идентификатор заметки был передан, это означает, что редактируется существующая заметка
            } else {
                // Идентификатор заметки не был передан, это означает, что создается новая заметка
                NewMainActivity activity = (NewMainActivity) getActivity();
                if (activity != null) {
                    activity.setIsNewNote(true);
                }
            }
        }

        // Получаем переданные данные о заметке РАБОТАЕТ
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            int noteId = bundle.getInt("note_id");
//            String noteTitle = bundle.getString("note_title");
//
//            // Заполняем поля данными заметки
//            EditText titleEditText = view.findViewById(R.id.edittxt_multilines);
//            titleEditText.setText(noteTitle);
//        }

        return view;
    }

    // В WriteFragment добавьте обработчик для поля ввода текста
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText = view.findViewById(R.id.edittxt_multilines);

    }

    // Метод для получения введенного текста из LinedEditText
    public String getEnteredText() {
        if (linedEditText != null && linedEditText.getText() != null) {
            return linedEditText.getText().toString();
        } else {
            return "НЕ НАШЕЛ ТЕКСТ";
            // Возвращаем пустую строку, если linedEditText не инициализирован
        }
    }

    public void fillNoteData(int noteId, String title) {
        // Заполняем поля данными заметки
        EditText editText = getView().findViewById(R.id.edittxt_multilines);
        editText.setText(title);

        // Добавьте заполнение других полей заметки (если есть)
    }

}