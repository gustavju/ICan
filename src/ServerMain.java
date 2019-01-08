import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class ServerMain {

    private void run() {
        MainServer server = new MainServer();
        server.mqttClient.sendMessage("trashcanDiscovery", "");
        server.mqttClient.sendMessage("garbagetruckDiscovery", "");

        try {
            HttpServer webServer = HttpServer.create(new InetSocketAddress(8500), 0);
            HttpContext commandContext = webServer.createContext("/trashcanCommand");
            commandContext.setHandler(new HandleCommandRequest(server));
            HttpContext routeContext = webServer.createContext("/garbagetruckRoute");
            routeContext.setHandler(new HandleRouteRequest(server));
            HttpContext getTrashcansContext = webServer.createContext("/getTrashcans");
            getTrashcansContext.setHandler(new HandleGetTrashcansRequest(server));
            HttpContext getGarbagetruckContext = webServer.createContext("/getGarbagetruck");
            getGarbagetruckContext.setHandler(new HandleGetGarbageTruckRequest(server));
            webServer.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                server.checkConnections();
            }
        }, 10000, 30000);

    }

    public static void sendResponseJSON(HttpExchange exchange, String response) {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        try {
            exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception ex) {
            System.out.println("Exeption: " + ex.getMessage());
        }
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new ServerMain().run();
    }
}

class HandleCommandRequest implements HttpHandler {
    MainServer server;

    HandleCommandRequest(MainServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> query = ServerMain.queryToMap(exchange.getRequestURI().getQuery());
        String command = query.get("command");
        String id = query.get("id");
        System.out.println(command + ":" + id);
        switch (command) {
            case "empty":
                server.mqttClient.sendMessage(id, "empty");
                break;
            case "addTrash":
                server.mqttClient.sendMessage(id, "addTrash");
                break;
            case "startTrashFire":
                server.mqttClient.sendMessage(id, "startTrashFire");
                break;
            case "booked":
                server.mqttClient.sendMessage(id, "booked");
                break;
        }
        server.mqttClient.sendMessage(id, "getHistoryEntry");
        String response = String.format("{ \"Message\": \"%s sent to trashcan with id: %s\" }", command, id);
        ServerMain.sendResponseJSON(exchange, response);
    }
}

class HandleRouteRequest implements HttpHandler {
    MainServer server;

    HandleRouteRequest(MainServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Map<String, String> query = ServerMain.queryToMap(exchange.getRequestURI().getQuery());
        String garbagetruckId = query.get("garbagetruckId");
        System.out.println(garbagetruckId);
        int trascanCansInRoute = Integer.parseInt(query.get("num"));
        List<String> trashcanIds = new ArrayList<>();
        for (int i = 0; i < trascanCansInRoute; i++) {
            String currentTrashcan = "t" + i;
            trashcanIds.add("\"" + query.get(currentTrashcan) + "\"");
        }
        System.out.println("{ \"action\": \"route\", \"data\": " + trashcanIds + " }");
        server.mqttClient.sendMessage(garbagetruckId, "{ \"action\": \"route\", \"data\": " + trashcanIds + " }");
    }
}

class HandleGetTrashcansRequest implements HttpHandler {
    MainServer server;

    HandleGetTrashcansRequest(MainServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String response = server.getTrashcanJSON();
        ServerMain.sendResponseJSON(exchange, response);
    }
}

class HandleGetGarbageTruckRequest implements HttpHandler {
    MainServer server;

    HandleGetGarbageTruckRequest(MainServer server) {
        this.server = server;
    }

    public void handle(HttpExchange exchange) {

        String response = server.getGarbageTruckJSON();
        ServerMain.sendResponseJSON(exchange, response);
    }
}









