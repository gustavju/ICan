public class Main {

    private void run() {
        Trashcan t1 = new Trashcan(new Location(59.406897, 17.944906));

        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));

        String[] subs = {"pi"};
        MQTTClient mqttClient = new MQTTClient("Adam", subs);
        mqttClient.sendMessage("pi", "Hej alla cloudbois!");
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
