package kbrent.FMSDesign.Service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.EventDao;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;

import java.sql.Connection;

/*
 * Created by kbrent on 2/09/19.
 */

/**
 * Returns the single Event object with the specified ID
 * No parameter = Returns ALL events for ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class EventService {

    private EventDao eventDao;
    private UserDao userDao;
    private TokenDao tokenDao;
    private Connection conn;
    private DataBase dataBase = new DataBase();

    /**
     * Using an authtoken from the registered user and the eventid, return the one specific event
     * @param eventID ID comes from header
     * @return event object matched to user and eventid
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Event getEventByID(String eventID, String token) {
        Event event = null;
        User user = null;
        try {
            conn = dataBase.openConnection();
            eventDao = new EventDao(conn);
            tokenDao = new TokenDao(conn);
            userDao = new UserDao(conn);
            Token tempToken = tokenDao.findToken(token);
            user = userDao.find(tempToken.getUsername());
            if(user != null) {
                event = eventDao.find(eventID);
                dataBase.closeConnection(true);
            }
            else {
                dataBase.closeConnection(false);
                return null;
            }
        } catch (DataAccessException e) {
            System.out.println(e.toString());
        }
        if (!event.getUsername().equals(user.getUserName())){
            return null;
        }
        else {
            return event;
        }
    }

    /**
     * Return all events
     * @param token using the authtoken from the user
     * @return all events attatched to the user
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonArray getAllEventsByAccessToken(String token) {
        JsonArray ja = new JsonArray();
        try {
            conn = dataBase.openConnection();
            eventDao = new EventDao(conn);
            tokenDao = new TokenDao(conn);
            userDao = new UserDao(conn);
            Token tempToken = tokenDao.findToken(token);
            User user = userDao.find(tempToken.getUsername());
            if (user != null) {
                ja = eventDao.findEvents(user.getUserName());
                dataBase.closeConnection(true);
                return ja;
            } else {
                dataBase.closeConnection(false);
                return null;
            }
        } catch (DataAccessException e) {
            System.out.println(e.toString());
        }
        return(ja);
    }
}
