public class ElectronicsCan extends Trashcan {
    private boolean toxic;



    public ElectronicsCan(Location location) {
        super(location, true);
    }

    public boolean isToxic(){
        return temperatureSensor.getTemperature() > MAX_TEMPERATURE;
    }


    @Override
    public String toString() {
        return "{" +
                "location=" + location +
                ", trashcanId='" + trashcanId + '\'' +
                '}';
    }

    @Override
    public void specificCalc() {
       //TODO: SEND MESSAGE BRO IM TOXIC
        if (isToxic()){
            mqttClient.sendMessage(trashcanId,"toxic");
        }

    }
}
