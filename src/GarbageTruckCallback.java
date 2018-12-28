import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class GarbageTruckCallback implements MqttCallback {
    private GarbageTruck garbageTruck;

    public GarbageTruckCallback(GarbageTruck garbageTruck) {
        this.garbageTruck = garbageTruck;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Message received:\n\t" + message);
        if (topic.equals("garbagetruckDiscovery")) {
            garbageTruck.mqttClient.sendMessage("garbagetruckDiscoveryResponse", garbageTruck.toJson());
        } else {
            takeAction(topic, message);
        }
    }


    private void takeAction(String topic, String message) {
        // String[] splitMessage = message.split(":");
        if (message.contains("trashLevel")) {
            try {
                JSONObject trashToEmpty = new JSONObject(message);
                garbageTruck.fillTruck(trashToEmpty.getDouble("trashLevel"));
                Location newLocation = new Location(
                        trashToEmpty.getJSONObject("location").getDouble("longitude"),
                        trashToEmpty.getJSONObject("location").getDouble("latitude")
                );
                garbageTruck.setLocation(newLocation);
                garbageTruck.removeFromRoute(topic);
                garbageTruck.mqttClient.sendMessage(garbageTruck.getGarbageTruckId(), garbageTruck.getGarbagetruckStatusUpdate());
                if (garbageTruck.getRoute().isEmpty()) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    garbageTruck.setLocation(new Location(17.938740, 59.383689));
                    garbageTruck.mqttClient.sendMessage(garbageTruck.getGarbageTruckId(), garbageTruck.getGarbagetruckStatusUpdate());
                }
            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (message.contains("route")) {

            try {
                JSONObject action = new JSONObject(message);
                System.out.println(action.getString("action"));
                switch (action.getString("action")) {

                    case "route":
                        JSONArray data = action.getJSONArray("data");
                        System.out.println(data.get(0) + " " + data.length());
                        for (int i = 0; i < data.length(); i++) {
                            String trashcan = data.get(i).toString();
                            System.out.println(trashcan);
                            garbageTruck.addTrashcan(trashcan);
                            System.out.println("Traschan: " + trashcan + " Added!");
                        }
                        for (String trashcanId : garbageTruck.getRoute()) {
                            garbageTruck.mqttClient.sendMessage(trashcanId, "empty");
                        }
                }

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }


    private void doRoute() {

    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
