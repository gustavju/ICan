import java.util.Timer;
import java.util.TimerTask;

public class TrashcanMain {

    private void run(String clientId, double longitude, double latitude) {
        Trashcan trashcan = new Trashcan(new Location(longitude, latitude));
        String[] subs = {clientId};
        MQTTClient mqttClient = new MQTTClient(clientId, new TrashCanCallBack(trashcan), subs);


        // fake sensor changes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                trashcan.addTrash();
                mqttClient.sendMessage(clientId, "trashLevel:" + trashcan.getLevel());
                trashcan.fakeTemperature();
                mqttClient.sendMessage(clientId, "temperature:" + trashcan.getTemperature());
            }
        }, 0, 30000);
    }

    public static void main(String[] args) {
        String clientId;
        double longitude;
        double latitude;
        if (args.length == 0) {
            clientId = "client1";
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
