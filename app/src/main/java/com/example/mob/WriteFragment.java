package com.example.mob;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class WriteFragment extends Fragment {

    private LinedEditText linedEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false);
    }

    // В WriteFragment добавьте обработчик для поля ввода текста
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText = view.findViewById(R.id.edittxt_multilines);

    }


    // Метод для получения введенного текста из LinedEditText
    public String getEnteredText() {
        if (linedEditText != null) {
            return linedEditText.getText().toString();
        } else {
            return ""; // Возвращаем пустую строку, если linedEditText не инициализирован
        }
    }
}