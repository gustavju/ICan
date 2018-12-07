
import java.util.ArrayList;
import java.util.List;


public class GarbageTruck {

    private double gasLevel;
    private final double MAX_CAPACITY= 100;
    private List<Trashcan> route = new ArrayList();
    private Location location;
    private MainServer mainserver;


    public GarbageTruck(double gasLevel, Location location ){

        this.gasLevel = gasLevel;
        this.location = location;
        // mainserver = new MainServer(Ip,password,server)
    }

    public void setLocation(Location location){
        this.location=location;
    }

    public void setRoute(Trashcan trashcan){
        if(route.size()== MAX_CAPACITY){
            System.out.println("Garbage Truck is Full");
            return;
        }
        route.add(trashcan);
    }

    public List<Trashcan> getRoute(){
        return route;
    }

    public void fillTank(){
        gasLevel = 100;
    }

    public double getGasLevel(){
        return gasLevel;
    }
}