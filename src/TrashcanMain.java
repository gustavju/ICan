import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TrashcanMain {
    private static ArrayList<Location> locationList = new ArrayList<Location>(){{
        add(new Location(59.407859, 17.944644));
        add(new Location(59.407296, 17.937902));
        add(new Location(59.411183, 17.942859));
        add(new Location(59.398546, 17.947002));
        add(new Location(59.401205, 17.937862));
        add(new Location(59.398338, 17.937847));
        add(new Location(59.412139, 17.973096));
    }};
    private static Random rand = new Random();
    private void run(Location location) {
        HouseholdCan trashcan = new HouseholdCan(location);

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
