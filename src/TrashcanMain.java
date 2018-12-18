import java.util.Timer;
import java.util.TimerTask;

public class TrashcanMain {

    private void run(double longitude, double latitude) {
        HouseholdCan trashcan = new HouseholdCan(new Location(longitude, latitude));
        // fake sensor changes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                trashcan.addTrash();
                trashcan.readTemperature();
                trashcan.mqttClient.sendMessage(trashcan.getTrashcanId(), "{ \"action\":\"getHistoryEntryResponse\", \"data\": " + trashcan.trashcanHistoryEntryJSON() + " }");
            }
        }, 0, 20000);
    }

    public static void main(String[] args) {
        double longitude;
        double latitude;
        if (args.length == 0) {
            longitude = 0.2;
            latitude = 0.2;
        } else {
            longitude = Double.parseDouble(args[0]);
            latitude = Double.parseDouble(args[1]);
        }
        new TrashcanMain().run(longitude, latitude);
    }
}
