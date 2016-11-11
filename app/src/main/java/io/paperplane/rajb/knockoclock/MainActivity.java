package io.paperplane.rajb.knockoclock;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static TextToSpeech t1;
    public static SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    /*public static void readTime(){
        //Toast.makeText(getApplicationContext(), "handled!" ,Toast.LENGTH_LONG).show();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        Log.d("DEBUG", hour + ":" + minute);
        String toSpeak;
        String ampm;
        if(hour - 12 > 0){
            hour -= 12;
            ampm = "P M";
        }else{
            ampm = "A M";
        }
        if(minute < 10 && minute != 0){
            toSpeak = "The time is " + hour + " o " + minute + " " + ampm;
        }else if(minute == 0){
            toSpeak = "The time is " + hour + " o clock" + " " + ampm;
        }
        else{
            toSpeak = "The time is " + hour + " " + minute + " " + ampm;
        }
        //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }*/

    @Override
    public void onPause(){
        super.onPause();
       // dk.stopAccSensing();
    }

    @Override
    public void onResume(){
        super.onResume();
      //  dk.resumeAccSensing();
    }

    public void run(View v){
        Intent intent = new Intent(this, ListenService.class);
        startService(intent);
    }

    public void stop(View v){
        Intent intent = new Intent(this, ListenService.class);
        stopService(intent);
    }

}
