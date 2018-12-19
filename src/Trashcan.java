import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

abstract public class Trashcan {

    protected Location location;
    protected String trashcanId;
    protected TrashLevelSensor trashLevelSensor;
    protected TemperatureSensor temperatureSensor;
    private final int MAX_CAPACITY = 100;
    protected CanStatus canStatus = CanStatus.EMPTY;
    private boolean flammable;
    protected MQTTClient mqttClient;
    protected final int MAX_TEMPERATURE = 80;
    protected Date lastEmptied;




    public Trashcan(Location location, boolean flammable) {
        trashcanId = UUID.randomUUID().toString();
        this.location = location;
        temperatureSensor = new TemperatureSensor("Id", 10);
        trashLevelSensor = new TrashLevelSensor("Id", 10, MAX_CAPACITY);
        this.flammable = flammable;
        String[] subs = {trashcanId, "trashcanDiscovery"};
        mqttClient = new MQTTClient(trashcanId, new TrashCanCallBack(this), subs);
        mqttClient.sendMessage("trashcanDiscoveryResponse", this.toJSON());
        lastEmptied = new Date();
    }

    public Trashcan() {
    }

    public void changeStatus(CanStatus canStatus) {
        this.canStatus = canStatus;
    }

    public CanStatus getStatus() {
        return canStatus;
    }

    public double getTemperature() {
        return temperatureSensor.getTemperature();
    }

    public void readTemperature() {
        temperatureSensor.readTemperature();
        if (temperatureSensor.getTemperature() > MAX_TEMPERATURE && flammable)
            canStatus = CanStatus.ONFIRE;
        if (this instanceof HouseholdCan) {
            HouseholdCan householdCan = (HouseholdCan) this;
            householdCan.addTemperature(temperatureSensor.getTemperature());
        }
    }


    public double getLevel() {
        return trashLevelSensor.getLevel();
    }

    public void addTrash() {
        trashLevelSensor.addTrash();
        calculateCanStatus();
    }

    public void empty() {
        trashLevelSensor.empty();
        calculateCanStatus();
        if (this instanceof HouseholdCan) {
            HouseholdCan householdCan = (HouseholdCan) this;
            householdCan.updatePickup();
        }
    }

    public void startTrashFire() {
        temperatureSensor.startTrashFire();

    }

    public String getTrashcanId() {
        return trashcanId;
    }

    @Override
    abstract public String toString();

    public String trashcanHistoryEntryJSON() {
        return "{ \"temperature\":\"" + getTemperature() + "\", \"trashLevel\": \"" + getLevel() + "\", \"canStatus\":\"" + getStatus() + "\", \"lastEmptied\": \"" + lastEmptied.getTime() + "\"}";
    }

    public String toJSON() {
        return "{ \"trashcanId\": \"" + trashcanId + "\", \"location\": " + location.toJSON() + " }";
    }

    private void calculateCanStatus() {
        if (canStatus != CanStatus.ONFIRE) {
            double currentLevel = trashLevelSensor.getLevel();
            if (currentLevel == 0) {
                canStatus = CanStatus.EMPTY;
            } else if (currentLevel < 90) {
                canStatus = CanStatus.NORMAL;
            } else {
                canStatus = CanStatus.FULL;
            }
            specificCalc();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return ((Trashcan) obj).getTrashcanId().equals(this.trashcanId);
    }

    abstract public void specificCalc();


}
