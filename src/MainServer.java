public class MainServer {

    private String hostName;
    private String serverName;
    private String password;


    public MainServer(String host, String server, String pass) {
        hostName = host;
        serverName = server;
        password = pass;
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
