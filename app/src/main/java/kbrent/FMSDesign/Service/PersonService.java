package kbrent.FMSDesign.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;

import java.sql.Connection;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.PersonDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Person;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;

/*
 * Created by kbrent on 2/09/19.
 */

/**
 * Return all info about a person based on ID, userName
 * Also can return an array of people based on userNmae
 */
public class PersonService {

    private Connection conn;
    private DataBase dataBase = new DataBase();
    private PersonDao personDao;
    private UserDao userDao;
    private TokenDao tokenDao;

    public PersonService(){ }

    /**
     * Return a specific person
     * @param personID specific ID
     * @return person object
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Person getPersonByID(String personID, String token) {

        //grab the user by the auth token
        //and then check to see if the user is
        //this persons decendant, if not return
        //null
        Person person = null;
        try {
            conn = dataBase.openConnection();
            personDao = new PersonDao(conn);
            userDao = new UserDao(conn);
            tokenDao = new TokenDao(conn);
            Token tempToken = tokenDao.findToken(token);
            User user = userDao.find(tempToken.getUsername());
            if(user != null) {
                person = personDao.find(personID);
                dataBase.closeConnection(true);
                if (person == null){
                    return null;
                }
                else {
                    if (!person.getDescendant().equals(user.getUserName())){
                        return null;
                    }
                }
            }
            else {
                dataBase.closeConnection(false);
                return null;
            }

        } catch (DataAccessException e) {
            System.out.println(e.toString());
        }

        return person;
    }

    /**
     * Returns a list of people based on userName
     * @param token current userName
     * @return an array of persons
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonArray getUserNamesFamily(String token) {
        JsonArray ja = new JsonArray();
        try {
            conn = dataBase.openConnection();
            personDao = new PersonDao(conn);
            userDao = new UserDao(conn);
            tokenDao = new TokenDao(conn);
            Token tempToken = null;
            tempToken = tokenDao.findToken(token);
            if(tempToken == null) {
                return null;
            }
            else {
                User user = userDao.find(tempToken.getUsername());
                if (user != null) {
                    ja = personDao.findPersons(user.getUserName());
                    dataBase.closeConnection(true);
                    return ja;
                } else {
                    dataBase.closeConnection(false);
                    return null;
                }
            }
        } catch (DataAccessException e) {
            System.out.println(e.toString());
        }
        return(ja);
    }

}
