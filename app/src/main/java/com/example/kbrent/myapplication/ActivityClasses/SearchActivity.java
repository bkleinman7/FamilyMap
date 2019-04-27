package com.example.kbrent.myapplication.ActivityClasses;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.example.kbrent.myapplication.ContainerClasses.Event;
import com.example.kbrent.myapplication.ContainerClasses.Person;
import com.example.kbrent.myapplication.ContainerClasses.Search;
import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.R;
import com.example.kbrent.myapplication.AdapterClasses.SearchAdapter;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

public class SearchActivity  extends AppCompatActivity {

    private ArrayList<Search> listResultsArray = new ArrayList<>();
    private SearchAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Search");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search);

        EditText searchText = findViewById(R.id.searchText);
        ListView listResults = findViewById(R.id.peopleAndEvents);


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String search = searchText.getText().toString().toLowerCase();

                listResultsArray = new ArrayList<Search>();

                if(search.length() == 0) {
                    listResultsArray.clear();
                    arrayAdapter = new SearchAdapter(getApplication(), listResultsArray);

                    listResults.setAdapter(arrayAdapter);
                } else {

                    for (Person person : User.peopleMap.values()) {
                        String fullName = person.getFirstName() + " " + person.getLastName();
                        if (person.getFirstName().toLowerCase().contains(search) ||
                                person.getLastName().toLowerCase().contains(search) ||
                                fullName.toLowerCase().contains(search)) {

                            Drawable genderIcon;

                            if(person.getGender().equals("m")) {
                                genderIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_male).color(-16776961).sizeDp(40);
                            } else {
                                genderIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_female).color(-65536).sizeDp(40);
                            }

                            listResultsArray.add(new Search(fullName,
                                    "person", person.getPersonId(), genderIcon));
                        }
                    }

                    for (Event event : User.eventsMap.values()) {
                        boolean enterStatement = true;
                        Person person = User.peopleMap.get(event.getPersonID());
                        if (User.eventFilterMap.get(event.getEventType().toLowerCase())) {
                            enterStatement = false;
                        }
                        if (User.fatherSide.contains(person.getPersonId()) && User.filterFather) {
                            enterStatement = false;
                        }
                        if (User.motherSide.contains(person.getPersonId()) && User.filterMother) {
                            enterStatement = false;
                        }
                        if (User.filterMale && person.getGender().equals("m")) {
                            enterStatement = false;
                        }
                        if (User.filterFemale && person.getGender().equals("f")) {
                            enterStatement = false;
                        }
                        if (enterStatement) {

                            String fullName = person.getFirstName() + " " + person.getLastName();
                            if (event.getEventType().toLowerCase().contains(search) ||
                                    event.getCountry().toLowerCase().contains(search) ||
                                    event.getCity().toLowerCase().contains(search) ||
                                    Integer.toString(event.getYear()).contains(search) ||
                                    person.getFirstName().toLowerCase().contains(search) ||
                                    person.getLastName().toLowerCase().contains(search) ||
                                    fullName.toLowerCase().contains(search)) {

                                String item_content = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")" +
                                        System.getProperty("line.separator") + person.getFirstName() + " " + person.getLastName();


                                Drawable markerDrawable = new IconDrawable(getApplicationContext(),
                                        FontAwesomeIcons.fa_map_marker).color(-7829368).sizeDp(40);

                                listResultsArray.add(new Search(item_content,
                                        "event", event.getEventID(), markerDrawable));
                            }
                        }
                    }

                    arrayAdapter = new SearchAdapter(getApplication(), listResultsArray);
                    listResults.setAdapter(arrayAdapter);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listResults.setOnItemClickListener((adapterView, view, i3, l) -> {
            Search search1 = (Search) adapterView.getAdapter().getItem(i3);
            //Log.e("HTTP", search1.typeID);
            Intent intent;
            if (search1.type.equals("event")) {

                intent = new Intent(getApplicationContext(), MapActivity.class);
                User.selectedEventID = search1.typeID;
                //intent.putExtra("eventID", search1.typeID );
                startActivity(intent);

            } else if(search1.type.equals("person")) {

                intent = new Intent(getApplicationContext(), PersonActivity.class);
                User.tempPersonID = search1.typeID;
                //intent.putExtra("personID ", search1.typeID );
                startActivity(intent);
            }
        });


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
