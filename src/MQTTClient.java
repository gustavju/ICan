import org.eclipse.paho.client.mqttv3.*;
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

    public MQTTClient(String clientId, MqttCallback callback, String[] subscribeTopics) {
        this.clientId = clientId;
        Properties mqttProps = loadMQTTProperties();
        username = mqttProps.getProperty("mqttusername");
        password = mqttProps.getProperty("mqttpassword");
        broker = mqttProps.getProperty("mqttbroker");
        mqttClient = setupMQTTClient(subscribeTopics, callback);
    }

    public void sendMessage(String topic, String content) {
        //String con = String.format(clientId, content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }

    }

    private MqttClient setupMQTTClient(String[] subscribeTopics, MqttCallback callback) {
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(username);
        connOpts.setPassword(password.toCharArray());
        connOpts.setCleanSession(true);
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.connect(connOpts);
            mqttClient.subscribe(subscribeTopics);
            mqttClient.setCallback(callback);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }
        System.out.println("New mqttClient with id:" + mqttClient.getClientId());
        return mqttClient;
    }

    public void subscribe(String sub) {
        try {
            mqttClient.subscribe(sub);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }
    }

    public void subscribe(String[] sub) {
        try {
            mqttClient.subscribe(sub);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }
    }

    public void unsubscribe(String unsub) {
        try {
            mqttClient.unsubscribe(unsub);
        } catch (MqttException me) {
            System.out.println(String.format("Errorcode: %s, Message: %s", me.getReasonCode(), me.getMessage()));
        }
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
