package kbrent.FMSDesign.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
 * Created by kbrent on 2/09/19.
 */


/**
 * This class checks to see if the user exists and if it does then it will load in fake data for the user
 */
public class FillService {

    public FillService(){}

    private int numEvents = 0;
    private int numPeople = 0;

    private EventDao eventDao;
    private PersonDao personDao;

    private Connection conn;

    private Event newEvent;
    private Person newPerson;

    private DataBase dataBase = new DataBase();

    /**
     * /fill/[username]/{generations}
     * @param userName the association
     * @param gen number of levels
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Integer[] fill(String userName, Integer gen) throws DataAccessException, IOException {

        UserService userService = new UserService();

        User user = userService.getUserWithUserName(userName);

        try {
            //dataBase.clearTablesBasedOnUser(userName);
            dataBase.clearTables();
        } catch (DataAccessException e) {
            System.out.println(e.toString());
        }

        dataBase.createTables();

        String uniqueToken = user.getToken();
        String uniquePersonID = user.getPersonId();

        Token token1 = new Token(uniqueToken, userName);
        Connection conn = dataBase.openConnection();
        TokenDao tokenDao = new TokenDao(conn);
        boolean commit = tokenDao.insert(token1);
        dataBase.closeConnection(commit);

        conn = dataBase.openConnection();
        UserDao userDao = new UserDao(conn);
        commit = userDao.insert(user);
        dataBase.closeConnection(commit);

        String Country;
        //float latitude;
        //float longitude;

        ArrayList<String> Fname = getFNames();
        List<String> Mname = getMNames();
        List<String> locationArray = getLocations();

        String lastName = "";

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

        Integer[] intArr = new Integer[2];

        String[] randomCity = {"Provo", "Mesa", "Salt Lake City"};

        String[] eventType = {"Birth", "Baptism", "First Kiss", "Marriage", "Death"};

        Integer[] randomBirths = {1993};

        ArrayList<String> personID = new ArrayList<>();
        Map<String, String> spouseToSpouse = new HashMap<>();

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

        for (int i = 1; i < personID.size() - 1; i++) {
            if ((i % 2) != 0) {
                spouseToSpouse.put(personID.get(i), personID.get(i + 1));
            } else {
                spouseToSpouse.put(personID.get(i), personID.get(i - 1));
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
                int randomNumberLast = r.nextInt(lastNames.length);

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
                    String firstName = user.getFirstName();
                    personName = firstName;
                }

                String spouse;

                if(i == 0) {
                    child = uniquePersonID;
                    spouse = "";
                    String gender = user.getGender();
                    lastName = user.getLastName();
                    genderPerson = gender;
                } else {
                    spouse = spouseToSpouse.get(child);
                    if((i % 2) != 0) {
                        lastName = lastNames[randomNumberLast];
                    }
                }

                newPerson = new Person(userName, child, personName, lastName,
                        genderPerson, parentFather, parentMother, spouse);

                personDao.insert(newPerson);
                numPeople++;

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
                        numEvents++;
                    }

                }


            }
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
            System.out.println(e.toString());
        }

        intArr[0] = numPeople;
        intArr[1] = numEvents;

        return intArr;
    }

    /**
     * Parse through fnames json file and return an array
     * @return fnamesArray which is used for the fake data
     * @throws IOException Bad practice
     */
    private ArrayList<String> getFNames() throws IOException {

        File file = new File("/Users/brent/Downloads/MyApplication2/app/src/main/java/json/fnames.json");
        Scanner scan = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while(scan.hasNext()) {
            sb.append(scan.next());
        }

        scan.close();

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray fnames = jsonObject.getAsJsonArray("data");
        StringBuilder sb1;
        ArrayList<String> nameArray = new ArrayList<>();
        for (JsonElement string : fnames) {
            sb1 = new StringBuilder();
            sb1.append(string);
            nameArray.add(sb1.toString());
        }

        return nameArray;
    }

    /**
     * Parse through mnames json file and return an array
     * @return nameArray which is used for the fake data
     * @throws IOException Bad practice..
     */
    private List<String> getMNames() throws IOException {

        File file = new File("/Users/brent/Downloads/MyApplication2/app/src/main/java/json/mnames.json");
        Scanner scan = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while(scan.hasNext()) {
            sb.append(scan.next());
        }

        scan.close();

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray fnames = jsonObject.getAsJsonArray("data");
        StringBuilder sb1;
        List<String> nameArray = new ArrayList<>();
        for (JsonElement string : fnames) {
            sb1 = new StringBuilder();
            sb1.append(string);
            nameArray.add(sb1.toString());
        }

        return nameArray;
    }

    /**
     * Parse through snames json file and return an array
     * @return nameArray which is used for the fake data
     * @throws IOException Bad practice..
     */
    private List<String> getSNames() throws IOException {

        File file = new File("/Users/brent/Downloads/MyApplication2/app/src/main/java/json/snames.json");
        Scanner scan = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while(scan.hasNext()) {
            sb.append(scan.next());
        }

        scan.close();

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray fnames = jsonObject.getAsJsonArray("data");
        StringBuilder sb1;

        List<String> nameArray = new ArrayList<>();

        for (JsonElement string : fnames) {
            sb1 = new StringBuilder();
            sb1.append(string);
            nameArray.add(sb1.toString());
        }

        return nameArray;
    }

    /**
     * Parse through locations json file and return an array
     * @return locationArray which is used for the fake data
     * @throws IOException Bad practice..
     */
    private List<String> getLocations() throws IOException {

        File file = new File("/Users/brent/Downloads/MyApplication2/app/src/main/java/json/locations.json");
        Scanner scan = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while(scan.hasNext()) {
            sb.append(scan.next());
        }

        scan.close();

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray locations = jsonObject.getAsJsonArray("data");
        StringBuilder sb1;
        List<String> locationArray = new ArrayList<>();

        for (JsonElement string : locations) {
            sb1 = new StringBuilder();
            sb1.append(string);
            locationArray.add(sb1.toString());
        }

        return locationArray;
    }

}
