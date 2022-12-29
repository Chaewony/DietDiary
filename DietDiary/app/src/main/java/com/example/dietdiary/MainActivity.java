package com.example.dietdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;


@SuppressWarnings("deprecation")

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec spec;
        Intent intent; //객체

        intent = new Intent().setClass(this, Pedometer.class);
        spec = tabHost.newTabSpec("Pedometer");
        spec.setIndicator("만보계");
        spec.setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Calendar.class);
        spec = tabHost.newTabSpec("Calendar");
        spec.setIndicator("달력");
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

}