
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GarbageTruck {

    private final double MAX_CAPACITY= 100;
    private List<String> route;
    private Location location;
    private double capacity;
    private MQTTClient mqttClient;
    private String garbageTruckId;

    public GarbageTruck(Location location){
        garbageTruckId = UUID.randomUUID().toString();
        this.location = location;
        capacity = 0;
        String[] subs ={"Trash1","Trash2","Trash3","Central"};
        SimpleMqttCallBack callback = new SimpleMqttCallBack();
        mqttClient = new MQTTClient("GarbageTruck", callback, subs);


    }

    public void setLocation(Location location){
        this.location=location;
    }

    public void setRoute(Trashcan trashcan){

        if((capacity+trashcan.getLevel())>=MAX_CAPACITY*0.8){
            System.out.println("Error: Route is full");
            return;
        }

        route.add(trashcan.getTrashcanId());
        trashcan.changeStatus(CanStatus.PICKUPPENDING);
    }

    public void fillTruck(Trashcan trashcan){
        if((capacity + trashcan.getLevel()/10)> MAX_CAPACITY){
            System.out.println("Error: Garbage Truck is Full");
        }

        if((capacity + trashcan.getLevel()/10)> MAX_CAPACITY*0.9){
            System.out.println("Warning: Garbage Truck almost Full");
            capacity += trashcan.getLevel()/10;
        }else{
            capacity += trashcan.getLevel()/10;
        }

    }

    public void sendMessage(String topic,String message){
        mqttClient.sendMessage(topic,message);
    }

    public List<String> getRoute(){
        return route;
    }

    public void emptyTruck(){
        route.clear();
    }

    @Override
    public String toString() {
        return "{" +
            "garbageTruckId='" + garbageTruckId + '\'' +
            '}';
    }
}
