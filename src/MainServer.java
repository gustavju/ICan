import org.json.JSONObject;

import java.util.ArrayList;

public class MainServer {

    protected MQTTClient mqttClient;
    protected ArrayList<TrashcanHistory> trashcanHistories;
    protected ArrayList<GarbageTruck> garbageTrucks;

    public MainServer() {
        trashcanHistories = new ArrayList<TrashcanHistory>();
        garbageTrucks = new ArrayList<GarbageTruck>();
        String[] subs = {"trashcanDiscoveryResponse", "garbagetruckDiscoveryResponse"};
        mqttClient = new MQTTClient("server", new ServerCallback(this), subs);

    }

    public String getTrashcanJSON() {
        String trashcans = "[";
        for (int i = 0; i < trashcanHistories.size(); i++) {
            trashcans += trashcanHistories.get(i).toJSON();
            if (i != trashcanHistories.size() - 1) {
                trashcans += ",";
            }
        }
        System.out.println(trashcans);
        return trashcans + "]";
    }

    public String getGarbageTruckJSON() {
        String garbageTruck = "[";
        for (int i = 0; i < garbageTrucks.size(); i++) {
            garbageTruck += garbageTrucks.get(i).toJson();
            if(i != garbageTrucks.size()-1){
                garbageTruck+=",";
            }

        }
        System.out.println(garbageTruck);
        return garbageTruck +"]";
    }

    public TrashcanHistory getTrashcanHistoryById(String Id) {
        for (TrashcanHistory trashcanHistory : trashcanHistories) {
            if (trashcanHistory.getTrashcanId().equals(Id))
                return trashcanHistory;
        }
        return null;
    }

}
