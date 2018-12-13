public class ServerMain {

    private void run() {
        MainServer server = new MainServer("", "", "");
        server.mqttClient.sendMessage("discovery", "");
    }

    public static void main(String[] args) {
        new ServerMain().run();
    }
}
