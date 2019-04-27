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
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Service.UserService;

import static org.junit.Assert.*;

public class EventDaoTest {

    private DataBase dataBase = new DataBase();
    private Event ogEvent;
    private Connection conn;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase.createTables();
        ogEvent = new Event("test123", "kbrent", "testpID", 90,
                91,"USA", "mesa", "birth", 1990);
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
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            insertSuccess = eventDao.insert(ogEvent);
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
    public void insertFail() {
        boolean insertSuccess = true;
        boolean insertSuccessSecond = true;
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            insertSuccess = eventDao.insert(ogEvent);
            insertSuccessSecond = eventDao.insert(ogEvent);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                insertSuccess = false;
            }
        }
        assertTrue(insertSuccess);
        assertFalse(insertSuccessSecond);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findSuccess() {
        Event compareEvent = null;
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(ogEvent);
            compareEvent = eventDao.find(ogEvent.getEventID());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(compareEvent);
        assertEquals(ogEvent, compareEvent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findFail() {
        Event compareEvent = null;
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(ogEvent);
            compareEvent = eventDao.find("badID");
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(compareEvent);
        assertNotEquals(ogEvent, compareEvent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findEventsSuccess() {
        JsonArray jsonArray = null;
        try {
            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            //Find events based on userName
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            jsonArray = eventDao.findEvents("kbrent");
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
    public void findEventsFail() {
        JsonArray jsonArray = null;
        try {
            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            //Find events based on userName
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            jsonArray = eventDao.findEvents("invalid");
            dataBase.closeConnection(true);
            if (jsonArray.size() == 0) {
                jsonArray = null;
            }

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