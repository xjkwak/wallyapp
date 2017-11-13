package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Chronometer chronometer = (Chronometer)findViewById(R.id.chronometer);
        chronometer.start();
    }

    public void addPoints(View view) {
        Button button = (Button)view;
        String value = button.getText().toString();
        int intValue = Integer.parseInt(value);
        intValue++;
        button.setText(String.valueOf(intValue));
        System.out.println("Valor:  " + value);


    }
}
