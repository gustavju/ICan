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
        System.out.println(username);
        System.out.println(password);

        Trashcan t1 = new Trashcan(new Location(59.406897, 17.944906));
        Trashcan t2 = new Trashcan(new Location(59.405450, 17.956345));
        Trashcan t3 = new Trashcan(new Location(59.401946, 17.946584));

        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));

        t1.addTrash();
        t2.addTrash();
        t3.addTrash();
        t1.fakeTemperature();
        t2.fakeTemperature();
        t3.fakeTemperature();

        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));

        t1.empty();
        t2.empty();
        t3.empty();
        t1.startTrashFire();
        t2.startTrashFire();
        t3.startTrashFire();

        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan1", t1.getLevel(), t1.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan2", t2.getLevel(), t2.getTemperature()));
        System.out.println(
                String.format("%s Level:%f, Temp:%f", "Trashcan3", t3.getLevel(), t3.getTemperature()));

        String topic = "pi";
        String content = "Message from MqttPublishSample";
        int qos = 2;
        String broker = "tcp://m20.cloudmqtt.com:11706";
        String clientId = "Adam";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            char[] pass = password.toCharArray();
            connOpts.setPassword(pass);
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.setCallback( new SimpleMqttCallBack());
            sampleClient.subscribe("pi");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            //sampleClient.disconnect();
            System.out.println("Disconnected");
            //System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
