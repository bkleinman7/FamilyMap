package com.example.kbrent.myapplication.ActivityClasses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.kbrent.myapplication.ContainerClasses.Event;
import com.example.kbrent.myapplication.ContainerClasses.Person;
import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.AdapterClasses.ExpandableListEvents;
import com.example.kbrent.myapplication.AdapterClasses.ExpandableList;
import com.example.kbrent.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PersonActivity extends AppCompatActivity {

    private String personID;
    private ArrayList<Person> personArrayList;
    private ArrayList<Event> events;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Family Map: Person Activity");

        if (savedInstanceState != null) {
            personID = savedInstanceState.getString("personID");
        }

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());
        if (intent.get().hasExtra("personID")) {
            personID = intent.get().getStringExtra("personID");
        }

        if(!User.tempPersonID.isEmpty()) {
            personID = User.tempPersonID;
        }

        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);

        ExpandableListView eventList = findViewById(R.id.personEvents);
        ExpandableListView familyList = findViewById(R.id.personFamily);

        Person person = User.peopleMap.get(personID);
        HashMap<String, Event> eventMap = User.eventsMap;
        HashMap<String, Person> personMap = User.peopleMap;
        events = new ArrayList<>();
        HashMap<Person, String> persons = new HashMap<>();

        //Grab events associated with person
        //But make sure to exlude filtered events
        for(Event event : eventMap.values()) {
            Person person1 = User.peopleMap.get(event.getPersonID());
            boolean shouldEnter = true;

            if(person1.getGender().equals("f")) {
                if(User.filterFemale) {
                    shouldEnter = false;
                }
            }
            if(person1.getGender().equals("m")) {
                if (User.filterMale) {
                    shouldEnter = false;
                }

            }
            if (User.motherSide.contains(person1.getPersonId())) {
                if (User.filterMother) {
                    shouldEnter = false;
                }
            }
            if (User.fatherSide.contains(person1.getPersonId())) {
                if (User.filterFather) {
                    shouldEnter = false;
                }
            }

            if (shouldEnter) {
                if (event.getPersonID().equals(personID) &&
                        !User.eventFilterMap.get(event.getEventType().toLowerCase())) {
                    events.add(event);
                }
            }
        }

        //Grab father, mother, spouse and children from this person...
        String motherID = person.getMotherId();
        String fatherID = person.getFatherId();
        String spouseID = person.getSpouseId();
        //If anyone has this personID as their fatherID or motherID then
        //This is a child of that person
        for(Person person1 : personMap.values()) {
            if (person1.getPersonId().equals(motherID)) {
                persons.put(person1, "Mother");
            }
            else if (person1.getPersonId().equals(fatherID)) {
                persons.put(person1, "Father");
            }
            else if (person1.getPersonId().equals(spouseID)) {
                persons.put(person1, "Spouse");
            }
            else if (person1.getFatherId().equals(personID)) {
                persons.put(person1, "Child");
            }
            else if (person1.getMotherId().equals(personID)) {
                persons.put(person1, "Child");
            }
        }

        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());

        if (person.getGender().equals("m")) {
            gender.setText("Male");
        }
        else {
            gender.setText("Female");
        }

        this.personArrayList = new ArrayList<>();

        this.personArrayList.addAll(persons.keySet());

        Comparator<Event> compareByYear = (Event o1, Event o2) -> Integer.toString(o1.getYear()).
                compareTo(Integer.toString(o2.getYear()) );
        Collections.sort(events, compareByYear);

        ExpandableListEvents eventAdapter = new ExpandableListEvents(getApplicationContext(), events);
        ExpandableList relativeAdapter = new ExpandableList(getApplicationContext(), persons);

        eventList.setAdapter(eventAdapter);
        familyList.setAdapter(relativeAdapter);

        familyList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            intent.set(new Intent(getApplicationContext(), PersonActivity.class));
            intent.get().putExtra("personID", personArrayList.get(childPosition).getPersonId());
            startActivity(intent.get());
            User.tempPersonID = "";
            return false;
        });

        eventList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            intent.set(new Intent(getApplicationContext(), MapActivity.class));
            intent.get().putExtra("eventID", events.get(childPosition).getEventID());
            User.selectedEventID = events.get(childPosition).getEventID();
            startActivity(intent.get());
            User.tempPersonID = "";
            return false;
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putString("personID", personID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            User.selectedEventID = "";
            User.tempPersonID = "";
            startActivity(intent);
            return true;
        }
        return true;
    }

}
