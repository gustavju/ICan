import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class GarbageTruckCallback implements MqttCallback {
    private GarbageTruck garbageTruck;

    public GarbageTruckCallback(GarbageTruck garbageTruck) {
        this.garbageTruck = garbageTruck;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Message received:\n\t" + message);
        takeAction(message);

    }

    private void takeAction(String message) {
        switch (message) {
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
