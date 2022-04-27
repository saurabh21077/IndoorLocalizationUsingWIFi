package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String  TAG = "MainActivity";

    private SensorManager sensorManager;
    private Sensor magnetometerSensor, accelerometerSensor;
    private double prevDis, distance = 0;
    private int stepCount = 0;
    private double strideLength=0;

    private float[] gravity;
    private float[] geoMagneticField;
    private float azimuth;


    private ProgressBar progressBar;
    private TextView progressText;
    private int i=0;

    private EditText editText_height;
    private RadioGroup sex_radioGroup;
    private TextView textView_strideLength, textView_direction, textView_stepsCount, textView_distanceMoved, textView_accl, textView_mag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);




        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        textView_direction = findViewById(R.id.textView_directionValue);
        sex_radioGroup = (RadioGroup) findViewById(R.id.radioGroup_gender);
        sex_radioGroup.clearCheck();
        // Add the Listener to the RadioGroup
        sex_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                // Get the selected Radio Button
                RadioButton radioButton_sex = (RadioButton)radioGroup.findViewById(checkedId);
                System.out.println(radioButton_sex.getText().toString());
                editText_height = findViewById(R.id.editText_heightValue);
                textView_strideLength = findViewById(R.id.textView_strideValue);

                if(radioButton_sex.getText().toString().equalsIgnoreCase("Male")){
                    strideLength = Double.valueOf(editText_height.getText().toString()) * 0.415;
                }
                if(radioButton_sex.getText().toString().equalsIgnoreCase("Female")){
                    strideLength = Double.valueOf(editText_height.getText().toString()) * 0.413;
                }
                textView_strideLength.setText(String.valueOf(Double.valueOf(strideLength)));

            }
        });



        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
           // sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(MainActivity.this, " Accelerometer Sensor sensor started", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i(TAG,"Accelerometer Sensor Not supported");
        }

        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometerSensor != null) {
            sensorManager.registerListener(MainActivity.this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            // sensorManager.registerListener(MainActivity.this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(MainActivity.this, " Magnetometer Sensor sensor started", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i(TAG,"Magnetometer Sensor Not supported");
        }




    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Log.d(TAG, "Accelerometer:" + sensorEvent.values[0]+"  "+sensorEvent.values[1]+"  "+sensorEvent.values[2]);
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            textView_accl = findViewById(R.id.textView_acclValue);
            textView_accl.setText(Float.toString(x)+"  "+Float.toString(y)+"  "+Float.toString(z));

            gravity = sensorEvent.values;

            double currentdDis = Math.sqrt((x * x) + (y * y) + (z * z));
            double deltaDis = currentdDis - prevDis;
            prevDis = currentdDis;


            double threshold = 6.0;
            if(deltaDis > threshold) {
                stepCount++;
                distance += strideLength;
            }
//            System.out.println("Step Count = "+stepCount);
//            System.out.println("Stride "+strideLength);
//            distance = stepCount*strideLength;
//            System.out.println("distance "+distance);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // set the limitations for the numeric
                    // text under the progress bar
                    if (i <= 100) {
                        progressText.setText("" + stepCount);
                        progressBar.setProgress(i);
                        i = stepCount/3;
                        handler.postDelayed(this, 100);
                    } else {
                        handler.removeCallbacks(this);
                    }
                }
            }, 100);

            textView_stepsCount = findViewById(R.id.textView_stepsValue);
            textView_stepsCount.setText(Integer.toString(stepCount));

            textView_distanceMoved = findViewById(R.id.textView_distanceValue);
            textView_distanceMoved.setText(String.format("%.3f",distance/100000.0));
        }

        if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
          //Log.i("MAGNETOMETER", "X-axis:" + sensorEvent.values[0] + "Y-axis:" + sensorEvent.values[1]+"Z-axis:" + sensorEvent.values[2]);

            textView_mag = findViewById(R.id.textView_magValue);
            textView_mag.setText(sensorEvent.values[0] +"  "+sensorEvent.values[1]+"  "+sensorEvent.values[2]);

            geoMagneticField = sensorEvent.values;
        }

        //Direction detection
        if (gravity != null && geoMagneticField != null) {
            //R = Rotation Matrix
            float R[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, gravity, geoMagneticField)) {

                // orientation contains azimut, pitch and roll
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                //azimuth in radians
                azimuth = orientation[0];
                //azimuth in degrees
                float degree = (float)(Math.toDegrees(azimuth)+360)%360;


                if(degree < 22.5 || degree >= 337.5){
                    textView_direction.setText("North");
                }
                else if(degree >= 22.5 && degree < 67.5){
                    textView_direction.setText("North East");
                }
                else if(degree >= 67.5 && degree < 112.5){
                    textView_direction.setText("East");
                }
                else if(degree >= 112.5 && degree < 157.5){
                    textView_direction.setText("South East");
                }
                else if(degree >= 157.5 && degree < 202.5){
                    textView_direction.setText("South");
                }
                else if(degree >= 202.5 && degree < 247.5){
                    textView_direction.setText("South West");
                }
                else if(degree >= 247.5 && degree < 292.5){
                    textView_direction.setText("West");
                }
                else {
                    textView_direction.setText("North West");
                }

                //deg.setText(Float.toString(degree));
                //System.out.println("degree " + degree);
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}