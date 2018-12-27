import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ServerCallback implements MqttCallback {
    private MainServer server;

    public ServerCallback(MainServer server) {
        this.server = server;
    }

    @Override
    public void connectionLost(Throwable throwable) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Message Arrived:" + message);
        switch (topic) {
            case "trashcanDiscoveryResponse":
                handleTrashcanDiscoveryResponse(message);
                break;
            case "garbagetruckDiscoveryResponse":
                handleGarbagetruckDiscoveryResponse(message);
                break;
            default:
                if (message.contains("getHistoryEntryResponse")) {
                    JSONObject jsonMessage = new JSONObject(message);
                    TrashcanHistoryEntry trashcanHistoryEntry = new TrashcanHistoryEntry(jsonMessage.getJSONObject("data"));
                    TrashcanHistory trashcanHistory = server.getTrashcanHistoryById(topic);
                    trashcanHistory.addEntry(trashcanHistoryEntry);
                    System.out.println(message);
                }
                if (message.contains("getGarbagetruckStatusUpdate")) {
                    JSONObject jsonMessage = new JSONObject(message);
                    GarbageTruck garbageTruck = parseGarbagetruckJson(jsonMessage.getJSONObject("data").toString());
                    server.garbageTrucks.remove(garbageTruck);
                    server.garbageTrucks.add(garbageTruck);
                }
                break;
        }
        System.out.println("Message received:\n\t" + message);
    }

    private void handleTrashcanDiscoveryResponse(String message) {
        System.out.println(message);
        try {
            JSONObject trashcanJson = new JSONObject(message);
            Location location = new Location(
                    trashcanJson.getJSONObject("location").getDouble("longitude"),
                    trashcanJson.getJSONObject("location").getDouble("latitude")
            );
            String trashCanId = trashcanJson.getString("trashcanId");
            String type = trashcanJson.getString("type");
            TrashcanHistory trashcanHistory = new TrashcanHistory(trashCanId, location, type);
            if (!server.trashcanHistories.contains(trashcanHistory)) {
                server.trashcanHistories.add(trashcanHistory);
                server.mqttClient.subscribe(trashCanId);
            }
            server.mqttClient.sendMessage(trashCanId, "getHistoryEntry");
        } catch (JSONException ex) {
            System.out.println("Message:" + ex.getMessage());
        }
    }

    private void handleGarbagetruckDiscoveryResponse(String message) {
        System.out.println(message);
        GarbageTruck garbageTruck = parseGarbagetruckJson(message);
        if (!server.garbageTrucks.contains(garbageTruck)) {
            server.garbageTrucks.add(garbageTruck);
            server.mqttClient.subscribe(garbageTruck.getGarbageTruckId());
        }
    }

    private GarbageTruck parseGarbagetruckJson(String message) {
        try {
            JSONObject garbagetruckJson = new JSONObject(message);
            Location location = new Location(
                    garbagetruckJson.getJSONObject("location").getDouble("longitude"),
                    garbagetruckJson.getJSONObject("location").getDouble("latitude"));
            String id = garbagetruckJson.getString("garbageTruckId");
            double capacity = Double.parseDouble(garbagetruckJson.getString("capacity"));
            return new GarbageTruck(id, location, capacity);
        } catch (Exception ex) {
            System.out.println("Message:" + ex.getMessage());
        }
        return null;
    }

    private void switchTrashcan(String message, Trashcan trashcan) {

    }

    private void switchGarbageTruck(String message) {
        switch (message) {

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
