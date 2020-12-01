package com.hanultari.parking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView recycler_View;
    ArrayList<com.hanultari.parking.ParkingDTO> dtos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_main);

        dtos = new ArrayList<>();

        recycler_View = findViewById(R.id.recycler_View);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler_View.setLayoutManager(layoutManager);

        com.hanultari.parking.ParkingAdapter adapter = new com.hanultari.parking.ParkingAdapter(this, dtos);
        adapter.addDto(new com.hanultari.parking.ParkingDTO("무료", "서구청주차장A", "300m 이내에 있어요", R.drawable.dream01));
        adapter.addDto(new com.hanultari.parking.ParkingDTO("유료", "서구청주차장B", "200m 이내에 있어요", R.drawable.dream01));
        adapter.addDto(new com.hanultari.parking.ParkingDTO("유료", "농성동 주차장", "100m 이내에 있어요", R.drawable.dream01));


        recycler_View.setAdapter(adapter);
    }
}