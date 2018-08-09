package com.dinesh.kandili.botcontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login_Page extends AppCompatActivity {

    EditText name;
    EditText pass;
    TextView iac;
    Button b;
    int count = 5;
    Toast to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        name = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        //iac = (TextView) findViewById(R.id.ia);
        b = (Button) findViewById(R.id.logbutton);

        //iac.setText("Number of attempts remaining: 5");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(name.getText().toString(), pass.getText().toString());
            }
        });
    }

    private void validate(String username, String password) {
        if (username.equals("Admin") && password.equals("1234"))
        {
            Intent i = new Intent(Login_Page.this, UserDashboard.class);
            startActivity(i);
        } /*else {
            count--;
            iac.setText("Number of attempts remaining: " + count);
            if (count == 0) {
                b.setEnabled(false);
                int sec=3;
                for (int i=1;i<4;i++) {
                    to.makeText(this, "Time remaining to unlock: " + i,Toast.LENGTH_SHORT).show();
                }
                int secs = 3; // Delay in seconds
                Utils_Delay_Function.delay(secs, new Utils_Delay_Function.DelayCallback() {
                    @Override
                    public void afterDelay() {
                        //After delay execute
                        b.setEnabled(true);
                    }
                });
            }
        }*/

    }
}
