import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

    private void run() {
        MainServer server = new MainServer();
        server.mqttClient.sendMessage("trashcanDiscovery", "");
        server.mqttClient.sendMessage("garbagetruckDiscovery", "");
        /*
        server.trashcanHistories.add(new TrashcanHistory("hej-alla-barn", new Location(1.09, 1.78)));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.out.println(server.garbageTruckIds.get(0));
        server.mqttClient.sendMessage(server.garbageTruckIds.get(0), "{ action:route, data:['" + server.trashcanHistories.get(0).getTrashcanId() + "','kjehj-345-345']}");
        */
        try {
            HttpServer webServer = HttpServer.create(new InetSocketAddress(8500), 0);
            HttpContext commandContext = webServer.createContext("/trashcanCommand");
            commandContext.setHandler(new HandleCommandRequest(server));
            HttpContext getTrashcansContext = webServer.createContext("/getTrashcans");
            getTrashcansContext.setHandler(new HandleGetTrashcansRequest(server));
            HttpContext getGarbagetruckContext = webServer.createContext("/getGarbagetruck");
            getGarbagetruckContext.setHandler(new HandleGetGarbageTruckRequest(server));
            webServer.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
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
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> query = queryToMap(exchange.getRequestURI().getQuery());
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


    public Map<String, String> queryToMap(String query) {
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
}

class HandleGetTrashcansRequest implements HttpHandler {
    MainServer server;

    HandleGetTrashcansRequest(MainServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = server.getTrashcanJSON();
        ServerMain.sendResponseJSON(exchange, response);
    }
}

class HandleGetGarbageTruckRequest implements HttpHandler {
    MainServer server;

    HandleGetGarbageTruckRequest(MainServer server) {
        this.server = server;
    }

    public void handle(HttpExchange exchange) throws IOException {

        String response = server.getGarbageTruckJSON();
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}









