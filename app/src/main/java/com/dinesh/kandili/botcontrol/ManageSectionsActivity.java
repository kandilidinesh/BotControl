package com.dinesh.kandili.botcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.UnsupportedEncodingException;

public class ManageSectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sections);
/*
        String topic = "foo/bar";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            //client.publish(topic, message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }
}
