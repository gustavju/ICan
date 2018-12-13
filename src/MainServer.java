import java.util.ArrayList;

public class MainServer {

    private String hostName;
    private String serverName;
    private String password;
    protected MQTTClient mqttClient;
    private ArrayList<Trashcan> trashcans;

    public MainServer(String host, String server, String pass) {
        hostName = host;
        serverName = server;
        password = pass;
        String[] subs = {"discoveryResponse"};
        mqttClient = new MQTTClient("server", new ServerCallback(this), subs);
    }

    public String getHostName() {
        return hostName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getPassword() {
        return password;
    }


}
