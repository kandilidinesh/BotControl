package com.dinesh.kandili.botcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class HallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        Switch ac;
        ac=(Switch) findViewById(R.id.hallac);
       /* final String clientId="dinesh";
        final MqttAndroidClient client =
                new MqttAndroidClient(HallActivity.this, "tcp://172.16.73.4:1883", clientId);

        String topic = "devinp";
        String payload;
        if(ac.isChecked())
        {
            payload="hall/ac/on";
            byte[] encodedPayload = new byte[0];
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        }
        if(!ac.isChecked())
        {
            payload="hall/ac/off";
            byte[] encodedPayload = new byte[0];
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        }*/
    }
}
