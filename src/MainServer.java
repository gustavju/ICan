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

    public String getTrashcanJSON() {
        String trashcans = "[]";
        for (int i = 0; i < trashcanHistories.size(); i++) {
            trashcans += trashcanHistories.get(i).toJSON();
            if (i != trashcanHistories.size() - 1) {
                trashcans += ",";
            }
        }
        return trashcans;
    }

}
