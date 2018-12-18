import java.util.Random;

public class TemperatureSensor extends Sensor {

    private static Random rand = new Random();

    private double temperature;

    TemperatureSensor(String Id, double temperature) {
        super(Id);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void readTemperature() {
        temperature += 1 + rand.nextInt(4) - 2;
    }

    public void startTrashFire() {
        temperature = 666;
    }

}
