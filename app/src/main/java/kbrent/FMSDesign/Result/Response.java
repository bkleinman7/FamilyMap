package kbrent.FMSDesign.Result;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import com.google.gson.Gson;

/*
 * Created by kbrent on 02/09/19.
 */

/**
 * This class is to make it a little easier to send a response
 */
public class Response {

    public Response(){}
    private Gson gson = new Gson();

    public void sendMessage(JsonObject jo, HttpExchange exchange, int header_val) throws IOException {

        exchange.sendResponseHeaders(header_val, 0);

        OutputStreamWriter sw = new OutputStreamWriter(exchange.getResponseBody(), Charset.forName("UTF-8"));
        String json = gson.toJson(jo);
        sw.write(json);
        sw.close();
    }

}
