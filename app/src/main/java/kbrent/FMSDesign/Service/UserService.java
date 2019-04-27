package kbrent.FMSDesign.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.DataAccess.PersonDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Request.LoginRequest;
import kbrent.FMSDesign.Request.RegisterRequest;

/*
  Created by kbrent on 2/09/19.
 */

/**
 * This is to handle the business logic from the userHandler
 * Will load in the generations from the register API call and
 * Will also check to see if a user exists with a username and username w/ password
 */
public class UserService {

    private Connection conn;
    private DataBase dataBase = new DataBase();
    private UserDao userDao;
    private EventDao eventDao;
    private PersonDao personDao;
    private User user;

    private Event newEvent;
    private Person newPerson;


    public UserService() {
    }

    /**
     * Query based on userName
     *
     * @param userName use to return a user's info
     * @return user object
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User getUserWithUserName(String userName) throws DataAccessException {

        conn = dataBase.openConnection();
        user = null;

        userDao = new UserDao(conn);
        user = userDao.find(userName);
        dataBase.closeConnection(true);

        return user;

    }

    /**
     * This method logs the user in
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonObject login(HttpExchange httpExchange) throws IOException, DataAccessException {
        LoginRequest loginRequest = new LoginRequest();
        //Get JSON string
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder sb = new StringBuilder();
        while ((b = br.read()) != -1) {
            sb.append((char) b);
        }
        return (loginRequest.checkJson(sb.toString()));
    }

    /**
     * This method registers the user
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonObject register(HttpExchange httpExchange) throws IOException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest();
        //Get JSON string
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder sb = new StringBuilder();
        while ((b = br.read()) != -1) {
            sb.append((char) b);
        }
        return (registerRequest.checkJson(sb.toString()));
    }

    /**
     * Query based on authToken
     *
     * @param userName and password use to access the user
     * @return user object
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User getUserWithUsernamePassword(String userName, String password) throws DataAccessException {

        conn = dataBase.openConnection();
        userDao = new UserDao(conn);
        user = userDao.findLogin(userName, password);
        dataBase.closeConnection(true);
        return user;

    }

    /**
     * This is to load the 4 default generations
     *
     * @param userName
     * @param uniquePersonID
     * @param firstName
     * @param lastName
     * @param gender
     * @param gen
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadPersonsAndEvents(String userName, String uniquePersonID,
                                     String firstName, String lastName,
                                     String gender, int gen) throws DataAccessException {

        String[] fatherNames = {"Jolynn", "Maryland", "Marjorie", "Arletta", "Sanda", "Kyong", "Rosette",
                "Tera", "Spencer", "Jannette", "Gladys", "Nichole", "Terisa", "Tenesha", "Rebecca",
                "Alpha", "Elena", "Marsha", "Kelle", "Deedee", "Celsa", "Marget", "Anette", "Cicely"};

        String[] motherNames = {"Kevin", "Zack", "Ken", "Benjamin", "Alonzo",
                "Sam", "Stanley", "Kermit", "Augustine", "Silas", "Sol", "Franklyn", "Clement", "Ezra",
                "Lee", "Hal", "Bruce", "Clifford", "Wilbert", "Lonny", "Marco", "Vaughn", "Brandon",
                "Odell", "Rudolf", "Teodoro", "Rico", "Tyree", "Eliseo", "Brice", "Jacinto", "Hunter"};

        String[] lastNames = {"Torgeson", "Maston", "Lisenby", "Ocheltree", "Cadden", "Garden", "Delcastillo",
                "Mccawley", "Albertson", "Chiang", "Sydnor", "Mcnulty", "Bowdoin", "Quiroz", "Waechter", "Schaber",
                "Nalley", "Voris", "Kliebert", "Woosley", "Mandelbaum", "Lindahl", "Freitag", "Van", "Vinyard"};

        String[] eventType = {"Birth", "Baptism", "First Kiss", "Marriage", "Death"};

        String[] randomCity = {"Provo", "Mesa", "Salt Lake City"};

        Integer[] randomBirths = {1993};

        ArrayList<String> personID = new ArrayList<>();
        Map<String, String> spouseToSpouse = new HashMap<>();

        //Create all person IDs for specified generations
        for (int i = 0; i < (Math.pow(2, gen + 1) - 1); i++) {
            //Make sure user has parents..
            if(i == 0) {
                personID.add(UUID.randomUUID().toString());
                personID.add(UUID.randomUUID().toString());
                personID.add(UUID.randomUUID().toString());
            }
            //otherwise just add parents
            else {
                personID.add(UUID.randomUUID().toString());
                personID.add(UUID.randomUUID().toString());
            }
        }

        System.out.println(personID);

        //Map spouces
        for(int i = 1; i < personID.size() - 1; i++) {
            if((i % 2) != 0) {
                spouseToSpouse.put(personID.get(i), personID.get(i+1));
            } else {
                spouseToSpouse.put(personID.get(i), personID.get(i-1));
            }
        }


        try {
            conn = dataBase.openConnection();
            personDao = new PersonDao(conn);
            eventDao = new EventDao(conn);

            int maxGeneration = 0;
            int currGen = 0;

            for (int i = 0; i < (Math.pow(2, gen + 1) - 1); i++) {

                //Keep track of what generation I am on
                if(i > maxGeneration) {
                    currGen++;
                    maxGeneration = (i*2);
                }

                Random r = new Random();
                int randomNumberFather = r.nextInt(fatherNames.length);
                int randomNumberMother = r.nextInt(motherNames.length);

                //Currently these are the only events listed
                int birthYear = randomBirths[0];
                int baptismYear = birthYear + 8;
                int firstKissYear = baptismYear + 8;
                int marriageYear = firstKissYear + 8;
                int deathYear = marriageYear + 30;

                Integer[] yearArray = {birthYear, baptismYear, firstKissYear, marriageYear, deathYear};

                String motherName = motherNames[randomNumberMother];
                String fatherName = fatherNames[randomNumberFather];

                String child = personID.get(i);
                int parentFatherIndex = (2 * i) + 1;
                int parentMotherIndex = (2 * i) + 2;

                String parentFather = personID.get(parentFatherIndex);
                String parentMother = personID.get(parentMotherIndex);

                String genderPerson;
                String personName;

                //odd = male, even = female
                if((i % 2) != 0) {
                    personName = fatherName;
                    genderPerson = "m";
                } else {
                    personName = motherName;
                    genderPerson = "f";
                }

                if(i == 0) {
                    personName = firstName;
                }

                String spouse;
                if(i == 0) {
                    child = uniquePersonID;
                    spouse = "";
                    genderPerson = gender;
                } else {
                    spouse = spouseToSpouse.get(child);
                }

                newPerson = new Person(userName, child, personName, lastName,
                        genderPerson, parentFather,
                        parentMother, spouse);

                personDao.insert(newPerson);

                double latitude = ThreadLocalRandom.current().nextDouble(-90, 90 + 1);
                double longitude = ThreadLocalRandom.current().nextDouble(-180, 180 + 1);

                for (int j = 0; j < yearArray.length; j++) {

                    if(i == 0) {
                        child = uniquePersonID;
                    }

                    latitude += ThreadLocalRandom.current().nextDouble(-4, 4 + 1);
                    longitude += ThreadLocalRandom.current().nextDouble(-4, 4 + 1);

                    newEvent = new Event(UUID.randomUUID().toString(), userName, child,
                            (float) latitude, (float) longitude,
                            "USA", randomCity[r.nextInt(randomCity.length)],
                            eventType[j], yearArray[j] - (32*currGen));

                    //Make sure we are not adding events past this current year..
                    int CURRENT_YEAR = 2019;
                    if((yearArray[j] - (32*currGen)) <= CURRENT_YEAR) {
                        eventDao.insert(newEvent);
                    }

                }


            }
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
            System.out.println(e.toString());
        }

    }
}
