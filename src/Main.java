import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private void run() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configPath = rootPath + "settings.properties";
        Properties mqttProps = new Properties();

        try {
            mqttProps.load(new FileInputStream(configPath));
        } catch (IOException ex) {
            System.out.println("Error" + ex.getMessage());
        }

        String username = mqttProps.getProperty("mqttusername");
        String password = mqttProps.getProperty("mqttpassword");
        String broker = mqttProps.getProperty("mqttbroker");

        Trashcan t1 = new Trashcan(new Location(59.406897, 17.944906));

        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));

        String topic = "pi";
        String content = "Message from MqttPublishSample";
        int qos = 2;
        String clientId = "Client1";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);

            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(new SimpleMqttCallBack());
            mqttClient.subscribe(topic);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            //mqttClient.disconnect();
            //System.exit(0);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message:", me.getReasonCode(), me.getMessage()));
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
