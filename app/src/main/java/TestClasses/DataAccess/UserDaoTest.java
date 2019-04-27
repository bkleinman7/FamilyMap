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
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;

import static org.junit.Assert.*;

public class UserDaoTest {

   private DataBase dataBase = new DataBase();
    private User ogUser;
    private Token ogToken;
    private Connection conn;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        dataBase.createTables();
        ogUser = new User("kbrent", "test",
                "testemail","brent", "k",
                "testtoken", "test_id", "m");
        ogToken = new Token("testtoken", "kbrent");
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
    public void insertSuccess() {
        boolean insertSuccess = false;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            insertSuccess = userDao.insert(ogUser);
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
        boolean insertSuccess2 = true;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            insertSuccess = userDao.insert(ogUser);
            insertSuccess2 = userDao.insert(ogUser);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertTrue(insertSuccess);
        assertFalse(insertSuccess2);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findSuccess() {
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
    public void findFail() {
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
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findTokenSuccess() {
        Token compareToken = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            TokenDao tokenDao = new TokenDao(conn);
            userDao.insert(ogUser);
            tokenDao.insert(ogToken);
            compareToken = tokenDao.findToken(ogToken.getToken());
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNotNull(compareToken);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findTokenFail() {
        Token compareToken = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            TokenDao tokenDao = new TokenDao(conn);
            userDao.insert(ogUser);
            compareToken = tokenDao.findToken("badtoken");
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(compareToken);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void findLoginSuccess() {
        User compareUser = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insert(ogUser);
            compareUser = userDao.findLogin("kbrent", "test");
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
    public void findLoginFail() {
        User compareUser = null;
        try {
            conn = dataBase.openConnection();
            UserDao userDao = new UserDao(conn);
            userDao.insert(ogUser);
            compareUser = userDao.findLogin("kbrent", "badpw");
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }
        assertNull(compareUser);
    }
}