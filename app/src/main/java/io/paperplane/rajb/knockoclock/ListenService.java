package io.paperplane.rajb.knockoclock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by raj on 11/11/16.
 */
public class ListenService extends Service implements SensorEventListener {

    public volatile boolean spikeDetected = false;
    private SensorManager mSensorManager;
    final public float thresholdZ = 3;
    final public float threshholdX = 6;
    final public float threshholdY = 6;
    final public int updateFrequency = 100;
    private Context mContext;
    private static TextToSpeech t1;
    private ServiceConnection sc;


    private float prevZVal = 0;
    private float currentZVal = 0;
    private float diffZ = 0;

    private float prevXVal = 0;
    private float currentXVal = 0;
    private float diffX = 0;

    private float prevYVal = 0;
    private float currentYVal = 0;
    private float diffY = 0;


    public static void readTime(){
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
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }


    public void stopAccSensing(){
        mSensorManager.unregisterListener(this);
    }

    public void resumeAccSensing(){
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 1000000/updateFrequency);
    }

    public void onSensorChanged(SensorEvent event) {
        prevXVal = currentXVal;
        currentXVal = abs(event.values[0]); // X-axis
        diffX = currentXVal - prevXVal;

        prevYVal = currentYVal;
        currentYVal = abs(event.values[1]); // Y-axis
        diffY = currentYVal - prevYVal;

        prevZVal = currentZVal;
        currentZVal = abs(event.values[2]); // Z-axis
        diffZ = currentZVal - prevZVal;

        //Z force must be above some limit, the other forces below some limit to filter out shaking motions
        if (currentZVal > prevZVal && diffZ > thresholdZ && diffX < threshholdX && diffY < threshholdY && currentXVal < 1 && currentYVal < 1){
            accTapEvent();
        }

    }

    private void accTapEvent(){
        Log.d("acceltap","single tap event detected!");
        spikeDetected = true;
        readTime();
        NotificationListener nl = new NotificationListener();
        NotificationListener.NLServiceReceiver nls = new NotificationListener.NLServiceReceiver();
        Log.d("DEBUG", nl.getActiveNotifications()+"");

    }

    private float abs(float f) {
        if (f<0){
            return -f;
        }
        return f;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
        mSensorManager = MainActivity.sm;
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        resumeAccSensing();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAccSensing();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


