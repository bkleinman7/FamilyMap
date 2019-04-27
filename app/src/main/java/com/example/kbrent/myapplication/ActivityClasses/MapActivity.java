package com.example.kbrent.myapplication.ActivityClasses;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.Fragments.MapFragment;
import com.example.kbrent.myapplication.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportFragmentManager().beginTransaction().add(R.id.mainMap, new MapFragment()).commit();

        ActionBar actionBar = getSupportActionBar();

        if(!User.selectedEventID.isEmpty()) {
            actionBar.setTitle("Family Map: Event Activity");
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }



}
