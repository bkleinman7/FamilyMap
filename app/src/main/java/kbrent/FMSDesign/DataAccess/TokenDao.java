package kbrent.FMSDesign.DataAccess;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kbrent.FMSDesign.Model.Token;

public class TokenDao {

    private Connection conn;

    public TokenDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a token into the db based on the /register API call or /login API
     * @param token token info to insert
     * @return commit for the db
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean insert(Token token) {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Tokens (Token, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token.getToken());
            stmt.setString(2, token.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            System.out.println(e.toString());
        }

        return commit;
    }

    /**
     * This is used for all API calls that require an authToken
     * @param token Try to find user based on a token..
     * @return user based on token passed in
     * @throws DataAccessException find why findToken fails
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Token findToken(String token) throws DataAccessException {

        Token token1 = null;
        ResultSet rs;
        String sqlToken = "SELECT * FROM Tokens WHERE Token = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sqlToken)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token1 = new Token(rs.getString("Token"), rs.getString("Username"));
            }
            return token1;

        } catch (SQLException e) {
            return  null;
            //System.out.println(e.toString());
            //throw new DataAccessException("Error encountered while finding token");
        }

    }
}
