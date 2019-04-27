package kbrent.FMSDesign.Server;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Result.Response;
import kbrent.FMSDesign.Service.EventService;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * Routes to the EventService
 */
public class EventHandler implements HttpHandler {

    private Response response = new Response();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers reqHeaders = httpExchange.getRequestHeaders();
        List<String> authToken = reqHeaders.get("Authorization");

        URI uri = httpExchange.getRequestURI();
        String[] parameters = uri.toString().split("/");

        EventService eventService = new EventService();
        JsonObject jo = new JsonObject();

        int headerVal;

        if(parameters.length == 3) {
            System.out.println("entered this path: " + uri.toString());
            String eventID = parameters[2];

            Event event = eventService.getEventByID(eventID, authToken.get(0));
            if(authToken.get(0).isEmpty()) {
                jo.addProperty("message", "token is empty");
                headerVal = 400;
            }
            else if(event == null) {
                jo.addProperty("message", "invalid eventID or token was invalid");
                headerVal = 400;
            }
            else {
                headerVal = 200;
                jo.addProperty("descendant", event.getUsername());
                jo.addProperty("eventID", event.getEventID());
                jo.addProperty("personID", event.getPersonID());
                jo.addProperty("latitude", event.getLatitude());
                jo.addProperty("longitude", event.getLongitude());
                jo.addProperty("country", event.getCountry());
                jo.addProperty("city", event.getCity());
                jo.addProperty("eventType", event.getEventType());
                jo.addProperty("year", event.getYear());
            }
            response.sendMessage(jo, httpExchange, headerVal);
        }
        else {
            System.out.println("entered this path: " + uri.toString());
            JsonArray ja;
            new JsonArray();
            if(authToken.get(0).isEmpty()) {
                jo.addProperty("message", "token is empty");
                headerVal = 400;
            }
            else {
                ja = eventService.getAllEventsByAccessToken(authToken.get(0));
                if (ja != null) {
                    jo.add("data", ja);
                    headerVal = 200;
                }
                else {
                    jo.addProperty("message", "invalid token");
                    headerVal = 400;
                }
            }
            response.sendMessage(jo, httpExchange, headerVal);
        }

    }
}
