package kbrent.FMSDesign.DataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kbrent.FMSDesign.Model.Person;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * This class inserts people into the db and returns a list or a specific person
 */
public class PersonDao {

    private Connection conn;

    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a person into the db
     * @param person Person to be inserted
     * @return commit this is to tell the db to close or rollback the commit
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean insert(Person person) {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (Descendant, PersonID, Firstname, Lastname, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getDescendant());
            stmt.setString(2, person.getPersonId());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherId());
            stmt.setString(7, person.getMotherId());
            stmt.setString(8, person.getSpouseId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            System.out.println(e.toString());
        }

        return commit;
    }

    /**
     * Find a specific person based on a person id for /person/[id] API call
     * @param personID Specific id to search
     * @return person for the response
     * @throws DataAccessException Know why find fails
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("Descendant"), rs.getString("PersonID"),
                        rs.getString("Firstname"), rs.getString("Lastname"),
                        rs.getString("Gender"), rs.getString("FatherID"), rs.getString("MotherID"),
                        rs.getString("SpouseID"));
                return person;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return null;
    }

    /**
     * To find all persons based on /person API call
     * @param userName find all people attatched to this user
     * @return ja which is a jason array for the response
     * @throws DataAccessException Why find persons failed
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonArray findPersons(String userName) throws DataAccessException {
        JsonArray ja = new JsonArray();
        Person person;
        ResultSet rs;

        String sqlPersonID = "SELECT * FROM Persons WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sqlPersonID)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                JsonObject jo = new JsonObject();
                person = new Person(rs.getString("Descendant"), rs.getString("PersonID"),
                        rs.getString("Firstname"), rs.getString("Lastname"),
                        rs.getString("Gender"), rs.getString("FatherID"), rs.getString("MotherID"),
                        rs.getString("SpouseID"));

                jo.addProperty("descendant", person.getDescendant());
                jo.addProperty("personID", person.getPersonId());
                jo.addProperty("firstName", person.getFirstName());
                jo.addProperty("lastName", person.getLastName());
                jo.addProperty("gender", person.getGender());
                jo.addProperty("father", person.getFatherId());
                jo.addProperty("mother", person.getMotherId());
                jo.addProperty("spouse", person.getSpouseId());

                ja.add(jo);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error encountered while finding personIDs");
        }


        return ja;
    }

}
