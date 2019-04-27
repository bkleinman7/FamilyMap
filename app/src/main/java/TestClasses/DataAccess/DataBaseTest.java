package TestClasses.DataAccess;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.User;

import static org.junit.Assert.*;

public class DataBaseTest {

    private DataBase dataBase = new DataBase();
    private Connection conn;
    private Event ogEvent;
    private User ogUser;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase.createTables();
        ogEvent = new Event("test123", "kbrent", "testpID", 90,
                91,"USA", "mesa", "birth", 1990);
        ogUser = new User("kbrent", "test",
                "testemail","brent", "k",
                "testtoken", "test_id", "m");
        try {
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(ogEvent);
            UserDao userDao = new UserDao(conn);
            userDao.insert(ogUser);
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
    public void clearTablesBasedOnUserSuccess() {
        Event testEvent = null;
        try {
            dataBase.clearTablesBasedOnUser(ogUser.getUserName());
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            testEvent = eventDao.find(ogEvent.getEventID());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(testEvent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void clearTablesSuccess() {
        User user = null;
        try {
            dataBase.clearTables();
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            user = userDao.find(ogUser.getUserName());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(user);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void clearTablesBasedOnUserFalse() {
        Event testEvent = null;
        try {
            dataBase.clearTablesBasedOnUser("fakename");
            conn = dataBase.openConnection();
            EventDao eventDao = new EventDao(conn);
            testEvent = eventDao.find(ogEvent.getEventID());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(testEvent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void clearTablesFalse() {
        User user = null;
        try {
            //dataBase.clearTables();
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            user = userDao.find(ogUser.getUserName());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(user);
    }
}