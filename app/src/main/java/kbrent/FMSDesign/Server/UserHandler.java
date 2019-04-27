package kbrent.FMSDesign.Server;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.Result.Response;
import kbrent.FMSDesign.Service.UserService;

/*
 * Created by kbrent on 2/09/19.
 */

/**
 * This class can login or register a user
 * /login
 * /register
 * are the two paths that will be passed in
 * Check to see if those paths exists to either call the LoginRequest or RegisterRequest
 */
public class UserHandler implements HttpHandler {

    private Response response = new Response();
    private JsonObject jsonObject = new JsonObject();

    /**
     * In this handle we will pass this into the URI and
     * will split the uri based on the /
     * @param httpExchange passed in from server
     */
    @SuppressLint("Assert")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String[] parameters = uri.toString().split("/");
        UserService userService = new UserService();

        int headerVal;

        switch (parameters[2]) {
            case "login":
                try {
                    jsonObject = userService.login(httpExchange);
                    headerVal = 200;
                } catch (DataAccessException e) {
                    headerVal = 400;
                    jsonObject.addProperty("message", "Invalid");
                    System.out.println(e.toString());
                }
                response.sendMessage(jsonObject, httpExchange, headerVal);
                break;
            case "register":
                try {
                    jsonObject = userService.register(httpExchange);
                    headerVal = 200;
                } catch (DataAccessException e) {
                    headerVal = 400;
                    jsonObject.addProperty("message", "Invalid");
                    System.out.println(e.toString());
                }
                response.sendMessage(jsonObject, httpExchange, headerVal);
                break;
            default:
                try {
                    headerVal = 400;
                    jsonObject.addProperty("message", "Invalid");
                    response.sendMessage(jsonObject, httpExchange, headerVal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
