import java.util.Random;

public class TemperatureSensor extends Sensor {

    private double temperature;

    TemperatureSensor(String Id, double temperature) {
        super(Id);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void fakeTemperature() {
        Random rand = new Random();
        int temp = 1 + rand.nextInt(60) - 30;
        temperature = temp;
    }

    public void startTrashFire() {
        temperature = 666;
    }

}
