package com.dinesh.kandili.botcontrol;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class UserDashboard extends AppCompatActivity {


    public String sendSms() {
        try {
            // Construct data
            String apiKey = "apikey=" + "X6bWfqqkXcE-6lBRnWfPUiJqvAyrIuFITIv7VfIQ0c\t";
            String message = "&message=" + "Dinesh, An unauthorised person has entered in your home";
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "919741370733";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        //String clientId = MqttClient.generateClientId();
        CardView cardView, cardView1;
        cardView= (CardView) findViewById(R.id.managesection);
        cardView1= (CardView) findViewById(R.id.sections);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ManageSectionsActivity.class);
                startActivity(intent);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashboard.this, SectionsActivity.class);
                startActivity(i);
            }
        });

        Toast.makeText(getApplicationContext(),"MQTT Service running in the background",Toast.LENGTH_SHORT).show();
        final String clientId="dinesh";
        final MqttAndroidClient client =
                new MqttAndroidClient(UserDashboard.this, "tcp://172.16.73.4:1883", clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Toast.makeText(MyActivity.this,"Device List",Toast.LENGTH_SHORT).show();

                    String topic = "mainface";
                    int qos = 1;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                                client.setCallback(new MqttCallback() {
                                    @Override
                                    public void connectionLost(Throwable cause) {

                                    }

                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void messageArrived(String topic, MqttMessage message) throws Exception {
//                                        Toast.makeText(getApplicationContext(),new String(message.getPayload()),Toast.LENGTH_SHORT).show();
                                        //if(Objects.equals(message.getPayload().toString(), "Unauthorised"))
                                            sendSms();
                                        /*Intent intent1;
                                        intent1 = new Intent(UserDashboard.this,openactivity.class);
                                        startActivity(intent1);*/
                                    }

                                    @Override
                                    public void deliveryComplete(IMqttDeliveryToken token) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    // Toast.makeText(MyActivity.this,"Restart the application",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
}
