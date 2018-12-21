import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        }else{
            takeAction(topic,message);
        }
    }


    private void takeAction(String topic,String message) {
        //String splitMessage = message.split("Message:" )[1];
        String[] splitMessage = message.split(":");
        if(splitMessage[0].equals("trashLevel")){
            String level = splitMessage[1];
                garbageTruck.fillTruck(Double.parseDouble(level));
                garbageTruck.removeFromRoute(topic);
                return;
        }
        if(message.contains("route")) {

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
                }

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
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
