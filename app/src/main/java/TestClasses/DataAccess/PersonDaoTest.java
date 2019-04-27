package TestClasses.DataAccess;

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
import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Service.UserService;

import static org.junit.Assert.*;

public class PersonDaoTest {

    Person ogPerson;
    private Connection conn;
    DataBase dataBase;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase = new DataBase();
        dataBase.createTables();
        ogPerson = new Person("kbrent", "testID", "brent",
                "kleinman", "m", "kurt","robyn","clara");
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
    public void insertSuccess() throws DataAccessException {
        boolean insertSuccess = true;
        dataBase.clearTables();
        try {
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            insertSuccess = personDao.insert(ogPerson);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertTrue(insertSuccess);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void insertFail() throws DataAccessException {
        boolean insertSuccess = true;
        boolean insertSuccess2 = true;
        dataBase.clearTables();
        try {
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            insertSuccess = personDao.insert(ogPerson);
            insertSuccess2 = personDao.insert(ogPerson);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
        }
        assertTrue(insertSuccess);
        assertFalse(insertSuccess2);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findSuccess() throws DataAccessException {
        Person comparePerson = null;
        dataBase.clearTables();
        try {
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            personDao.insert(ogPerson);
            comparePerson = personDao.find(ogPerson.getPersonId());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
        }
        assertNotNull(comparePerson);
        assertEquals(ogPerson, comparePerson);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findFail() {
        Person comparePerson = null;
        try {
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            personDao.insert(ogPerson);
            comparePerson = personDao.find("badID");
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(comparePerson);
        assertNotEquals(ogPerson, comparePerson);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findPersonsSuccess() {
        JsonArray jsonArray = null;
        try {
            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            //Find events based on userName
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            jsonArray = personDao.findPersons("kbrent");
            if (jsonArray.size() == 0) { jsonArray = null; }
            dataBase.closeConnection(true);

        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(jsonArray);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findPersonsFail() {
        JsonArray jsonArray = null;
        try {
            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            //Find events based on userName
            conn = dataBase.openConnection();
            PersonDao personDao = new PersonDao(conn);
            jsonArray = personDao.findPersons("badName");
            if (jsonArray.size() == 0) { jsonArray = null; }
            dataBase.closeConnection(true);

        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(jsonArray);
    }
}