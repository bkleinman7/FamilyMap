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

import static org.junit.Assert.*;

public class FillServiceTest {

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
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @After
    public void tearDown() throws Exception {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void fillSuccess() {
        //This is to check if the user is valid before trying to fill
        User compareUser = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insert(ogUser);
            compareUser = userDao.find(ogUser.getUserName());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(compareUser);
        assertEquals(ogUser, compareUser);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void fillFail() {
        User compareUser = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insert(ogUser);
            compareUser = userDao.find("badname");
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(compareUser);
        assertNotEquals(ogUser, compareUser);
    }
}