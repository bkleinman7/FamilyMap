package TestClasses.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.PersonDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Service.PersonService;

import static org.junit.Assert.*;

public class PersonServiceTest {

    DataBase dataBase = new DataBase();
    Person ogPerson;
    User ogUser;
    Token token;
    Connection conn;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase = new DataBase();
        dataBase.createTables();
        ogPerson = new Person("kbrent", "testID", "brent",
                "kleinman", "m", "kurt","robyn","clara");
        ogUser = new User("kbrent", "test",
                "testemail","brent", "k",
                "testtoken", "test_id", "m");

        token = new Token("testtoken", "kbrent");
        try {
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            UserDao userDao = new UserDao(conn);
            TokenDao tokenDao = new TokenDao(conn);
            tokenDao.insert(token);
            userDao.insert(ogUser);
            personDao.insert(ogPerson);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @After
    public void tearDown() throws Exception {
        dataBase.clearTables();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getPersonByIDSuccess() {
        Person comparePerson = null;
        PersonService personService = new PersonService();
        comparePerson = personService.getPersonByID(ogPerson.getPersonId(), ogUser.getToken());
        assertNotNull(comparePerson);
        assertEquals(ogPerson, comparePerson);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getPersonByIDFail() {
        Person comparePerson = null;
        PersonService personService = new PersonService();
        comparePerson = personService.getPersonByID("badId", "testtoken");
        assertNull(comparePerson);
        assertNotEquals(ogPerson, comparePerson);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getUserNamesFamilySuccess() {
        Person comparePerson = null;
        PersonService personService = new PersonService();
        JsonArray ja = personService.getUserNamesFamily(ogUser.getToken());
        assertNotNull(ja);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getUserNamesFamilyFail() {
        JsonArray ja = null;
        Person comparePerson = null;
        PersonService personService = new PersonService();
        ja = personService.getUserNamesFamily("badToken");
        assertNull(ja);
    }
}