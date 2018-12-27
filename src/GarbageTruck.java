
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GarbageTruck {

    private final double MAX_CAPACITY = 100;
    private List<String> route;
    private Location location;
    private double capacity;
    protected MQTTClient mqttClient;
    private String garbageTruckId;

    public GarbageTruck(Location location) {
        garbageTruckId = UUID.randomUUID().toString();
        this.location = location;
        capacity = 0;
        String[] subs = {garbageTruckId, "garbagetruckDiscovery"};
        mqttClient = new MQTTClient(garbageTruckId, new GarbageTruckCallback(this), subs);
        mqttClient.sendMessage("garbagetruckDiscovery", this.toString());
        route = new ArrayList<String>();
    }

    public GarbageTruck(String id, Location location, double capacity){
        garbageTruckId = id;
        this.location = location;
        this.capacity = capacity;
        //Kan behövas sättas till null
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addTrashcan(String trashcanID) {
        route.add(trashcanID);
        mqttClient.subscribe(trashcanID);
    }


    public void removeFromRoute(String trashCanId){
        route.remove(trashCanId);
        mqttClient.unsubscribe(trashCanId);

    }

    public void fillTruck(double trashLevel) {
        if ((capacity + trashLevel / 10) > MAX_CAPACITY) {
            mqttClient.sendMessage(garbageTruckId,"Warning: Garbage Truck is FULL");
        }

        if ((capacity + trashLevel / 10) > MAX_CAPACITY * 0.9) {
           mqttClient.sendMessage(garbageTruckId,"Warning: Garbage Truck almost Full");
            capacity += trashLevel / 10;
        } else {
            capacity += trashLevel / 10;
        }
        System.out.println(capacity);
    }

    public String getGarbageTruckId() {
        return garbageTruckId;
    }

    public String toJson() {
        return
                "{\"garbageTruckId\":" + "\"" + garbageTruckId + "\"" +
                        ",\"location\":" + location.toJSON() +
                        ",\"capacity\":" + "\"" + capacity + "\"}";
    }

    public String getGarbagetruckStatusUpdate() {
        return "{ \"action\": \"getGarbagetruckStatusUpdate\", \"data\": " + this.toJson() + "}";
    }

    public List<String> getRoute() {
        return route;
    }

    /*public void emptyTruck() {

        route.clear();
    }*/

    public void emptyTrashcan(String id){
        if(route.contains(id)){
            mqttClient.sendMessage(id,"empty");
            //todo: Koppla så att Trucken fylls med det som finns i soptunnan.


            route.remove(id);
        }
    }
    @Override
    public String toString() {
        return "{" +
                "garbageTruckId=" + garbageTruckId +
                '}';
    }


    @Override
    public boolean equals(Object obj) {
        return ((GarbageTruck) obj).getGarbageTruckId().equals(this.garbageTruckId);
    }
}
