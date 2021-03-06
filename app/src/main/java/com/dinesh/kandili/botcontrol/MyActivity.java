package com.dinesh.kandili.botcontrol;

import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MyActivity extends AppCompatActivity {

    boolean fanstate,lightstate;
    String FAN_STATE;
    String LIGHT_STATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        LoadPreferences();

        final Switch s1;
        final Switch s2;

        s1=(Switch) findViewById(R.id.myfan);
        s2=(Switch)  findViewById(R.id.mylight);

        if(savedInstanceState!=null)
        {
            fanstate= savedInstanceState.getBoolean(FAN_STATE);
            s1.setChecked(fanstate);
            lightstate=savedInstanceState.getBoolean(LIGHT_STATE);
            s2.setChecked(lightstate);
        }

        final String clientId="dinesh";
        final MqttAndroidClient client =
                new MqttAndroidClient(MyActivity.this, "tcp://172.16.73.4:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Toast.makeText(MyActivity.this,"Device List",Toast.LENGTH_SHORT).show();

                    s1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String topic = "devinp";
                            String payload = null;
                            if (s1.isChecked())
                            {
                                payload = "my/fan/on";
                            }
                            if (!s1.isChecked())
                            {
                                payload = "my/fan/off";
                            }
                            byte[] encodedPayload = new byte[0];
                            try {
                                encodedPayload = payload.getBytes("UTF-8");
                                MqttMessage message = new MqttMessage(encodedPayload);
                                client.publish(topic, message);
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    s2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String topic = "devinp";
                            String payload = null;
                            if (s2.isChecked())
                            {
                                payload = "my/light/on";
                            }
                            if (!s2.isChecked())
                            {
                                payload = "my/light/off";
                            }
                            byte[] encodedPayload = new byte[0];
                            try {
                                encodedPayload = payload.getBytes("UTF-8");
                                MqttMessage message = new MqttMessage(encodedPayload);
                                client.publish(topic, message);
                            } catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MyActivity.this,"Restart the application",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Switch fan = (Switch) findViewById(R.id.myfan);
        Switch light = (Switch) findViewById(R.id.mylight);
        if(fan.isChecked()==true)
            outState.putBoolean(FAN_STATE,true);
        else
            outState.putBoolean(FAN_STATE,false);
        if(light.isChecked()==true)
            outState.putBoolean(LIGHT_STATE,true);
        else
            outState.putBoolean(LIGHT_STATE,false);

    }

    private void SavePreferences(){
        Switch fan = (Switch) findViewById(R.id.myfan);
        Switch light = (Switch) findViewById(R.id.mylight);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("fanstate", fan.isChecked());
        editor.putBoolean("lightstate", light.isChecked());
        editor.commit();   // I missed to save the data to preference here,.
    }

    private void LoadPreferences(){
        Switch fan = (Switch) findViewById(R.id.myfan);
        Switch light = (Switch) findViewById(R.id.mylight);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean  fanstate = sharedPreferences.getBoolean("fanstate", false);
        fan.setChecked(fanstate);
        Boolean  lightstate = sharedPreferences.getBoolean("lightstate", false);
        light.setChecked(lightstate);
    }

    @Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }
}
