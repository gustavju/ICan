import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        switch (topic) {
            case "discoveryResponse":
                handleDiscoveryResponse(message);
            case "Trashcan1":

                //switchTrashcan(message, trashcans.get(0));
                break;
            case "Trashcan2":
                //switchTrashcan(message, trashcans.get(1));
                break;
            case "Trashcan3":
                //switchTrashcan(message, trashcans.get(2));
                break;
            case "GarbageTruck":
                break;
        }
        System.out.println("Message received:\n\t" + message);
    }

    private void handleDiscoveryResponse(String message) {
        System.out.println(message);
        try {
            JSONObject trashcanJson = new JSONObject(message);
            Location location = new Location(
                    trashcanJson.getJSONObject("Location").getDouble("longitude"),
                    trashcanJson.getJSONObject("Location").getDouble("latitude")
            );
            String trashCanId = trashcanJson.getString("trashcanId");
            System.out.println(location + " : " + trashCanId);
        } catch (JSONException ex) {
            System.out.println("Message:" + ex.getMessage());
        }
        // Trashcan{location=Location{longitude=0.2, latitude=0.2}, trashcanId='71b80dfe-ef58-4759-b095-c1fe02f27998'}


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
