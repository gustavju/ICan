import java.util.Random;

public class Temperature extends Sensor {

    private float temperature;

    Temperature(String Id, float temperature) {
        super(Id);
        this.temperature = temperature;
    }

  public float getTemperature() {
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
