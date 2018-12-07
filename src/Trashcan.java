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


}
