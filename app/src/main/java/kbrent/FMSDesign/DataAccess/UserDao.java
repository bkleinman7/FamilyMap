package kbrent.FMSDesign.DataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kbrent.FMSDesign.Model.User;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * This class is used for registering a user, checking to see if a user exists or a token is valid.
 */
public class UserDao {

    private Connection conn;

    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a user into the db based on the /register API call
     * @param user What info of a user to insert
     * @return commit for the db
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean insert(User user) {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (Username, Password, Email, Firstname, Lastname, " +
                "Token, PersonID, Gender) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassWord());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getToken());
            stmt.setString(7, user.getPersonId());
            stmt.setString(8, user.getGender());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            System.out.println(e.toString());
        }

        return commit;
    }

    /**
     * Checks to see if a user exists
     * @param userName Username to return a specific user
     * @return user if null then we can insert or we shouldn't use the find API call
     * @throws DataAccessException Find out why find failed
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User find(String userName) throws DataAccessException {

        User user;
        ResultSet rs;
        String sql = "SELECT * FROM Users WHERE Username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("Firstname"), rs.getString("Lastname"),
                        rs.getString("Token"), rs.getString("PersonID"), rs.getString("Gender"));
                return user;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error encountered while finding token");
        }
        return null;
    }

    /**
     * Used for the login API call, check to see if username and password is valid
     * @param userName Username to search
     * @param password Password to search
     * @return user
     * @throws DataAccessException To find why login failed
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User findLogin(String userName, String password) throws DataAccessException {

        User user;
        ResultSet rs;
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("Firstname"), rs.getString("Lastname"),
                        rs.getString("Token"), rs.getString("PersonID"), rs.getString("Gender"));
                return user;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Error encountered while finding username and password");
        }
        return null;
    }

}
