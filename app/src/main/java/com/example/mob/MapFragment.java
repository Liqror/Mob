package com.example.mob;

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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Инфлейтим layout для этого фрагмента
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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

        // Устанавливаем координаты перекрёстка
        LatLng crossroads = new LatLng(47.249912481336494, 39.69719647988453);

        // Добавляем маркер на карту
        mMap.addMarker(new MarkerOptions().position(crossroads).title("Перекресток"));

        // Включаем или выключаем отображение зданий
        mMap.setBuildingsEnabled(true);

        // Включаем или выключаем внутреннюю картографию
        mMap.setIndoorEnabled(true);
    }
}
