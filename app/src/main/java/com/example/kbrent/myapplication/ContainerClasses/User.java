package com.example.kbrent.myapplication.ContainerClasses;

/*
 * Created by kbrent on 02/09/19.
 */

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * stores info for a given user w/ Their events and people they are assoiciated with
 */
@SuppressLint("Registered")
public class User extends Application {

    public static volatile String authToken = "";
    public static volatile String userName = "";
    public static volatile String userPass = "";
    public static volatile String serverHost = "";
    public static volatile String serverPort = "";
    public static volatile String PersonID = "";
    public static volatile String tempPersonID = "";
    public static volatile String selectedEventID = "";
    public static volatile boolean selectable = false;

    //For settings activity
    public static volatile int spouseLine = Color.BLUE;
    public static volatile int familyLine = Color.RED;
    public static volatile int lifeStoryLine = Color.GREEN;
    public static volatile int mapType = GoogleMap.MAP_TYPE_NORMAL;
    public static volatile boolean showSpouseLines = false;
    public static volatile boolean showFamilyLines = false;
    public static volatile boolean showLifeStoryLines = false;
    public static volatile int  familyWidthValue = 20;

    //For filter activity
    public static volatile boolean filterMother = false;
    public static volatile boolean filterFather = false;
    public static volatile boolean filterMale = false;
    public static volatile boolean filterFemale = false;

    public static volatile HashMap<String, Person> peopleMap = new HashMap<>();
    public static volatile HashMap<String, Event> eventsMap = new HashMap<>();
    public static volatile ArrayList<String> fatherSide = new ArrayList<>();
    public static volatile ArrayList<String> motherSide = new ArrayList<>();
    public static volatile TreeSet<String> eventTypeTree = new TreeSet<>();
    public static volatile HashMap<String, Boolean> eventFilterMap = new HashMap<>();
    public static volatile HashMap<String, Float> eventMarkerColor = new HashMap<>();

    public void addEvents(String response) {

        JsonElement jsonElement = new JsonParser().parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        System.out.println("Gathering event array");
        JsonArray events = jsonObject.getAsJsonArray("data");

        for (JsonElement obj : events) {
            JsonObject userObj = obj.getAsJsonObject();
            String descendant, eventID, personID, country, city, eventType;
            Float latitude = (float) 0;
            Float longitude = (float) 0;
            boolean addObject = true;
            int year = 0;
            descendant = eventID = personID = country = city = eventType = "";

            if (userObj.has("descendant") && !userObj.isJsonNull()) {
                descendant = userObj.get("descendant").getAsString();
            }

            if (userObj.has("personID") && !userObj.isJsonNull()) {
                personID = userObj.get("personID").getAsString();
            }

            if (userObj.has("eventID") && !userObj.isJsonNull()) {
                eventID = userObj.get("eventID").getAsString();
            }

            if (userObj.has("latitude") && !userObj.isJsonNull()) {
                latitude = userObj.get("latitude").getAsFloat();
            }

            if (userObj.has("longitude") && !userObj.isJsonNull()) {
                longitude = userObj.get("longitude").getAsFloat();
            }

            if (userObj.has("country") && !userObj.isJsonNull()) {
                country = userObj.get("country").getAsString();
            }

            if (userObj.has("city") && !userObj.isJsonNull()) {
                city = userObj.get("city").getAsString();
            }

            if (userObj.has("eventType") && !userObj.isJsonNull()) {
                eventType = userObj.get("eventType").getAsString();
            }

            if (userObj.has("year") && !userObj.isJsonNull()) {
                year = userObj.get("year").getAsInt();
            }

            if (descendant.equals("") || personID.equals("") || eventID.equals("") || country.equals("") ||
                    city.equals("") || eventType.equals("") || latitude.equals((float) 0)
                    || longitude.equals((float) 0) || year == 0) {
                addObject = false;
            }

            if (addObject) {
                eventTypeTree.add(eventType.toLowerCase());
                if(!eventsMap.containsValue(eventType)) {
                    Event event = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
                    eventsMap.put(eventID, event);
                }

            }
        }

        // 0 - 360
        //  15

        Float[] colorArray = {BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE,
                BitmapDescriptorFactory.HUE_CYAN,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_MAGENTA,
                BitmapDescriptorFactory.HUE_RED,
                BitmapDescriptorFactory.HUE_ROSE,
                BitmapDescriptorFactory.HUE_VIOLET,
                BitmapDescriptorFactory.HUE_YELLOW, 40.0f, 70.0f, 90.0f, 110.0f, 130.0f, 150.0f, 170.0f, 190.0f};
        Integer colorInt = 0;
        for(String s : eventTypeTree) {
            eventFilterMap.put(s.toLowerCase(), false);
            eventMarkerColor.put(s.toLowerCase(), colorArray[colorInt]);
            colorInt++;
        }


    }

    public void addPeople(String response) {

        JsonElement jsonElement = new JsonParser().parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        System.out.println("Gathering person array");
        JsonArray persons = jsonObject.getAsJsonArray("data");
        boolean addObject = true;

        for (JsonElement obj : persons) {
            JsonObject userObj = obj.getAsJsonObject();
            String descendant, personID, firstName, lastName, gender, father, mother, spouse;
            descendant= personID = firstName= lastName= gender= father= mother= spouse = "";

            if (userObj.has("descendant") && !jsonObject.isJsonNull()) {
                descendant = userObj.get("descendant").getAsString();
            }

            if (userObj.has("personID") && !jsonObject.isJsonNull()) {
                personID = userObj.get("personID").getAsString();
            }

            if (userObj.has("firstName") && !userObj.isJsonNull()) {
                firstName = userObj.get("firstName").getAsString();
            }

            if (userObj.has("lastName") && !userObj.isJsonNull()) {
                lastName = userObj.get("lastName").getAsString();
            }

            if (userObj.has("gender") && !userObj.isJsonNull()) {
                gender = userObj.get("gender").getAsString();
            }

            if (userObj.has("father") && !userObj.isJsonNull()) {
                father = userObj.get("father").getAsString();
            }

            if (userObj.has("mother") && !userObj.isJsonNull()) {
                mother = userObj.get("mother").getAsString();
            }

            if (userObj.has("spouse") && !userObj.isJsonNull()) {
                spouse = userObj.get("spouse").getAsString();
            }

            if(descendant.equals("") || personID.equals("") || firstName.equals("") ||
                    lastName.equals("") || gender.equals("")) {
                addObject = false;
            }

            if(personID.equals(PersonID)) {
                fatherSide.add(father);
                motherSide.add(mother);
            }

            if (!personID.equals(PersonID)) {
                if (fatherSide.contains(personID)) {
                    fatherSide.add(father);
                    fatherSide.add(mother);
                }

                if (motherSide.contains(personID)) {
                    motherSide.add(father);
                    motherSide.add(mother);
                }
            }

            if(addObject) {
                Person person = new Person(descendant, personID, firstName, lastName, gender, father, mother, spouse);
                peopleMap.put(personID, person);
            }
        }



    }

}
