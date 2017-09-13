package ekyc.itsutra.ekyc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Act_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void evt_form(View view) {
        Intent i =  new Intent(Act_Main.this,Act_Form.class);
        startActivity(i);

    }

    public void evt_config(View view) {
        Intent i =  new Intent(Act_Main.this,Act_Config.class);
        startActivity(i);
    }
}
