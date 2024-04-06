package com.example.mob;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mob.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static final String ARG_MODE = "mode";
    public static final int MODE_VIEW = 1; // Режим просмотра
    public static final int MODE_EDIT = 2; // Режим редактирования
    private int currentMode = MODE_VIEW; // По умолчанию

    private GoogleMap mMap;
    private int markerCount = 0; // Счетчик для подсчета маркеров

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Инфлейтим layout для этого фрагмента
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Получаем режим из аргументов
        Bundle args = getArguments();
        if (args != null) {
            currentMode = args.getInt(ARG_MODE, MODE_VIEW);
        }

        // Получаем SupportMapFragment и уведомляем, когда карта готова к использованию.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /// Устанавливаем координаты перекрёстка в режиме просмотра
        if (currentMode == MODE_VIEW) {
            LatLng crossroads = new LatLng(47.249912481336494, 39.69719647988453);
            mMap.addMarker(new MarkerOptions().position(crossroads).title("Перекресток"));
        } else if (currentMode == MODE_EDIT) {
            // Устанавливаем слушатель кликов по карте в режиме редактирования
            mMap.setOnMapClickListener(this);
        }
        // Включаем или выключаем отображение зданий
        mMap.setBuildingsEnabled(true);
        // Включаем или выключаем внутреннюю картографию
        mMap.setIndoorEnabled(true);
    }

    @Override
    public void onMapClick(LatLng point) {
        // Действия при клике на карту в режиме редактирования
        new FetchAddressTask().execute(point);
    }

    private class FetchAddressTask extends AsyncTask<LatLng, Void, String> {
        private LatLng location; // Добавьте это поле

        @Override
        protected String doInBackground(LatLng... latLngs) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            location = latLngs[0]; // Используйте это поле
            List<Address> addresses = null;
            String addressText = "";

            try {
                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    addressText = address.getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String address) {
            if (!address.isEmpty()) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(address));
            }
        }
    }

}
