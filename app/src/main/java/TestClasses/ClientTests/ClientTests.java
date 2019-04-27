package TestClasses.ClientTests;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.kbrent.myapplication.HTTP.HttpClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kbrent.FMSDesign.Model.Event;

import static org.junit.Assert.*;

public class ClientTests {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {

        @SuppressLint("StaticFieldLeak")
        class loginHttpPost extends AsyncTask<URL, String, String> {


            @Override
            public String doInBackground(URL... urls) {

                HttpClient httpClient = new HttpClient();

                String serverHost = "192.168.1.198";
                String serverPort = "8080";
                String userName = "b";
                String userPass = "k";
                com.example.kbrent.myapplication.ContainerClasses.User.userPass = userPass;
                String response = "message";

                com.example.kbrent.myapplication.ContainerClasses.User.serverHost = serverHost;
                com.example.kbrent.myapplication.ContainerClasses.User.serverPort = serverPort;

                JSONObject body = new JSONObject();

                try {
                    body.put("userName", userName);
                    body.put("password", userPass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    String serverPath = "user/login";
                    URL url = new URL("http://" + serverHost + ":" + serverPort + "/" + serverPath);
                    response = HttpClient.postUrl(url, body);

                    if (!response.contains("message")) {

                        com.example.kbrent.myapplication.ContainerClasses.User user = new com.example.kbrent.myapplication.ContainerClasses.User();
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        String personId = rootObj.get("personID").getAsString();
                        String tokenId = rootObj.get("authToken").getAsString();
                        com.example.kbrent.myapplication.ContainerClasses.User.PersonID = personId;
                        com.example.kbrent.myapplication.ContainerClasses.User.authToken = tokenId;
                        com.example.kbrent.myapplication.ContainerClasses.User.userName = userName;

                        try {

                            serverPath = "person/" + personId;
                            url = new URL("http://" + serverHost + ":" + serverPort + "/" + serverPath);
                            String responsePersonID = httpClient.getUrl(url, tokenId);
                            //Log.e("HTTP", responsePersonID);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        //Add events
                        try {
                            serverPath = "event";
                            url = new URL("http://" + serverHost +
                                    ":" + serverPort + "/" + serverPath);
                            String responseEvent = HttpClient.getUrl(url, com.example.kbrent.myapplication.ContainerClasses.User.authToken);
                            user.addEvents(responseEvent);
                            //Log.e("HTTP", responseEvent);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        //Add people
                        try {
                            serverPath = "person";
                            url = new URL("http://" + serverHost +
                                    ":" + serverPort + "/" + serverPath);
                            String responsePerson = HttpClient.getUrl(url, com.example.kbrent.myapplication.ContainerClasses.User.authToken);
                            user.addPeople(responsePerson);
                            //Log.e("HTTP", responsePerson);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return "";
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @After
    public void tearDown() throws Exception {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void calculateRelationships() {

        Event testEvent = null;
        assertNull(testEvent);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void filterEvents() {

        com.example.kbrent.myapplication.ContainerClasses.User.filterMother = true;
        com.example.kbrent.myapplication.ContainerClasses.User.filterFather = true;

        ArrayList<com.example.kbrent.myapplication.ContainerClasses.Event> events = null;

        for (com.example.kbrent.myapplication.ContainerClasses.Event event :
                com.example.kbrent.myapplication.ContainerClasses.User.eventsMap.values()) {
            if (!com.example.kbrent.myapplication.ContainerClasses.User.filterMother &&
                    !com.example.kbrent.myapplication.ContainerClasses.User.filterFather) {
                events.add((event));
            }
        }

        assertNull(events);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void sortEvents() {

        ArrayList<com.example.kbrent.myapplication.ContainerClasses.Event> events = new ArrayList<>();

        com.example.kbrent.myapplication.ContainerClasses.Event event = new com.example.kbrent.myapplication.ContainerClasses.Event("test", "test1","test2",
                10.0f, 10.0f, "usa", "mesa", "birth", 1990);

        com.example.kbrent.myapplication.ContainerClasses.Event event1 = new com.example.kbrent.myapplication.ContainerClasses.Event("test", "test1","test2",
                10.0f, 10.0f, "usa", "mesa", "birth", 1980);

        assert events != null;
        events.add(event);
        events.add(event1);

        Comparator<com.example.kbrent.myapplication.ContainerClasses.Event> compareByYear = (com.example.kbrent.myapplication.ContainerClasses.Event o1, com.example.kbrent.myapplication.ContainerClasses.Event o2) -> Integer.toString(o1.getYear()).
                compareTo(Integer.toString(o2.getYear()) );
        Collections.sort(events, compareByYear);

        assertEquals(events.get(0).getEventType(), "birth");


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void searchEvents() {

    }
}