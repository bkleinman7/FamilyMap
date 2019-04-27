package kbrent.FMSDesign.Server;

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

import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Result.Response;
import kbrent.FMSDesign.Service.PersonService;

/*
 * Created by kbrent on 02/09/19.
 */

public class PersonHandler implements HttpHandler {

    private Response response = new Response();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers reqHeaders = httpExchange.getRequestHeaders();
        List<String> authToken = reqHeaders.get("Authorization");

        URI uri = httpExchange.getRequestURI();
        String[] parameters = uri.toString().split("/");
        System.out.println(parameters.length);
        JsonObject jo = new JsonObject();
        PersonService personService = new PersonService();

        int headerVal;

        if(parameters.length == 3) {

            String personID = parameters[2];
            System.out.println(personID);
            Person person = personService.getPersonByID(personID, authToken.get(0));
            if(authToken.get(0).isEmpty()) {
                jo.addProperty("message", "token is empty");
                headerVal = 400;
            }
            else if(person == null) {
                jo.addProperty("message", "invalid personID or token is invalid");
                headerVal = 400;
            }
            else {

                headerVal = 200;
                jo.addProperty("descendant", person.getDescendant());
                jo.addProperty("personID", person.getPersonId());
                jo.addProperty("firstName", person.getFirstName());
                jo.addProperty("lastName", person.getLastName());
                jo.addProperty("gender", person.getGender());
                jo.addProperty("father", person.getFatherId());
                jo.addProperty("mother", person.getMotherId());
                jo.addProperty("spouse", person.getSpouseId());


            }
            response.sendMessage(jo, httpExchange, headerVal);
        }
        else {
            System.out.println("entered this path: " + uri.toString());
            new JsonArray();
            JsonArray ja;
            if(authToken.get(0).isEmpty()) {
                jo.addProperty("message", "token is empty");
                headerVal = 400;
            }
            else {
                ja = personService.getUserNamesFamily(authToken.get(0));
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
