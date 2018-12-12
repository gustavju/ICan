public class GarbageTruckMain {

    private void run(String clientId, double longitude, double latitude) {
        GarbageTruck garbageTruck = new GarbageTruck(100, new Location(longitude, latitude));
        String[] subs = {clientId, "client2", "client3", "server"};
        MQTTClient mqttClient = new MQTTClient("GarbageTruck", new GarbageTruckCallback(garbageTruck), subs);

    }
    public static void main(String [] args){
        String clientId;
        double longitude;
        double latitude;
        if (args.length == 0) {
            clientId = "client1";
            longitude = 66.00;
            latitude = 66.00;
        } else {
            clientId = args[0];
            longitude = Double.parseDouble(args[1]);
            latitude = Double.parseDouble(args[2]);
        }
        new GarbageTruckMain().run(clientId, longitude, latitude);

    }
}
