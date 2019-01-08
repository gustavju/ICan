import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HouseholdCan extends Trashcan {
    private double hygieneLevel;

    // protected final double MAX_LEVEL_HYGIENE = 1400; lowered threshold for demo
    protected final double MAX_LEVEL_HYGIENE = 140;
    private List<Double> temperaturesSincePickup;

    public HouseholdCan(Location location) {
        super(location, true);
        temperaturesSincePickup = new ArrayList<>();

    }

    private double calculateHygieneLevel() {
        double hygiene = 0.0;
        if (canStatus == CanStatus.NORMAL) {
            long dif = Math.abs(new Date().getTime() - lastEmptied.getTime());
            // int hours = (int) TimeUnit.HOURS.convert(dif, TimeUnit.MILLISECONDS); changed from hours to seconds for easy demo
            int hours = (int) TimeUnit.SECONDS.convert(dif, TimeUnit.MILLISECONDS);
            hygiene = hours * avgTemperature();
        }
        return hygiene;
    }

    protected void addTemperature(double temperature) {
        temperaturesSincePickup.add(temperature);
    }

    public void updatePickup() {
        this.lastEmptied = new Date();
        temperaturesSincePickup.clear();
    }

    public boolean nonHygienic() {
        return calculateHygieneLevel() >= MAX_LEVEL_HYGIENE;
    }

    private double avgTemperature() {
        double sum = 0.0;
        for (Double temperature : temperaturesSincePickup) {
            sum += temperature;
        }
        return sum / temperaturesSincePickup.size();
    }

    @Override
    public void specificCalc() {
        if (nonHygienic())
            canStatus = CanStatus.NONHYGENIC;
    }

    @Override
    public String typeAsJson() {
        return "\"type\":\"household\"";

    }

    @Override
    public String toString() {
        return "{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }
}
