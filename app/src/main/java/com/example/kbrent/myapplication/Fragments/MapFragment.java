package com.example.kbrent.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbrent.myapplication.ActivityClasses.FilterActivity;
import com.example.kbrent.myapplication.ActivityClasses.MainActivity;
import com.example.kbrent.myapplication.ActivityClasses.PersonActivity;
import com.example.kbrent.myapplication.ActivityClasses.SearchActivity;
import com.example.kbrent.myapplication.ActivityClasses.SettingsActivity;
import com.example.kbrent.myapplication.ContainerClasses.Event;
import com.example.kbrent.myapplication.ContainerClasses.Person;
import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private TextView eventText;
    private String personId_for_service;
    private ImageView genderImage;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_map_layout, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        eventText = view.findViewById(R.id.mapText);
        genderImage = view.findViewById(R.id.mapImage);
        setHasOptionsMenu(true);
        return view;
    }

    public void drawLines(Event event, Person person) {
        boolean enterSpouse = true;

        PolylineOptions line = new PolylineOptions();

        if (person.getSpouseId().isEmpty()) {
            enterSpouse = false;
        }
        if (User.filterMale || User.filterFemale) {
            enterSpouse = false;
        }
        if (User.fatherSide.contains(person.getPersonId()) && User.filterFather) {
            enterSpouse = false;
        }
        if (User.motherSide.contains(person.getPersonId()) && User.filterMother) {
            enterSpouse = false;
        }
        if (User.showSpouseLines) {
            enterSpouse = false;
        }

        if (enterSpouse) {

            ArrayList<Double> personLatLong = new ArrayList<>();

            if (!User.eventFilterMap.get(event.getEventType().toLowerCase())) {
                personLatLong.add(event.getLatitude());
                personLatLong.add(event.getLongitude());
            }

            ArrayList<Event> events = new ArrayList<>();

            for (Event eventSpouse : User.eventsMap.values()) {

                if (eventSpouse.getPersonID().equals(person.getSpouseId())) {

                    if (!User.eventFilterMap.get(eventSpouse.getEventType().toLowerCase())) {
                        events.add(eventSpouse);
                    }
                }
            }

            Comparator<Event> compareByYear = (Event o1, Event o2) -> Integer.toString(o1.getYear()).
                    compareTo(Integer.toString(o2.getYear()));

            Collections.sort(events, compareByYear);

            Event earliestEvent = null;

            if (personLatLong.size() > 0 && events.size() > 0) {

                earliestEvent = events.get(0);

                line.add(new LatLng(personLatLong.get(0), personLatLong.get(1)),
                        new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude()))
                        .width(10)
                        .color(User.spouseLine);

                googleMap.addPolyline(line);
            }
            personLatLong.clear();
            events.clear();

        }

        boolean shouldEnterFamily = true;
        line = new PolylineOptions();

        if (User.showLifeStoryLines) {
            shouldEnterFamily = false;
        }

        if (shouldEnterFamily) {

            ArrayList<Event> unfilteredEvents = new ArrayList<>();

            for (Event eventFamily : User.eventsMap.values()) {
                boolean addLifeLine = true;
                if (eventFamily.getPersonID().equals(person.getPersonId())) {

                    if (User.eventFilterMap.get(eventFamily.getEventType().toLowerCase())) {
                        addLifeLine = false;
                    }
                    if(User.fatherSide.contains(eventFamily.getPersonID()) && User.filterFather) {
                        addLifeLine= false;
                    }
                    if (User.motherSide.contains(eventFamily.getPersonID()) && User.filterMother) {
                        addLifeLine = false;
                    }
                    if (addLifeLine) {
                        unfilteredEvents.add(eventFamily);
                    }
                }
            }

            int widthSize = 10;

            Comparator<Event> compareByYear = (Event o1, Event o2) -> Integer.toString(o1.getYear()).
                    compareTo(Integer.toString(o2.getYear()));

            Collections.sort(unfilteredEvents, compareByYear);

            for (int i = 0; i < unfilteredEvents.size() - 1; i++) {
                Event event1 = unfilteredEvents.get(i);
                Event event2 = unfilteredEvents.get(i + 1);

                line.add(new LatLng(event1.getLatitude(), event1.getLongitude()),
                        new LatLng(event2.getLatitude(), event2.getLongitude()))
                        .width(widthSize)
                        .color(User.lifeStoryLine);
                widthSize -= 1;

                googleMap.addPolyline(line);
            }
            unfilteredEvents.clear();

        }

        if (!User.showFamilyLines) {

            drawFamilyLines(event, User.familyWidthValue);

        }

    }

    public void drawFamilyLines(Event event, int widthValue) {

        Person person = User.peopleMap.get(event.getPersonID());
        Person currentMother = null;
        Person currentFather = null;

        if (!User.filterMother && !User.filterFemale) {
            currentMother = User.peopleMap.get(person.getMotherId());
        }

        if (!User.filterFather && !User.filterMale) {
            currentFather = User.peopleMap.get(person.getFatherId());
        }

        Log.e("HTTP", event.getEventType().toLowerCase());

        if (currentMother != null) {

            Log.e("HTTP", currentMother.getFirstName());

            ArrayList<Event> unfilteredEvents = new ArrayList<>();

            for (Event eventFamily : User.eventsMap.values()) {

                if (eventFamily.getPersonID().equals(currentMother.getPersonId())) {

                    if (!User.eventFilterMap.get(eventFamily.getEventType().toLowerCase())
                            && ((User.fatherSide.contains(currentMother.getPersonId()) && !User.filterFather) ||
                            (User.motherSide.contains(currentMother.getPersonId()) && !User.filterMother))) {
                        unfilteredEvents.add(eventFamily);
                    }

                }
            }

            Comparator<Event> compareByYear = (Event o1, Event o2) -> Integer.toString(o1.getYear()).
                    compareTo(Integer.toString(o2.getYear()));

            Collections.sort(unfilteredEvents, compareByYear);

            PolylineOptions line = new PolylineOptions();

            if( unfilteredEvents.size() > 0) {
                Log.e("HTTP", unfilteredEvents.get(0).getEventType().toLowerCase());

                line.add(new LatLng(event.getLatitude(), event.getLongitude()),
                        new LatLng(unfilteredEvents.get(0).getLatitude(), unfilteredEvents.get(0).getLongitude()))
                        .width(widthValue)
                        .color(User.familyLine);

                googleMap.addPolyline(line);

                drawFamilyLines(unfilteredEvents.get(0), widthValue - 4);
            }
        }

        if (currentFather != null) {

            Log.e("HTTP", currentFather.getFirstName());

            ArrayList<Event> unfilteredEvents = new ArrayList<>();

            for (Event eventFamily : User.eventsMap.values()) {

                if (eventFamily.getPersonID().equals(currentFather.getPersonId())) {

                    if (!User.eventFilterMap.get(eventFamily.getEventType().toLowerCase())
                            && ((User.fatherSide.contains(currentFather.getPersonId()) && !User.filterFather) ||
                            (User.motherSide.contains(currentFather.getPersonId()) && !User.filterMother))) {
                        unfilteredEvents.add(eventFamily);
                    }
                }
            }

            Comparator<Event> compareByYear = (Event o1, Event o2) -> Integer.toString(o1.getYear()).
                    compareTo(Integer.toString(o2.getYear()));

            Collections.sort(unfilteredEvents, compareByYear);

            PolylineOptions line = new PolylineOptions();

            line.add(new LatLng(event.getLatitude(), event.getLongitude()),
                    new LatLng(unfilteredEvents.get(0).getLatitude(), unfilteredEvents.get(0).getLongitude()))
                    .width(widthValue)
                    .color(User.familyLine);

            googleMap.addPolyline(line);

            drawFamilyLines(unfilteredEvents.get(0), widthValue - 4);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (User.selectedEventID.isEmpty()) {

            inflater.inflate(R.menu.options_menu, menu);
            MenuItem settingsButton = menu.findItem(R.id.settingsButton);
            MenuItem searchButton = menu.findItem(R.id.searchButton);
            MenuItem filterButton = menu.findItem(R.id.filterButton);

            settingsButton.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_cog).color(Color.DKGRAY).sizeDp(40));
            searchButton.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search).color(Color.DKGRAY).sizeDp(40));
            filterButton.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_filter).color(Color.DKGRAY).sizeDp(40));

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        Intent intent;

        if (menuItem.getItemId() == R.id.searchButton) {

            intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.settingsButton) {

            intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.filterButton) {

            intent = new Intent(getActivity().getApplicationContext(), FilterActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == android.R.id.home) {
            intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            User.selectedEventID = "";
            startActivity(intent);
            return true;
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap_) {
        googleMap = googleMap_;
        googleMap.setMapType(User.mapType);
        checkEventID();
        drawMarkers();
        setListeners();

    }

    private void setListeners() {
        genderImage.setOnClickListener(v -> {
            if (User.selectable) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("personID", personId_for_service);
                startActivity(intent);
            }
        });

        eventText.setOnClickListener(v -> {
            if (User.selectable) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("personID", personId_for_service);
                startActivity(intent);
            }
        });

        googleMap.setOnMarkerClickListener(marker -> {

            googleMap.clear();

            drawMarkers();

            String eventId = marker.getSnippet();

            User.selectable = true;

            Event event = User.eventsMap.get(eventId);
            Person person = User.peopleMap.get(event.getPersonID());

            Log.e("HTTP", event.getEventType().toLowerCase());
            Log.e("HTTP", person.getFirstName());


            Drawable genderIcon;

            if (person.getGender().equals("m")) {
                genderIcon = new IconDrawable(getActivity(),
                        FontAwesomeIcons.fa_male).color(Color.BLUE).sizeDp(40);
            } else {
                genderIcon = new IconDrawable(getActivity(),
                        FontAwesomeIcons.fa_female).color(Color.RED).sizeDp(40);
            }

            genderImage.setImageDrawable(genderIcon);

            String eventTextString = person.getFirstName() + " " + person.getLastName() +
                    System.getProperty("line.separator") + event.getEventType() + ": " + event.getCity() + ", "
                    + event.getCountry() + " (" + event.getYear() + ")";

            eventText.setText(eventTextString);

            personId_for_service = event.getPersonID();

            drawLines(event, person);

            return false;
        });
    }

    private void drawMarkers() {
        for (Event event1 : User.eventsMap.values()) {
            LatLng position = new LatLng(event1.getLatitude(), event1.getLongitude());
            Person person1 = User.peopleMap.get(event1.getPersonID());

            boolean shouldEnter = true;

            assert person1 != null;
            if (person1.getGender().equals("f")) {
                if (User.filterFemale) {
                    shouldEnter = false;
                }
            }
            if (person1.getGender().equals("m")) {
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

            if (shouldEnter && !User.eventFilterMap.get(event1.getEventType().toLowerCase())) {
                Float colorInt = User.eventMarkerColor.get(event1.getEventType().toLowerCase());
                googleMap.addMarker(new MarkerOptions().
                        position(position).
                        snippet(event1.getEventID()).
                        title(event1.getEventType()).
                        icon(BitmapDescriptorFactory.defaultMarker(colorInt)));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkEventID() {
        if(!User.selectedEventID.isEmpty()) {
            Event event = User.eventsMap.get(User.selectedEventID);
            //LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
            //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 20.0f);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.getLatitude(), event.getLongitude()), 4.0f));

            Person person = User.peopleMap.get(event.getPersonID());

            Drawable genderIcon;

            if (person.getGender().equals("m")) {
                genderIcon = new IconDrawable(getActivity(),
                        FontAwesomeIcons.fa_male).color(Color.BLUE).sizeDp(40);
            }
            else {
                genderIcon = new IconDrawable(getActivity(),
                        FontAwesomeIcons.fa_female).color(Color.RED).sizeDp(40);
            }

            genderImage.setImageDrawable(genderIcon);

            String eventTextString = person.getFirstName() + " " + person.getLastName() +
                    System.getProperty("line.separator") + event.getEventType() + ": " + event.getCity() + ", "
                    + event.getCountry() + " (" + event.getYear() + ")";

            eventText.setText(eventTextString);
            personId_for_service = event.getPersonID();
            User.selectable = true;

            drawLines(event, person);
            drawFamilyLines(event, 20);

        } else {

            Drawable genderIcon = new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_android).color(Color.GREEN).sizeDp(40);

            genderImage.setImageDrawable(genderIcon);
            eventText.setText("Click on a map marker to view details of an event");
            User.selectable = false;
        }
    }

}
