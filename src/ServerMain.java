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
            HttpContext context = webServer.createContext("/");
            context.setHandler(ServerMain::handleRequest);
            HttpContext trashContext = webServer.createContext("/trash");
            trashContext.setHandler(ServerMain::handleTrashRequest);
            HttpContext emptyContext = webServer.createContext("/empty");
            emptyContext.setHandler(new HandleTrashRequest(server));
            webServer.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String response = "Hello There!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleTrashRequest(HttpExchange exchange) throws IOException {
        String response = "{ \"trashcans\": [{ \"id\":\"\", \"location\": {\"long\":\"\", \"lat\":\"\"}}] }";
        exchange.getResponseHeaders().set("Content-Type", "appication/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void main(String[] args) {
        new ServerMain().run();
    }
}

class HandleTrashRequest implements HttpHandler {
    MainServer server;

    HandleTrashRequest(MainServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> query = queryToMap(exchange.getRequestURI().getQuery());
        String command = query.get("command");
        String id = query.get("id");
        server.mqttClient.sendMessage("pi", "hej");
        String response = "{ \"trashcans\": [{ \"id\":\"\", \"location\": {\"long\":\"\", \"lat\":\"\"}}] }";
        exchange.getResponseHeaders().set("Content-Type", "appication/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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












