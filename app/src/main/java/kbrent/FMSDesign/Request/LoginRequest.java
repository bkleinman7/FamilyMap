package kbrent.FMSDesign.Request;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.util.UUID;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.DataAccess.DataBase;
import kbrent.FMSDesign.DataAccess.TokenDao;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Service.UserService;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * This class logs the user in based on their post body
 */
public class LoginRequest {

    private DataBase dataBase = new DataBase();

    /**
     * This checks the json for validity and if the user exists, the info will be returned
     * @param jsonString to be parsed
     * @return jsonObject for the response body
     * @throws DataAccessException Bad Practice..
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonObject checkJson(String jsonString) throws DataAccessException {

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jo = new JsonObject();
        String userName;
        String password;

        if (jsonObject.has("userName") && !jsonObject.isJsonNull()) {
            userName = jsonObject.get("userName").getAsString();
        }
        else { jo.addProperty("message", "userName is empty"); return jo;}

        if (jsonObject.has("password") && !jsonObject.isJsonNull()) {
            password = jsonObject.get("password").getAsString();
        }
        else { jo.addProperty("message", "password is empty"); return jo;}

        try {
            UserService userService = new UserService();
            User user = userService.getUserWithUsernamePassword(userName, password);

            if(user == null) {
                jo.addProperty("message", "Username or password is invalid");
                return jo;
            }
            else {
                String token = UUID.randomUUID().toString();
                String personID = user.getPersonId();
                Token token1 = new Token(token, userName);
                Connection conn = dataBase.openConnection();
                TokenDao tokenDao = new TokenDao(conn);
                boolean commit = tokenDao.insert(token1);
                dataBase.closeConnection(commit);
                jo.addProperty("authToken", token);
                jo.addProperty("userName", userName);
                jo.addProperty("personID", personID);
            }

        } catch (DataAccessException e) {
            System.out.println(e.toString());
            dataBase.closeConnection(false);
        }

        return jo;

    }
}
