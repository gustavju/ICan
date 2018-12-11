
import java.util.ArrayList;
import java.util.List;


public class GarbageTruck {

    private double gasLevel;
    private final double MAX_CAPACITY= 100;
    private List<Trashcan> route;
    private Location location;
    //private MainServer mainserver;
    private double capacity;
    private MQTTClient mqttClient;

    public GarbageTruck(double gasLevel, Location location){

        this.gasLevel = gasLevel;
        this.location = location;
        capacity = 0;
        String[] subs ={"Trash1","Trash2","Trash3","Central"};
        mqttClient = new MQTTClient("GarbageTruck",subs);


    }

    public void setLocation(Location location){
        this.location=location;
    }

    public void setRoute(Trashcan trashcan){

        if((capacity+trashcan.getLevel())>=MAX_CAPACITY*0.8){
            System.out.println("Error: Route is full");
            return;
        }

        route.add(trashcan);
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

    public void sendMessage(String message){
        mqttClient.sendMessage("GarbageTruck",message);
    }

    public List<Trashcan> getRoute(){
        return route;
    }

    public void emptyTruck(){
        route.clear();
    }

    public void fillTank(){
        gasLevel = 100;
    }

    public double getGasLevel(){
        return gasLevel;
    }


}
