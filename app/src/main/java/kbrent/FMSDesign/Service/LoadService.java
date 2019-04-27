package kbrent.FMSDesign.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.util.UUID;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.DataAccess.PersonDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * This service deletes all the info in the db and parses through a json and loads in new info
 */
public class LoadService {

    public LoadService(){ }

    private DataBase dataBase = new DataBase();

    /**
     * Load in json file body for /load API call
     * @param json Parsed and then passed in
     * @return int array for how many users, people, events were added
     * @throws DataAccessException Bad Practice...
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer[] loadJson(String json) throws DataAccessException {
        Integer[] intArr = new Integer[3];
        int numUsers, numPersons, numEvents;
        numUsers = numPersons = numEvents = 0;
        boolean addObject = true;

        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        System.out.println("Gathering user array");
        JsonArray users = jsonObject.getAsJsonArray("users");
        System.out.println("Gathering persons array");
        JsonArray persons = jsonObject.getAsJsonArray("persons");
        System.out.println("Gathering events array");
        JsonArray events = jsonObject.getAsJsonArray("events");

        System.out.println("Parsing through user array");
        Connection conn;
        for (JsonElement obj : users) {
            JsonObject userObj = obj.getAsJsonObject();
            String userName, password, email, firstName, lastName, uniqueToken, uniquePersonID, gender, personID;
            userName = password = email = firstName = lastName = gender = personID = "";
            if (userObj.has("userName") && !userObj.isJsonNull()) {
                userName = userObj.get("userName").getAsString();
            }

            if (userObj.has("personID") && !userObj.isJsonNull()) {
                personID = userObj.get("personID").getAsString();
            }

            if (userObj.has("password") && !userObj.isJsonNull()) {
                password = userObj.get("password").getAsString();
            }

            if (userObj.has("email") && !userObj.isJsonNull()) {
                email = userObj.get("email").getAsString();
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

            //System.out.println(userName + " " + password + " " + email + " " + firstName + " " + lastName + " " + gender);

            if(userName.equals("") || password.equals("") || email.equals("") ||
                    firstName.equals("") || lastName.equals("") || gender.equals("") ||
                    (!gender.equals("f") && !gender.equals("m"))) {
                addObject = false;
            }

            uniqueToken = UUID.randomUUID().toString();
            uniquePersonID = UUID.randomUUID().toString();

            if(addObject) {
                User user = new User(userName, password, email, firstName, lastName, uniqueToken, personID, gender);
                conn = dataBase.openConnection();
                UserDao userDao = new UserDao(conn);
                TokenDao tokenDao = new TokenDao(conn);
                Token token = new Token(uniqueToken, user.getUserName());
                tokenDao.insert(token);
                boolean commit = userDao.insert(user);
                dataBase.closeConnection(commit);
                if (commit) {
                    numUsers++;
                }


            }
            addObject = true;
        }

        System.out.println("Parsing through persons array");
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

            if(addObject) {
                Person person = new Person(descendant, personID, firstName, lastName, gender, father, mother, spouse);
                conn = dataBase.openConnection();
                PersonDao personDao = new PersonDao(conn);
                boolean commit = personDao.insert(person);
                dataBase.closeConnection(commit);
                if (commit) {
                    numPersons++;
                }
            }
            addObject = true;
        }

        System.out.println("Parsing through event array");
        for (JsonElement obj : events) {
            JsonObject userObj = obj.getAsJsonObject();
            String descendant, eventID, personID, country, city, eventType;
            Float latitude = (float) 0;
            Float longitude = (float) 0;
            int year = 0;
            descendant= eventID= personID= country= city= eventType= "";

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

            if(descendant.equals("") || personID.equals("") || eventID.equals("") || country.equals("") ||
                    city.equals("") || eventType.equals("") || latitude.equals((float) 0)
                    || longitude.equals((float) 0) || year == 0) {
                addObject = false;
            }

            if(addObject) {
                Event event = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
                conn = dataBase.openConnection();
                EventDao eventDao = new EventDao(conn);
                boolean commit = eventDao.insert(event);
                dataBase.closeConnection(commit);
                if (commit) {
                    numEvents++;
                }
            }
            addObject = true;
        }
        System.out.println("Assigning variables");
        intArr[0] = numUsers;
        intArr[1] = numPersons;
        intArr[2] = numEvents;
        System.out.println("Finished...");
        return intArr;
    }

}
