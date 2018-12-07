public class Trashcan {
    private Location location;
    private TrashLevel trashLevelSensor;
    private Temperature temperatureSensor;
    private final int MAX_CAPACITY = 100;
    private CanStatus canStatus = CanStatus.EMPTY;

    public Trashcan(Location location) {
        this.location = location;
        temperatureSensor = new Temperature("Id", 10);
        trashLevelSensor = new TrashLevel("Id", 10);
    }

    public Trashcan() {
    }

    public void changeStatus(CanStatus canStatus) {
        this.canStatus = canStatus;

    }


}
