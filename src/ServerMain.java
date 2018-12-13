import org.json.JSONObject;

public class ServerMain {

    private void run() {
        MainServer server = new MainServer();
        server.mqttClient.sendMessage("trashcanDiscovery", "");
        server.mqttClient.sendMessage("garbageTruckDiscovery", "");
    }

    public static void main(String[] args) {
        new ServerMain().run();
    }
}
