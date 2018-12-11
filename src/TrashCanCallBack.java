import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TrashCanCallBack implements MqttCallback {
    private Trashcan trashcan;
    private String message;

    public TrashCanCallBack(Trashcan trashcan){
        this.trashcan = trashcan;
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        message = new String(mqttMessage.getPayload());
        System.out.println("Message received:\n\t"+ message);
        takeAction(message);

    }

    private void takeAction(String message){
        switch (message){
            case "booked":
                trashcan.changeStatus(CanStatus.PICKUPPENDING);
                System.out.println("Pickuppending");
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
