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
import kbrent.FMSDesign.DataAccess.PersonDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Token;

import static org.junit.Assert.*;

public class TokenDaoTest {

    Token token;
    Token tokenTest;
    Connection conn;
    DataBase dataBase;
    TokenDao tokenDao;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @After
    public void tearDown() throws Exception {
        //dataBase.clearTables();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void insertSuccess() {
        token = new Token("testtoken", "kbrent");
        try {
            dataBase = new DataBase();
            dataBase.clearTables();
            conn = dataBase.openConnection();
            tokenDao = new TokenDao(conn);
            assertTrue(tokenDao.insert(token));
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
    @Test
    public void insertFail() {
        token = new Token("testtoken", "kbrent");
        Token token2 = new Token("testtoken", "kbrent");
        try {
            dataBase = new DataBase();
            dataBase.clearTables();
            conn = dataBase.openConnection();
            tokenDao = new TokenDao(conn);
            assertTrue(tokenDao.insert(token));
            assertFalse(tokenDao.insert(token2));
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
    @Test
    public void findTokenSuccess() {

        token = new Token("testtoken", "kbrent");
        try {
            dataBase = new DataBase();
            dataBase.clearTables();
            conn = dataBase.openConnection();
            tokenDao = new TokenDao(conn);
            assertTrue(tokenDao.insert(token));
            token = tokenDao.findToken(token.getToken());
            assertNotNull(token);
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
    @Test
    public void findTokenFail() {

        token = new Token("testtoken", "kbrent");
        try {
            dataBase = new DataBase();
            dataBase.clearTables();
            conn = dataBase.openConnection();
            tokenDao = new TokenDao(conn);
            assertTrue(tokenDao.insert(token));
            token = tokenDao.findToken("badT");
            assertNull(token);
            dataBase.closeConnection(true);
        } catch (DataAccessException e) {
            try {
                dataBase.closeConnection(false);
            } catch (DataAccessException e1) {
                e1.printStackTrace();
            }
        }

    }
}