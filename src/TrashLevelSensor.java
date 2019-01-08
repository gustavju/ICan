import java.util.Random;

public class TrashLevelSensor extends Sensor {
    private final int MAX_CAPACITY;

    private double level;

    TrashLevelSensor(String Id, float level, final int MAX_CAPACITY) {
        super(Id);
        this.level = level;
        this.MAX_CAPACITY = MAX_CAPACITY;
    }

    public double getLevel() {
        return level;
    }

    public void empty() {
        level = 0;
    }

    public void addTrash() {
        Random rand = new Random();
        int trashVolume = 1 + rand.nextInt(40);
        double newTrashLevel = trashVolume + level;
        if (newTrashLevel > MAX_CAPACITY) {
            level = MAX_CAPACITY;
        } else {
            level = newTrashLevel;
        }
    }

}
