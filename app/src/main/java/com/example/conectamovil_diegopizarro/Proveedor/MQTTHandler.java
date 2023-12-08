package com.example.conectamovil_diegopizarro.Proveedor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MQTTHandler {
    private MqttClient client;
    public void connect(String brokerUrl, String clientId){
        try{
            MemoryPersistence persistence = new MemoryPersistence();
            client = new MqttClient(brokerUrl,clientId, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            client.connect(connectOptions);

        }catch(MqttException e){
            e.printStackTrace();
        }
    }
    public interface MensajeListener {
        void onMensajeRecibido(String topic, String message);
    }

    private MensajeListener mensajeListener;

    public void setMensajeListener(MensajeListener listener) {
        this.mensajeListener = listener;
    }

    public void disconnect(){
        try{
            client.disconnect();
        }catch(MqttException e){
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message){
        try{
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic,mqttMessage);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void subscribe(String topic){
        try{
            client.subscribe(topic);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
