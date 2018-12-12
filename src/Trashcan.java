public class Trashcan {

    private Location location;
    protected TrashLevelSensor trashLevelSensor;
    protected TemperatureSensor temperatureSensor;
    private final int MAX_CAPACITY = 100;
    protected CanStatus canStatus = CanStatus.EMPTY;
    private boolean flammable;


    public Trashcan(Location location, boolean flammable) {
        this.location = location;
        temperatureSensor = new TemperatureSensor("Id", 10);
        trashLevelSensor = new TrashLevelSensor("Id", 10);
        this.flammable = flammable;
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

}
