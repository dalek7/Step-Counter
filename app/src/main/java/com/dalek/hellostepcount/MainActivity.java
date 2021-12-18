package com.dalek.hellostepcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView mTextView1;
    private SensorManager mSensorManager;
    private Sensor stepSensor;
    private int mStepCountInit;
    private int mStepCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mTextView1 = (TextView) findViewById(R.id.textView1);


        mStepCountInit = -1;
        mStepCount = 0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepSensor == null){
            Toast.makeText(this,"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
            mTextView1.setText(":-(");
        }
        else {

            mSensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_GAME);

            mTextView1.setText("Ready...");
        }

        //mTextView1.setText(String.format("%d", mStepCountInit));
        checkAndRequestPermissions(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){

            if (mStepCountInit < 1) {
                mStepCountInit = (int) event.values[0];
            }

            mStepCount = (int) event.values[0] - mStepCountInit;
            mTextView1.setText(Integer.toString(mStepCount));
        }

    }

    public void onStart() {
        super.onStart();
        if(stepSensor !=null){
            mSensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onStop(){
        super.onStop();
        if(stepSensor!=null){
            mSensorManager.unregisterListener(this);
        }
    }

    //#define EPSILON    (1.0E-8)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // https://stackoverflow.com/a/35495855
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public void checkAndRequestPermissions(Activity activity)
    {

        int stepPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (stepPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);

        }

    }



}