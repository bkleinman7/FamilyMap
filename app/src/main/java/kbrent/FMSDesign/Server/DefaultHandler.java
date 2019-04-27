package kbrent.FMSDesign.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Scanner;

/**
 * Created by kbrent on 02/09/19.
 */

public class DefaultHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        httpExchange.sendResponseHeaders(200, 0);

        OutputStreamWriter sw = new OutputStreamWriter(httpExchange.getResponseBody());

        Scanner scan = null;

        try { scan = new Scanner(new FileReader("/Users/brent/Downloads/MyApplication2/app/src/main/java/web/index.html")); }
        catch(IOException e) { e.printStackTrace(); }

        StringBuilder sb = new StringBuilder();
        assert scan != null;
        while(scan.hasNextLine()) { sb.append(scan.nextLine()).append("\n"); }
        sw.write(sb.toString());

        scan.close();
        sw.close();
    }
}
