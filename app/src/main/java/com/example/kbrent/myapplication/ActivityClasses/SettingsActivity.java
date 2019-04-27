package com.example.kbrent.myapplication.ActivityClasses;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kbrent.myapplication.ContainerClasses.User;
import com.example.kbrent.myapplication.HTTP.HttpClient;
import com.example.kbrent.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);

        TextView logoutView = findViewById(R.id.logout);
        TextView resyncView = findViewById(R.id.resync);

        Switch lSwitch = findViewById(R.id.lifeLineSwitch);
        Switch fSwitch = findViewById(R.id.familySwitch);
        Switch sSwitch = findViewById(R.id.spouseSwitch);

        lSwitch.setChecked(User.showLifeStoryLines);
        fSwitch.setChecked(User.showFamilyLines);
        sSwitch.setChecked(User.showSpouseLines);

        lSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> User.showLifeStoryLines = isChecked);

        fSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> User.showFamilyLines = isChecked);

        sSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> User.showSpouseLines = isChecked);

        logoutView.setOnClickListener(view -> {
            User.filterFemale = false;
            User.filterMale = false;
            User.filterFather = false;
            User.filterMother = false;
            User.eventFilterMap.replaceAll((k, v) -> false);
            User.showSpouseLines = false;
            User.showLifeStoryLines = false;
            User.showFamilyLines = false;
            User.mapType = GoogleMap.MAP_TYPE_NORMAL;
            User.spouseLine = Color.BLUE;
            User.familyLine = Color.RED;
            User.lifeStoryLine = Color.GREEN;
            User.userName = "";
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });

        resyncView.setOnClickListener(view -> {

            registerHttpPost post = new registerHttpPost();

            User.peopleMap.clear();
            User.eventsMap.clear();

            post.execute();

            Intent intent = new Intent(this, MainActivity.class);

            User.selectedEventID = "";
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);

            startActivity(intent);
        });

        HashMap<String, Integer> colorMap = new HashMap<>();
        HashMap<String, Integer> typeMap = new HashMap<>();
        HashMap<Integer, String> stringColorMap = new HashMap<>();
        HashMap<Integer, String> stringTypeMap = new HashMap<>();

        colorMap.put("Green", Color.GREEN);
        colorMap.put("Red", Color.RED);
        colorMap.put("Blue", Color.BLUE);

        stringColorMap.put(Color.GREEN, "Green");
        stringColorMap.put(Color.RED, "Red");
        stringColorMap.put(Color.BLUE, "Blue");

        typeMap.put("Normal", GoogleMap.MAP_TYPE_NORMAL);
        typeMap.put("Hybrid", GoogleMap.MAP_TYPE_HYBRID);
        typeMap.put("Satellite", GoogleMap.MAP_TYPE_SATELLITE);
        typeMap.put("Terrain", GoogleMap.MAP_TYPE_TERRAIN);

        stringTypeMap.put(GoogleMap.MAP_TYPE_NORMAL, "Normal");
        stringTypeMap.put(GoogleMap.MAP_TYPE_HYBRID, "Hybrid");
        stringTypeMap.put(GoogleMap.MAP_TYPE_SATELLITE, "Satellite");
        stringTypeMap.put(GoogleMap.MAP_TYPE_TERRAIN, "Terrain");

        Spinner spouseSpinner = findViewById(R.id.spouseColor);
        Spinner familySpinner = findViewById(R.id.familyColor);
        Spinner lifeSpinner = findViewById(R.id.lifeLineColor);
        Spinner mapSpinner = findViewById(R.id.mapType);

        ArrayList<String> arrarOfColors = new ArrayList<>();
        arrarOfColors.add("Red");
        arrarOfColors.add("Green");
        arrarOfColors.add("Blue");

        ArrayList<String> mapTypes = new ArrayList<>();
        mapTypes.add("Normal");
        mapTypes.add("Hybrid");
        mapTypes.add("Satellite");
        mapTypes.add("Terrain");

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrarOfColors);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spouseSpinner.setAdapter(colorAdapter);
        familySpinner.setAdapter(colorAdapter);
        lifeSpinner.setAdapter(colorAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mapTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(typeAdapter);

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                User.mapType = (typeMap.get(parent.getItemAtPosition(pos).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        familySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                User.familyLine = colorMap.get(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                User.spouseLine = colorMap.get(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        lifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                User.lifeStoryLine = colorMap.get(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mapSpinner.setSelection(typeAdapter.getPosition(stringTypeMap.get(User.mapType)));
        lifeSpinner.setSelection(colorAdapter.getPosition(stringColorMap.get(User.lifeStoryLine)));
        familySpinner.setSelection(colorAdapter.getPosition(stringColorMap.get(User.familyLine)));
        spouseSpinner.setSelection(colorAdapter.getPosition(stringColorMap.get(User.spouseLine)));

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

    public class registerHttpPost extends AsyncTask<URL, String, String> {
        registerHttpPost() {}

        @Override
        public String doInBackground(URL... urls) {

            String response = "";

            String serverPath;

            User.eventsMap.clear();
            User.eventFilterMap.clear();
            User.eventMarkerColor.clear();
            User.eventTypeTree.clear();

            URL url;
            User user = new User();

            JSONObject body = new JSONObject();

            try {
                body.put("userName", User.userName);
                body.put("password", User.userPass);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                serverPath = "user/login";
                url = new URL("http://" + User.serverHost + ":" + User.serverPort + "/" + serverPath);
                response = HttpClient.postUrl(url, body);

                if (!response.contains("message")) {
                    JsonParser parser = new JsonParser();
                    JsonObject rootObj = parser.parse(response).getAsJsonObject();
                    User.PersonID = rootObj.get("personID").getAsString();
                    User.authToken = rootObj.get("authToken").getAsString();
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Add events
            try {
                serverPath = "event";
                url = new URL("http://" + User.serverHost + ":" + User.serverPort + "/" + serverPath);
                String responseEvent = HttpClient.getUrl(url, User.authToken);
                user.addEvents(responseEvent);
                Log.e("HTTP", responseEvent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Add people
            try {
                serverPath = "person";
                url = new URL("http://" + User.serverHost + ":" + User.serverPort + "/" + serverPath);
                String responsePerson = HttpClient.getUrl(url, User.authToken);
                user.addPeople(responsePerson);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return response;

        }

        public void onPostExecute(String response) {
            if (response.contains("user does not exist") || response.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}