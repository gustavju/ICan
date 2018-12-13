import java.util.UUID;

abstract public class Trashcan {

    private Location location;
    private String trashcanId;
    protected TrashLevelSensor trashLevelSensor;
    protected TemperatureSensor temperatureSensor;
    private final int MAX_CAPACITY = 100;
    protected CanStatus canStatus = CanStatus.EMPTY;
    private boolean flammable;
    protected MQTTClient mqttClient;


    public Trashcan(Location location, boolean flammable) {
        trashcanId = UUID.randomUUID().toString();
        this.location = location;
        temperatureSensor = new TemperatureSensor("Id", 10);
        trashLevelSensor = new TrashLevelSensor("Id", 10, MAX_CAPACITY);
        this.flammable = flammable;
        String[] subs = {trashcanId, "discovery"};
        mqttClient = new MQTTClient(trashcanId, new TrashCanCallBack(this), subs);
        mqttClient.sendMessage("discoveryResponse", this.toString());
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

    public void fakeTemperature() {
        temperatureSensor.fakeTemperature();
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
    public String toString() {
        return "Trashcan{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }

    private void calculateCanStatus() {
        if (this instanceof HouseholdCan) {
            HouseholdCan householdCan = (HouseholdCan) this;
            if (householdCan.nonHygienic())
                canStatus = CanStatus.NEEDPICKUP;
        }
        double currentLevel = trashLevelSensor.getLevel();
        if (currentLevel == 0) {
            canStatus = CanStatus.EMPTY;
        } else if (currentLevel < 90) {
            canStatus = CanStatus.NORMAL;
        } else {
            canStatus = CanStatus.FULL;
        }
    }

    abstract public void specificCalc();


}
