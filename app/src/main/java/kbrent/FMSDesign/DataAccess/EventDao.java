package kbrent.FMSDesign.DataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import kbrent.FMSDesign.Model.Event;
import kbrent.FMSDesign.Model.Person;

/*
 * Created by kbrent on 2/09/19.
 */

/**
 * This class is for inserting events into the db,
 * finding all events, and finding events by an id
 */
public class EventDao {
    private Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert events into the db
     * @param event Event to be inserted
     * @return commit this is to tell the db to close or rollback the commit
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean insert(Event event) {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            System.out.println(e.toString());
        }

        return commit;
    }

    /**
     * Find events by an ID for the /event/[id] API call
     * @param eventID Passed in to find a specific event
     * @return event this returns an event, specifically
     * @throws DataAccessException Find out what will happen if find fails
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Event find(String eventID) throws DataAccessException {

        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));

                return event;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error encountered while finding event");
        }
        return null;
    }

    /**
     * When a user calls the /event API this will return all events associated with the user
     * @param userName find events based on this userName
     * @return ja which is a json array to be sent to the response
     * @throws DataAccessException Know why findEvents failed
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonArray findEvents(String userName) throws DataAccessException {
        JsonArray ja = new JsonArray();
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE PersonID = ?;";
        Set<String> personIdList = new TreeSet<>();

        String sqlPersonID = "SELECT * FROM Persons WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sqlPersonID)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person person = new Person(rs.getString("Descendant"), rs.getString("PersonID"),
                        rs.getString("Firstname"), rs.getString("Lastname"),
                        rs.getString("Gender"), rs.getString("FatherID"), rs.getString("MotherID"),
                        rs.getString("SpouseID"));
                personIdList.add(person.getPersonId());
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error encountered while finding personIDs");
        }

        for (String aPersonIdList : personIdList) {

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, aPersonIdList);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    JsonObject jo = new JsonObject();
                    event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                            rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                            rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                            rs.getInt("Year"));

                    jo.addProperty("descendant", event.getUsername());
                    jo.addProperty("eventID", event.getEventID());
                    jo.addProperty("personID", event.getPersonID());
                    jo.addProperty("latitude", event.getLatitude());
                    jo.addProperty("longitude", event.getLongitude());
                    jo.addProperty("country", event.getCountry());
                    jo.addProperty("city", event.getCity());
                    jo.addProperty("eventType", event.getEventType());
                    jo.addProperty("year", event.getYear());
                    ja.add(jo);
                }

            } catch (SQLException e) {
                System.out.println(e.toString());
                throw new DataAccessException("Error encountered while finding event");
            }
        }
        return ja;
    }
}
