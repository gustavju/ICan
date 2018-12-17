import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TrashCanCallBack implements MqttCallback {
    private Trashcan trashcan;


    public TrashCanCallBack(Trashcan trashcan) {
        this.trashcan = trashcan;
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Message received:\n\t" + message);
        if (topic.equals("trashcanDiscovery")) {
            trashcan.mqttClient.sendMessage("trashcanDiscoveryResponse", trashcan.toJSON());
        } else {
            takeAction(message);
        }
    }

    private void takeAction(String message) {
        switch (message) {
            case "addTrash":
                trashcan.addTrash();
                break;
            case "empty":
                trashcan.empty();
                break;
            case "startTrashFire":
                trashcan.startTrashFire();
                break;
            case "readTemperature":
                trashcan.readTemperature();
                break;
            case "booked":
                trashcan.changeStatus(CanStatus.PICKUPPENDING);
                break;
            case "getHistoryEntry":
                trashcan.mqttClient.sendMessage(trashcan.trashcanId,trashcan.trashcanHistoryEntryJSON());
                break;

        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
