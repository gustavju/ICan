import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TrashcanMain {
    private static ArrayList<Location> locationList = new ArrayList<Location>(){{
        add(new Location(17.944644,59.407859));
        add(new Location(17.937902,59.407296));
        add(new Location(17.942859,59.411183));
        add(new Location(17.947002,59.398546));
        add(new Location(17.937862,59.401205));
        add(new Location(17.937847,59.398338));
        add(new Location(17.973096,59.412139));
    }};
    private static Random rand = new Random();
    private void run(Location location) {
        Trashcan trashcan;
        if (rand.nextBoolean())
            trashcan = new ElectronicsCan(location);
        else
            trashcan = new HouseholdCan(location);

        // fake sensor changes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                trashcan.addTrash();
                trashcan.readTemperature();
                trashcan.mqttClient.sendMessage(trashcan.getTrashcanId(), "{ \"action\":\"getHistoryEntryResponse\", \"data\": " + trashcan.trashcanHistoryEntryJSON() + " }");
            }
        }, 10000, 20000);

    }

    public static void main(String[] args) {
        double longitude;
        double latitude;
        Location location=new Location(0.2,0.2);
        if (args.length == 0) {
            int coordinates = rand.nextInt(locationList.size()-1);
            location = locationList.get(coordinates);
        } else {
            longitude = Double.parseDouble(args[0]);
            latitude = Double.parseDouble(args[1]);
        }
        new TrashcanMain().run(location);
    }
}
