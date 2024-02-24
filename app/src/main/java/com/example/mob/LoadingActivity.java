package com.example.mob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mob.R;

public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading); // Установите свой макет для activity_loading

        // Задержка на 3 секунды (3000 миллисекунд)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Интент для запуска вашей главной активности после загрузки
                Intent intent = new Intent(LoadingActivity.this, NewMainActivity.class);
                startActivity(intent);
                // Закройте LoadingActivity, чтобы пользователь не мог вернуться к нему, нажав кнопку Назад
                finish();
            }
        }, 3000); // время задержки в миллисекундах
    }
}