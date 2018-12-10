import java.util.Random;

public class TrashLevelSensor extends Sensor {

    private double level;

    TrashLevelSensor(String Id, float level) {
        super(Id);
        this.level = level;
    }

    public double getLevel() {
        return level;
    }

    public void empty() {
        level = 0;
    }

    public void fill() {
        level = 100;
    }

    public void addTrash() {
        Random rand = new Random();
        int trashVolume = 1 + rand.nextInt(40);
        double newTrashLevel = trashVolume + level;
        if (newTrashLevel > 100) {
            level = 100;
        } else {
            level = newTrashLevel;
        }
    }

}
