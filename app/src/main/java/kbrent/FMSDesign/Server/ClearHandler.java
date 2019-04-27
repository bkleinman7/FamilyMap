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

import java.io.IOException;

import kbrent.FMSDesign.Service.ClearService;
import kbrent.FMSDesign.Result.Response;

/**
 * This class is used to delete all data from database
 */
public class ClearHandler implements HttpHandler {

    private Response response = new Response();
    private JsonObject jsonObject = new JsonObject();
    private ClearService clearService = new ClearService();

    /**
     * There are two options, either the clear succeeded or failed
     * @param httpExchange passed in from server
     * @throws IOException Bad Practice..
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if(clearService.clear()) {
            jsonObject.addProperty("message", "Clear succeeded.");
            response.sendMessage(jsonObject, httpExchange, 200);
        }
        else {
            jsonObject.addProperty("message", "Clear failed.");
            response.sendMessage(jsonObject, httpExchange, 400);
        }

    }
}
