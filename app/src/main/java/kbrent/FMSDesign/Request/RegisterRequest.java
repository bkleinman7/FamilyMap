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
import kbrent.FMSDesign.DataAccess.UserDao;
import kbrent.FMSDesign.Model.Token;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Service.UserService;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * If the /register API call is made this will parse through the body and insert the user if info is valid
 */
public class RegisterRequest {

    private DataBase dataBase = new DataBase();

    /**
     * This is to check if the json is valid and will insert the user into the db
     * @param jsonString passed in from handler
     * @return jsobObject for the response body
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JsonObject checkJson(String jsonString) {

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject jo = new JsonObject();

        String userName;
        String password;
        String email;
        String firstName;
        String lastName;
        String gender;

        if (jsonObject.has("userName") && !jsonObject.isJsonNull()) {
            userName = jsonObject.get("userName").getAsString();
            if(userName.isEmpty()) { jo.addProperty("message", "userName value is empty");  return jo;}
        }
        else { jo.addProperty("message", "userName is empty"); return jo;}

        if (jsonObject.has("password") && !jsonObject.isJsonNull()) {
            password = jsonObject.get("password").getAsString();
            if(password.isEmpty()) { jo.addProperty("message", "password value is empty");  return jo;}
        }
        else { jo.addProperty("message", "password is empty");  return jo;}

        if (jsonObject.has("email") && !jsonObject.isJsonNull()) {
            email = jsonObject.get("email").getAsString();
            if(email.isEmpty()) { jo.addProperty("message", "email value is empty");  return jo;}
        }
        else { jo.addProperty("message", "email is empty");  return jo;}

        if (jsonObject.has("firstName") && !jsonObject.isJsonNull()) {
            firstName = jsonObject.get("firstName").getAsString();
            if(firstName.isEmpty()) { jo.addProperty("message", "firstName value is empty");  return jo;}
        }
        else { jo.addProperty("message", "firstName is empty");  return jo;}

        if (jsonObject.has("lastName") && !jsonObject.isJsonNull()) {
            lastName = jsonObject.get("lastName").getAsString();
            if(lastName.isEmpty()) { jo.addProperty("message", "lastName value is empty");  return jo;}
        }
        else { jo.addProperty("message", "lastName is empty");  return jo;}

        if (jsonObject.has("gender") && !jsonObject.isJsonNull()) {
            gender = jsonObject.get("gender").getAsString();
            if(!gender.equals("f") && !gender.equals("m")) { jo.addProperty("message", "gender must be m or f"); return jo; }
            if(gender.isEmpty()) { jo.addProperty("message", "gender value is empty");  return jo;}
        }
        else { jo.addProperty("message", "gender is empty");  return jo;}

        try {
            UserService userService = new UserService();
            User user = userService.getUserWithUserName(userName);

            if (user == null) {

                dataBase.createTables();

                String uniqueToken = UUID.randomUUID().toString();
                String uniquePersonID = UUID.randomUUID().toString();

                Token token1 = new Token(uniqueToken, userName);
                Connection conn = dataBase.openConnection();
                TokenDao tokenDao = new TokenDao(conn);
                boolean commit = tokenDao.insert(token1);
                dataBase.closeConnection(commit);

                user = new User(userName, password, email, firstName, lastName, uniqueToken, uniquePersonID, gender);

                conn = dataBase.openConnection();
                UserDao userDao = new UserDao(conn);
                commit = userDao.insert(user);

                dataBase.closeConnection(commit);

                if(commit) {
                    jo.addProperty("userName", userName);
                    jo.addProperty("token", uniqueToken);
                    jo.addProperty("personID", uniquePersonID);
                    userService.loadPersonsAndEvents(userName, uniquePersonID, firstName, lastName, gender, 4);
                    return jo;
                }
                else
                    jo.addProperty("message", "User failed to insert");
                    return jo;
            }
            else {
                System.out.println(user.getToken());
                jo.addProperty("message", "User already exists");
                return jo;
            }
        } catch (DataAccessException e) {
            jo.addProperty("message", "Weird error");
            System.out.println(e.toString());
        }


        return jo;

    }
}
