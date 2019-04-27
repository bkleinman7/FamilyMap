package kbrent.FMSDesign.Server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main server class to determine what path of handler this should route to...
 */

/*
 * Created by kbrent on 02/09/19.
 */

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private static int PORT_NUMBER;

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Need port");
        }
        else {
            PORT_NUMBER = Integer.parseInt(args[0]);
            new Server().run();
        }
    }

    private void run() {

        System.out.println("Initializing HTTP Server");

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT_NUMBER),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");

        server.createContext("/clear", new ClearHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/user", new UserHandler());
        server.createContext("/", new DefaultHandler());

        System.out.println("Starting server");

        server.start();

        System.out.println("Server started");
    }
}
