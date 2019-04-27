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
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Service.UserService;

import static org.junit.Assert.*;

public class EventServiceTest {

    Connection conn;
    DataBase dataBase;
    Event ogEvent;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase = new DataBase();
        dataBase.createTables();
        ogEvent = new Event("test123", "kbrent", "testpID", 90,
                91,"USA", "mesa", "birth", 1990);
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(ogEvent);
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
    public void getEventByIDSuccess() {
        Event compareEvent = null;
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
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
    public void getEventByIDFail() {
        Event compareEvent = null;
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
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
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getAllEventsByAccessTokenSuccess() throws DataAccessException {
        JsonArray ja = null;
        UserDao userDao;
        EventDao eventDao;
        try {
            conn = dataBase.openConnection();
            userDao = new UserDao(conn);
            User ogUser = new User("kbrent", "test",
                    "testemail","brent", "k",
                    "testtoken", "test_id", "m");
            userDao.insert(ogUser);
            dataBase.closeConnection(true);

            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            conn = dataBase.openConnection();
            eventDao = new EventDao(conn);
            userDao = new UserDao(conn);
            TokenDao tokenDao = new TokenDao(conn);
            Token quickToken = new Token("testtoken", "kbrent");
            tokenDao.insert(quickToken);
            Token tempToken = tokenDao.findToken(quickToken.getToken());
            User user = userDao.find(tempToken.getUsername());
            ja = eventDao.findEvents(user.getUserName());
            if (ja.size() == 0) { ja = null; }
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
            System.out.println(e.toString());
        }
        assertNotNull(ja);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getAllEventsByAccessTokenFail() throws DataAccessException {

        JsonArray ja = null;
        Token tempToken = null;
        UserDao userDao;

        try {
            conn = dataBase.openConnection();
            userDao = new UserDao(conn);
            User ogUser = new User("kbrent", "test",
                    "testemail","brent", "k",
                    "testtoken", "test_id", "m");
            userDao.insert(ogUser);
            dataBase.closeConnection(true);

            //Load in some events for a user...
            UserService userService = new UserService();
            userService.loadPersonsAndEvents("kbrent", "test_personID",
                    "brent", "kleinman", "m", 4);

            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            TokenDao tokenDao = new TokenDao(conn);
            Token quickToken = new Token("testtoken", "kbrent");
            tokenDao.insert(quickToken);
            //tempToken = tokenDao.findToken("badToken");
            ja = eventDao.findEvents(ogUser.getUserName());
            if (ja.size() == 0) { ja = null; }
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            dataBase.closeConnection(false);
            System.out.println(e.toString());
        }
        assertNull(tempToken);

    }
}