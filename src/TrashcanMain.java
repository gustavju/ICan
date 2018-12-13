import java.util.Timer;
import java.util.TimerTask;

public class TrashcanMain {

    private void run(String clientId, double longitude, double latitude) {
        HouseholdCan trashcan = new HouseholdCan(new Location(longitude, latitude));
        // fake sensor changes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                trashcan.addTrash();
                trashcan.mqttClient.sendMessage(clientId, "trashLevel:" + trashcan.getLevel());
                trashcan.readTemperature();
                trashcan.mqttClient.sendMessage(clientId, "temperature:" + trashcan.getTemperature());
            }
        }, 0, 20000);
    }

    public static void main(String[] args) {
        String clientId;
        double longitude;
        double latitude;
        if (args.length == 0) {
            clientId = "trashcan1";
            longitude = 0.2;
            latitude = 0.2;
        } else {
            clientId = args[0];
            longitude = Double.parseDouble(args[1]);
            latitude = Double.parseDouble(args[2]);
        }
        new TrashcanMain().run(clientId, longitude, latitude);
    }
}
