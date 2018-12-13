public class ServerMain {

    private void run() {
        MainServer server = new MainServer();
        server.mqttClient.sendMessage("trashcanDiscovery", "");
        server.mqttClient.sendMessage("garbagetruckDiscovery", "");
        server.trashcanHistories.add(new TrashcanHistory("hej-alla-barn", new Location(1.09, 1.78)));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        System.out.println(server.garbageTruckIds.get(0));
        server.mqttClient.sendMessage(server.garbageTruckIds.get(0), "{ action:route, data:['" + server.trashcanHistories.get(0).getTrashcanId() + "','kjehj-345-345']}");

    }

    public static void main(String[] args) {
        new ServerMain().run();
    }
}
