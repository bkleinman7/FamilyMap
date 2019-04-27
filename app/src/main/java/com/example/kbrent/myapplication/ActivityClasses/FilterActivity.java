package com.example.kbrent.myapplication.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.kbrent.myapplication.ContainerClasses.Event;
import com.example.kbrent.myapplication.ContainerClasses.Filter;
import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.AdapterClasses.FilterAdapter;
import com.example.kbrent.myapplication.R;

import java.util.ArrayList;
import java.util.TreeSet;

public class FilterActivity extends AppCompatActivity {

    ArrayList<String> eventNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Filter");
        actionBar.setDisplayHomeAsUpEnabled(true);

        TreeSet<String> eventType = new TreeSet<>();

        for(Event event : User.eventsMap.values()) {
            eventType.add(event.getEventType().toLowerCase());
        }

        ArrayList<Filter> arrayOfEvents = new ArrayList<>();

        for (String eventTypeString : eventType) {

            boolean isEnabled = User.eventFilterMap.get(eventTypeString);

            arrayOfEvents.add(new Filter(eventTypeString + " Events",
                    "FILTER BY " + eventTypeString.toUpperCase() + " EVENTS",
                    isEnabled, new buttonListener(eventTypeString)));
        }

        arrayOfEvents.add(new Filter( "Father's Side",
                "FILTER BY FATHER'S SIDE OF FAMILY",
                User.filterFather, new buttonListener("father")));

        arrayOfEvents.add(new Filter("Mother's Side",
                "FILTER BY MOTHER'S SIDE OF FAMILY",
                User.filterMother, new buttonListener("mother")));

        arrayOfEvents.add(new Filter("Male Events",
                "FILTER EVENTS BASED ON GENDER",
                User.filterMale, new buttonListener("male")));

        arrayOfEvents.add(new Filter("Female Events",
                "FILTER EVENTS BASED ON GENDER",
                User.filterFemale, new buttonListener("female")));

        FilterAdapter adapter = new FilterAdapter(this, arrayOfEvents);
        ListView filterSwitchList = findViewById(R.id.filterList);
        filterSwitchList.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            User.selectedEventID = "";
            startActivity(intent);
            return true;
        }
        return true;
    }
}
