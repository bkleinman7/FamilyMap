package kbrent.FMSDesign.DataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.*;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * Allow the service classes to access the database
 */
public class DataBase {

    private Connection conn;

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to DB
     * @return a connection for the DAO classes
     * @throws DataAccessException To know what happened
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:/Users/brent/Downloads/MyApplication2/app/db/database.sqlite";

            conn = DriverManager.getConnection(CONNECTION_URL);

            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * This is to commit changes or to rollback changes
     * @param commit t/f
     * @throws DataAccessException Incase the close fails
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * This is to ensure that we aren't trying to insert into empty tables for register API call
     * @throws DataAccessException Incase the create fails..
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createTables() throws DataAccessException {

        openConnection();

        try (Statement stmt = conn.createStatement()) {

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exaclty like a sql create table command
            String sqlEvents = "CREATE TABLE IF NOT EXISTS Events \n" +
                    "(EventID text NOT NULL unique, Descendant text NOT null, \n" +
                    " PersonID text NOT null, Latitude float NOT null, Longitude float NOT null, \n" +
                    " Country text NOT null, City text NOT null, \n" +
                    " EventType text NOT null, Year int NOT null, primary key (EventID), \n" +
                    " foreign key (Descendant) references Users(Username), \n" +
                    " foreign key (PersonID) references Persons(PersonID))";

            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users  \n" +
                    "(Username text not null unique,  \n" +
                    "Password text not null,  \n" +
                    "Email text not null,  \n" +
                    "Firstname text not null,  \n" +
                    "Lastname text not null,  \n" +
                    "Token text not null,  \n" +
                    "PersonID text not null,  \n" +
                    "Gender text not null,  \n" +
                    "primary key (Username))";

            String sqlPersons = "CREATE TABLE IF NOT EXISTS Persons  \n" +
                    "(Descendant text not null,  \n" +
                    "PersonID text not null,  \n" +
                    "Firstname text not null,  \n" +
                    "Lastname text not null,  \n" +
                    "Gender text not null,  \n" +
                    "FatherID text,  \n" +
                    "MotherID text,  \n" +
                    "SpouseID text,  \n" +
                    "primary key (PersonID))";

            String sqlTokens = "CREATE TABLE IF NOT EXISTS Tokens  \n" +
                    "(Token text not null,  \n" +
                    "Username text not null,  \n" +
                    "primary key (Token))";

            stmt.executeUpdate(sqlEvents);
            stmt.executeUpdate(sqlUsers);
            stmt.executeUpdate(sqlPersons);
            stmt.executeUpdate(sqlTokens);

            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }

    }

    /**
     * This is for the fill API call
     * @param userName Delete info based on this userName
     * @throws DataAccessException Bad Practice..
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void clearTablesBasedOnUser(String userName) throws DataAccessException {

        openConnection();

        String sqlPersons = "DELETE FROM Persons WHERE Descendant = ?;";
        String sqlEvents = "DELETE FROM Events WHERE Descendant = ?;";
        //String sqlTokens = "DELETE FROM Tokens WHERE Username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sqlPersons)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        try (PreparedStatement stmt = conn.prepareStatement(sqlEvents)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        /*try (PreparedStatement stmt = conn.prepareStatement(sqlTokens)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }*/

        closeConnection(true);

    }

    /**
     * This will clear out all data and recreate the tables
     * For the /clear API call and the /load API call
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void clearTables() throws DataAccessException
    {
        openConnection();

        try (Statement stmt = conn.createStatement()){
            String sqlEvents = "DROP TABLE IF EXISTS Events";
            String sqlUser = "DROP TABLE IF EXISTS Users";
            String sqlPerson = "DROP TABLE IF EXISTS Persons";
            String sqlToken = "DROP TABLE IF EXISTS Tokens";
            stmt.executeUpdate(sqlEvents);
            stmt.executeUpdate(sqlUser);
            stmt.executeUpdate(sqlPerson);
            stmt.executeUpdate(sqlToken);

            String sqlTokensTable = "CREATE TABLE IF NOT EXISTS Tokens  \n" +
                    "(Token text not null,  \n" +
                    "Username text not null,  \n" +
                    "primary key (Token))";

            String sqlEventsTable = "CREATE TABLE IF NOT EXISTS Events " +
                    "(" +
                    "EventID text not null unique, " +
                    "Descendant text not null, " +
                    "PersonID text not null, " +
                    "Latitude float not null, " +
                    "Longitude float not null, " +
                    "Country text not null, " +
                    "City text not null, " +
                    "EventType text not null, " +
                    "Year int not null, " +
                    "primary key (EventID), " +
                    "foreign key (Descendant) references Users(Username), " +
                    "foreign key (PersonID) references Persons(PersonID)" +
                    ")";

            String sqlUserTable = "CREATE TABLE Users " +
                    "(" +
                    "Username text not null unique, " +
                    "Password text not null, " +
                    "Email text not null, " +
                    "Firstname text not null, " +
                    "Lastname text not null, " +
                    "Token text not null, " +
                    "PersonID text not null, " +
                    "Gender text not null, " +
                    "primary key (Username) " +
                    ")";

            String sqlPersonTable = "CREATE TABLE IF NOT EXISTS Persons " +
                    "(" +
                    "Descendant text not null, " +
                    "PersonID text not null, " +
                    "Firstname text not null, " +
                    "Lastname text not null, " +
                    "Gender text not null, " +
                    "FatherID text, " +
                    "MotherID text, " +
                    "SpouseID text, " +
                    "primary key (PersonID) " +
                    ")";
            stmt.executeUpdate(sqlEventsTable);
            stmt.executeUpdate(sqlUserTable);
            stmt.executeUpdate(sqlPersonTable);
            stmt.executeUpdate(sqlTokensTable);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
