public class Trashcan {
    private Location location;
    private TrashLevelSensor trashLevelSensor;
    private TemperatureSensor temperatureSensor;
    private final int MAX_CAPACITY = 100;
    private CanStatus canStatus = CanStatus.EMPTY;

    public Trashcan(Location location) {
        this.location = location;
        temperatureSensor = new TemperatureSensor("Id", 10);
        trashLevelSensor = new TrashLevelSensor("Id", 10);
    }

    public Trashcan() {
    }

    public void changeStatus(CanStatus canStatus) {
        this.canStatus = canStatus;

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
    }

    public void startTrashFire() {
        temperatureSensor.startTrashFire();
    }

    private void calculateCanStatus() {
        double currentLevel = trashLevelSensor.getLevel();
        if (currentLevel== 0) {
            canStatus = CanStatus.EMPTY;
        } else if (currentLevel < 90) {
            canStatus = CanStatus.NORMAL;
        } else {
            canStatus = CanStatus.FULL;
        }
    }

}
