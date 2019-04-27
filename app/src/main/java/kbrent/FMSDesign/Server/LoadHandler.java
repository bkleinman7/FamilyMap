package kbrent.FMSDesign.Server;

/*
 * Created by kbrent on 02/09/19.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.Result.Response;
import kbrent.FMSDesign.Service.ClearService;
import kbrent.FMSDesign.Service.LoadService;

/**
 * Parses through three arrays, user, person and events
 * Then it tries to add them to the database if they are valid
 */
public class LoadHandler implements HttpHandler {

    private Response response = new Response();
    private ClearService clearService = new ClearService();
    private JsonObject jsonObject = new JsonObject();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        clearService.clear();

        int headerVal;

        //Get JSON string
        InputStreamReader isr =  new InputStreamReader(httpExchange.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder sb = new StringBuilder();
        while ((b = br.read()) != -1) {
            sb.append((char) b);
        }
        LoadService loadService = new LoadService();

        Integer[] intArr = new Integer[3];
        try {
            intArr = loadService.loadJson(sb.toString());
            headerVal = 200;
        } catch (DataAccessException e) {
            headerVal = 400;
            e.printStackTrace();
        }

        //if(intArr != null) {
        int numUsers = intArr[0];
        int numPeople = intArr[1];
        int numEvents = intArr[2];
        jsonObject.addProperty("message", "Added " + numUsers + " users, "
                + numPeople + " persons, " + "and " + numEvents + " events to the database.");

        response.sendMessage(jsonObject, httpExchange, headerVal);
    }
}
