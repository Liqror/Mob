package com.example.mob;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WriteFragment extends Fragment {

    private static final String API_KEY = "eb321b81d96945f8b2495153242405";

    private LinedEditText linedEditText;
    private boolean isKeyboardShowing = false;
    private View infLayout;
    private TextView geoText;
    private TextView weatherText;

    int noteId;
    String noteTitle;
    String noteLocation;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        linedEditText = view.findViewById(R.id.edittxt_multilines);
        infLayout = view.findViewById(R.id.inf_layout);
        geoText = view.findViewById(R.id.geo_text);
        weatherText = view.findViewById(R.id.weather_text);
        ImageButton btnRe1 = view.findViewById(R.id.btn_re_1);
        ImageButton btnRe2 = view.findViewById(R.id.btn_re_2);

        // Добавляем слушатель для отслеживания видимости клавиатуры
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Клавиатура открыта
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true;
                        onKeyboardVisibilityChanged(true);
                    }
                } else {
                    // Клавиатура закрыта
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        onKeyboardVisibilityChanged(false);
                    }
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            noteId = args.getInt("note_id", -1);
            noteTitle = args.getString("note_title");
            noteLocation = args.getString("note_location");

            // Заполняем поля данными заметки
            EditText titleEditText = view.findViewById(R.id.edittxt_multilines);
            titleEditText.setText(noteTitle);
            geoText.setText(noteLocation);

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

        btnRe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = noteId;

                // Создаем экземпляр MapFragment
                MapFragment mapFragment = new MapFragment();
                Bundle args = new Bundle();
                args.putInt(MapFragment.ARG_MODE, MapFragment.MODE_EDIT);
                args.putInt("note_id", id); // Передача noteId в аргументы
                args.putString("note_title", noteTitle);
                mapFragment.setArguments(args);

                // Получаем ссылку на MainActivity
                NewMainActivity mainActivity = (NewMainActivity) getActivity();
                // Устанавливаем слушатель для фрагмента
                mapFragment.setDataSelectedListener(mainActivity);
                // Скрываем кнопки в MainActivity
                mainActivity.hideButtonToMap();

                // Заменяем текущий фрагмент на MapFragment
                if (getActivity() instanceof NewMainActivity) {
                    ((NewMainActivity) getActivity()).replaceFragment(mapFragment);
                }
            }
        });

        btnRe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Пример строки noteLocation
//                String noteLocation = "Название: 9926 Svanvik, Норвегия\nШирота: 69.11699650145204\nДолгота: 28.89207322150469";

                // Извлечь широту и долготу из строки noteLocation
                double latitude = extractLatitude(noteLocation);
                double longitude = extractLongitude(noteLocation);

                if (latitude != 0 && longitude != 0) {
                    getWeather(latitude, longitude);
                } else {
                    weatherText.setText("Не удалось определить координаты");
                }
            }
        });

        return view;
    }

    // Метод для извлечения широты из строки noteLocation
    private double extractLatitude(String noteLocation) {
        // Разделите строку по разделителю и извлеките широту
        if (noteLocation.contains("Широта: ")) {
            String[] parts = noteLocation.split("Широта: ");
            if (parts.length > 1) {
                String[] locationParts = parts[1].split("\n");
                if (locationParts.length > 0) {
                    return Double.parseDouble(locationParts[0]);
                }
            }
        }
        return 0;
    }

    // Метод для извлечения долготы из строки noteLocation
    private double extractLongitude(String noteLocation) {
        // Разделите строку по разделителю и извлеките долготу
        if (noteLocation.contains("Долгота: ")) {
            String[] parts = noteLocation.split("Долгота: ");
            if (parts.length > 1) {
                String[] locationParts = parts[1].split("\n");
                if (locationParts.length > 0) {
                    return Double.parseDouble(locationParts[0]);
                }
            }
        }
        return 0;
    }

    private String extractCityName(String noteLocation) {
        // Разделите строку по разделителю и извлеките название города
        if (noteLocation.contains("Название: ")) {
            String[] parts = noteLocation.split("Название: ");
            if (parts.length > 1) {
                String[] locationParts = parts[1].split("\n");
                if (locationParts.length > 0) {
                    return locationParts[0];
                }
            }
        }
        return null;
    }

    // Метод для получения погоды по координатам
    private void getWeather(double latitude, double longitude) {
        WeatherAPI weatherAPI = RetrofitClient.getClient("https://api.weatherapi.com/").create(WeatherAPI.class);

        String coordinates = String.format(Locale.ENGLISH, "%.4f,%.4f", latitude, longitude);
        Call<WeatherResponse> call = weatherAPI.getCurrentWeather(API_KEY, coordinates, "no");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    double temperatureCelsius = weatherResponse.current.temp_c;
                    String conditionText = weatherResponse.current.condition.text;

                    String result = String.format(Locale.ENGLISH, "%.1f°C, %s", temperatureCelsius, conditionText);
                    weatherText.setText(result);
                } else {
                    weatherText.setText("Ошибка получения данных о погоде");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherText.setText("Ошибка: " + t.getMessage());
            }
        });
    }


//    private void getWeather(String cityName) {
//        WeatherAPI weatherAPI = RetrofitClient.getClient("https://api.weatherapi.com/").create(WeatherAPI.class);
//
//        Call<WeatherResponse> call = weatherAPI.getCurrentWeather(API_KEY, cityName, "no");
//        call.enqueue(new Callback<WeatherResponse>() {
//            @Override
//            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    WeatherResponse weatherResponse = response.body();
//                    double temperatureCelsius = weatherResponse.current.temp_c;
//                    String conditionText = weatherResponse.current.condition.text;
//
//                    String result = String.format("%.1f°C, %s", temperatureCelsius, conditionText);
//                    weatherText.setText(result);
//                } else {
//                    weatherText.setText("Ошибка получения данных о погоде");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherResponse> call, Throwable t) {
//                weatherText.setText("Ошибка: " + t.getMessage());
//            }
//        });
//    }

    // Метод для обработки изменений видимости клавиатуры
    private void onKeyboardVisibilityChanged(boolean opened) {
        if (opened) {
            // Клавиатура открыта, скрываем блок inf_layout
            infLayout.setVisibility(View.GONE);
        } else {
            // Клавиатура закрыта, показываем блок inf_layout
            infLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public void receiveData(int noteId, String noteTitle, String title, double latitude, double longitude) {

        // Обработка полученных данных
        String data = "Название: " + title + "\nШирота: " + latitude + "\nДолгота: " + longitude;
        if (geoText != null) {
            geoText.setText(data);
        } else {
            geoText.setText("-");
        }
//        Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
    }

    public String getLocation() {
        String geoTextContent = geoText.getText().toString();
        return geoTextContent;
    }
}