import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MQTTClient {

    private String username;
    private String password;
    private String broker;
    private String clientId;
    private final int qos = 2;
    private MqttClient mqttClient;

    public MQTTClient(String clientId, String[] subscribeTopics) {
        this.clientId = clientId;
        Properties mqttProps = loadMQTTProperties();
        username = mqttProps.getProperty("mqttusername");
        password = mqttProps.getProperty("mqttpassword");
        broker = mqttProps.getProperty("mqttbroker");
        mqttClient = setupMQTTClient(subscribeTopics);
    }

    public void sendMessage(String topic, String content) {
        String con = String.format("clientId:%s, Message:%s", clientId, content);
        MqttMessage message = new MqttMessage(con.getBytes());
        message.setQos(qos);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }

    }

    private MqttClient setupMQTTClient(String[] subscribeTopics) {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        connOpts.setCleanSession(true);
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.connect(connOpts);

            for (String subscribeTopic : subscribeTopics) {
                mqttClient.subscribe(subscribeTopic);
            }
            mqttClient.setCallback(new SimpleMqttCallBack());
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }
        return mqttClient;
    }

    private Properties loadMQTTProperties() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configPath = rootPath + "settings.properties";
        Properties mqttProps = new Properties();
        try {
            mqttProps.load(new FileInputStream(configPath));
        } catch (IOException ex) {
            System.out.println("Error" + ex.getMessage());
        }
        return mqttProps;
    }

}
