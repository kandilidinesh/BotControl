package com.dinesh.kandili.botcontrol;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;

public class SectionsActivity extends AppCompatActivity {

    final AIConfiguration config = new AIConfiguration("CLIENT_ACCESS_TOKEN",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        AIService aiService = AIService.getService(this, config);


        t1 =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if(status != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        HashMap<String, String> hash = new HashMap<String, String>();
        hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,String.valueOf(AudioManager.STREAM_NOTIFICATION));
        
        CardView ihall, iharsh, irajib, imy;
        /*ihall=(CardView) findViewById(R.id.hall);*/
        iharsh = (CardView) findViewById(R.id.harsharoom);
        irajib = (CardView) findViewById(R.id.rajibroom);
        imy = (CardView) findViewById(R.id.myroom);
        /*ihall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SectionsActivity.this, HallActivity.class);
                startActivity(i);
            }
        });*/
        iharsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SectionsActivity.this, HarshActivity.class);
                startActivity(i);
            }
        });
        irajib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SectionsActivity.this, RajActivity.class);
                startActivity(i);
            }
        });
        imy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SectionsActivity.this, MyActivity.class);
                startActivity(i);
            }
        });

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.floatingActionButton) {
            promptSpeechInput();

        }
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(SectionsActivity.this, "Sorry ! Your device doesn't support language.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i)
    {
        super.onActivityResult(request_code, result_code, i);
        switch (request_code)
        {
            case 100:
                if (result_code == RESULT_OK && i != null)
                {
                    final ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(SectionsActivity.this, result.get(0), Toast.LENGTH_LONG).show();
                    DatabaseReference ref;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    ref = database.getReference("message");
                    ref.setValue(result.get(0));
                    DatabaseReference myRef = database.getReference("response");
                    myRef.addValueEventListener(new ValueEventListener()
                    {
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Toast.makeText(SectionsActivity.this, value, Toast.LENGTH_LONG).show();
                            t1.speak(value.toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Toast.makeText(SectionsActivity.this, "Failed to read", Toast.LENGTH_LONG).show();
                        }
                    });

                    final String clientId = "dinesh";
                    final MqttAndroidClient client =
                            new MqttAndroidClient(SectionsActivity.this, "tcp://172.16.73.4:1883", clientId);
                    try {
                        IMqttToken token = client.connect();
                        token.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // We are connected
                                //Toast.makeText(RajActivity.this,"Device List",Toast.LENGTH_SHORT).show();
                                String topic = "devinp";
                                String payload = result.get(0);
                                byte[] encodedPayload = new byte[0];
                                try {
                                    encodedPayload = payload.getBytes("UTF-8");
                                    MqttMessage message = new MqttMessage(encodedPayload);
                                    client.publish(topic, message);
                                } catch (UnsupportedEncodingException | MqttException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                // Something went wrong e.g. connection timeout or firewall problems
                                Toast.makeText(SectionsActivity.this, "Restart the application", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }
}