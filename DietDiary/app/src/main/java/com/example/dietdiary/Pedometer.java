package com.example.dietdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Pedometer extends AppCompatActivity implements SensorEventListener {
    ImageView imgRun;
    Button restBtn;
    TextView stepCountView;
    SensorManager sensorManager;
    Sensor stepCountSensor;
    int currentSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        imgRun=(ImageView) findViewById(R.id.ImgRun);
        imgRun.setImageResource(R.drawable.run);
        restBtn=(Button) findViewById(R.id.resetButton);
        stepCountView =(TextView) findViewById(R.id.stepCountView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        restBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                currentSteps=0;
                stepCountView.setText(String.valueOf(currentSteps));
                return false;
            }
        });

    }

    public void onStart() {
        super.onStart();
        stepCountView.setText(String.valueOf(currentSteps));
        if(stepCountSensor !=null) {
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                currentSteps++;
                stepCountView.setText(String.valueOf(currentSteps));
                MyGlobalClass.getInstance().setData(currentSteps);
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}