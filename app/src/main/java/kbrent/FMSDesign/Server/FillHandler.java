package kbrent.FMSDesign.Server;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.Model.User;
import kbrent.FMSDesign.Result.Response;
import kbrent.FMSDesign.Service.FillService;
import kbrent.FMSDesign.Service.UserService;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * Routes to the FillService
 */
public class FillHandler implements HttpHandler {

    private Response response = new Response();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String[] parameters = uri.toString().split("/");
        System.out.println(parameters.length);
        JsonObject jo = new JsonObject();
        FillService fillService = new FillService();
        User user = null;
        UserService userService = new UserService();
        Integer[] intArr = new Integer[2];
        int headerVal;

        if(parameters.length == 3) {

            String userName = parameters[2];
            try {
                user = userService.getUserWithUserName(userName);
            } catch (DataAccessException e) {
                System.out.println(e.toString());
            }
            if(user == null) {
                jo.addProperty("message", "user does not exist");
                headerVal = 400;
            }
            else{
                try {
                    intArr = fillService.fill(userName, 4);
                } catch (DataAccessException e) {
                    System.out.println(e.toString());
                }
                if(intArr == null) {
                    jo.addProperty("message", "user exists");
                    headerVal = 400;
                }
                else {
                    jo.addProperty("message", "added " + intArr[0] + " people" +
                            " and " + intArr[1] + " events");
                    headerVal = 200;
                }

            }

            response.sendMessage(jo, httpExchange, headerVal);
        }
        else if(parameters.length == 4) {

            String userName = parameters[2];
            Integer numGen = Integer.parseInt(parameters[3]);

            try {
                user = userService.getUserWithUserName(userName);
            } catch (DataAccessException e) {
                System.out.println(e.toString());
            }
            if(user == null) {
                jo.addProperty("message", "user does not exist");
                headerVal = 400;
            }
            else{
                if(numGen > 0 && numGen < 8) {
                    try {
                        intArr = fillService.fill(userName, numGen);
                    } catch (DataAccessException e) {
                        System.out.println(e.toString());
                    }
                    if (intArr == null) {
                        jo.addProperty("message", "Nothing was added.. weird");
                        headerVal = 400;
                    } else {
                        jo.addProperty("message", "added " + intArr[0] + " people" +
                                " and " + intArr[1] + " events");
                        headerVal = 200;
                    }
                }
                else {
                    jo.addProperty("message", "generation number must be from 0-7");
                    headerVal = 400;
                }
            }


            response.sendMessage(jo, httpExchange, headerVal);
        }

    }
}