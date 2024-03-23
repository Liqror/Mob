package com.example.mob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;


public class NewMainActivity extends AppCompatActivity {

    private Button notesButton;
    private Button mapButton;
    private ImageButton plusButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        // Находим элементы интерфейса
        notesButton = findViewById(R.id.notes_button);
        mapButton = findViewById(R.id.map_button);
        plusButton = findViewById(R.id.btn_plus);
        backButton = findViewById(R.id.btn_back);

        // Устанавливаем начальный фрагмент
        replaceFragment(new NoteFragment());

        // Скрываем кнопку
        backButton.setVisibility(View.GONE);

        // Обработчики нажатия на кнопки
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new NoteFragment());
                // Показываем кнопку при отображении фрагмента NoteFragment
                plusButton.setVisibility(View.VISIBLE);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MapFragment());

                // Скрываем кнопку
                plusButton.setVisibility(View.GONE);

                // Скрываем кнопку
                backButton.setVisibility(View.GONE);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем экземпляр фрагмента "write"
                WriteFragment writeFragment = new WriteFragment();

                // Заменяем текущий фрагмент на фрагмент "write"
                replaceFragment(writeFragment);

                // Скрываем кнопку
                plusButton.setVisibility(View.GONE);

                // Показываем кнопку при отображении фрагмента NoteFragment
                backButton.setVisibility(View.VISIBLE);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем текст из фрагмента WriteFragment
                WriteFragment writeFragment = (WriteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                String title = writeFragment.getEnteredText();
                String location = "Адрес"; // Значение по умолчанию для location

                // Сохраняем заметку в базу данных
                DatabaseHelper dbHelper = new DatabaseHelper(NewMainActivity.this);
                dbHelper.addNoteToDatabase(title, location);

                // Обновляем список заметок в NoteFragment
//                NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                if (noteFragment != null) {
//                    noteFragment.updateNoteList();
//                }
                NoteFragment noteFragment = new NoteFragment();
                replaceFragment(noteFragment);

                // Возвращаемся к предыдущему фрагменту или завершаем активность
//                getSupportFragmentManager().popBackStack();

                // Показываем кнопку при отображении фрагмента NoteFragment
                plusButton.setVisibility(View.VISIBLE);

                // Скрываем кнопку
                backButton.setVisibility(View.GONE);
            }
        });
    }

    // Метод для замены фрагмента
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void hideButtonForWrite() {
        plusButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        // Скрыть другие кнопки по аналогии
    }


}
