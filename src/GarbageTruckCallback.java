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
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        System.out.println("Message received:\n\t" + message);
        for(int i=0;i<garbageTruck.getRoute().size();i++){

        }
        takeAction(topic, message);

    }

    private void takeAction(String topic, String message) {
        switch (topic) {
            case "garbageTruckDiscovery":
                garbageTruck.sendMessage("discoveryGarbagetruckResponse",garbageTruck.toString());
                break;

        }
    }

    private void switchTrashcan(String message){
        switch(message.split(":")[0]){
            case "temperature":
                break;
            case "trashlevel" :
                break;
            case "canStatus" :
                break;
        }
    }


    private void switchServer(String message){
        switch(message.split(":")[0]){
            case "temperature":
                break;
            case "trashlevel" :
                break;

        }
    }




    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
