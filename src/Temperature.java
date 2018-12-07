public class Temperature extends Sensor {

    private float temperature;

    Temperature(String Id, float temperature) {
        super(Id);
        this.temperature = temperature;
    }

    float getTemperature() {
        return temperature;
    }
}
