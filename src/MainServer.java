import java.util.ArrayList;

public class MainServer {

    protected MQTTClient mqttClient;
    protected ArrayList<TrashcanHistory> trashcanHistories;
    protected ArrayList<String> garbageTruckIds;

    public MainServer() {
        trashcanHistories = new ArrayList<TrashcanHistory>();
        garbageTruckIds = new ArrayList<String>();
        String[] subs = {"trashcanDiscoveryResponse", "garbagetruckDiscoveryResponse"};
        mqttClient = new MQTTClient("server", new ServerCallback(this), subs);

    }
}
