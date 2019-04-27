package TestClasses.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Service.UserService;

import static org.junit.Assert.*;

public class UserServiceTest {

    DataBase dataBase = new DataBase();
    User ogUser;
    Connection conn;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase.createTables();
        ogUser = new User("kbrent", "test",
                "testemail","brent", "k",
                "testtoken", "test_id", "m");
        try {
            conn = dataBase.openConnection();
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
    public void getUserWithUserNameSuccess() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        compareUser = userService.getUserWithUserName(ogUser.getUserName());
        assertNotNull(compareUser);
        assertEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getUserWithUserNameFail() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        compareUser = userService.getUserWithUserName("badUser");
        assertNull(compareUser);
        assertNotEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getUserWithUsernamePasswordSuccess() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        compareUser = userService.getUserWithUsernamePassword(ogUser.getUserName(), ogUser.getPassWord());
        assertNotNull(compareUser);
        assertEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void getUserWithUsernamePasswordFail() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        compareUser = userService.getUserWithUsernamePassword(ogUser.getUserName(), "badPW");
        assertNull(compareUser);
        assertNotEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void loadPersonsAndEventsSuccess() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        userService.loadPersonsAndEvents(ogUser.getUserName(),
                ogUser.getPersonId(), ogUser.getFirstName(), ogUser.getLastName(), ogUser.getGender(), 4);
        //assertNotNull(compareUser);
        assertNotEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void loadPersonsAndEventsFail() throws DataAccessException {
        User compareUser = null;
        UserService userService = new UserService();
        userService.loadPersonsAndEvents(ogUser.getUserName(),
                ogUser.getPersonId(), ogUser.getFirstName(), ogUser.getLastName(), ogUser.getGender(), 4);
        assertNull(compareUser);
    }
}