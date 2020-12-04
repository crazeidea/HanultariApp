package com.hanultari.parking;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class sidebar extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_sidebar);
        ArrayList<String> list = new ArrayList<>();

        list.add(0, "내 정보");
        list.add(1, "설정");
        list.add(2, "즐겨찾는 주차장");
        list.add(3, "주차 기록");


        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(sidebar.this, this + "가 눌렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
