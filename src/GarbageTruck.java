
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
    }

    public void setRoute(Trashcan trashcan) {

        if ((capacity + trashcan.getLevel()) >= MAX_CAPACITY * 0.8) {
            System.out.println("Error: Route is full");
            return;
        }

        route.add(trashcan.getTrashcanId());
        trashcan.changeStatus(CanStatus.PICKUPPENDING);
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

    }

    public String getGarbageTruckId() {
        return garbageTruckId;
    }

    public String toJson() {
        return
                "{\"garbageTruckId\":" + "\"" + garbageTruckId + "\"" +
                        "\"location\":" + "\"" + location +
                        "\"" + "\"capacity\":" + "\"" + capacity + "\"";
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
