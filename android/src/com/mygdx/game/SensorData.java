package com.mygdx.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by yeol on 16. 2. 11.
 */
public class SensorData implements SensorEventListener {

    private Semaphore clear = new Semaphore(1);
    private float orientation[] = new float[3];
    private float acceleration[] = new float[3];
    private float oldOrientation[];
    private float oldAcceleration[];
    private float newMat[];

    private float Quat[];

    private BufferAlgo1 accelBufferAlgo;
    private BufferAlgo1 magnetBufferAlgo;

    private ArrayList<HeadRotationInterface> RotationListeners;
    private SensorManager sensorMan;

    private Sensor sensorAcce;
    private Sensor sensorMagn;
    private Sensor sensorRot;

    private static SensorData instance=new SensorData();

    private boolean Started=false;

    private SensorData(){
        accelBufferAlgo = new BufferAlgo1(0.1f, 0.2f);
        magnetBufferAlgo = new BufferAlgo1(0.1f, 0.2f);
        RotationListeners=new ArrayList<HeadRotationInterface>();
    }

    public static SensorData getInstance(){
        if(instance == null)
            instance=new SensorData();
        return instance;
    }

    public void startSensing(Context context){
        sensorMan = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        sensorAcce = sensorMan.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        sensorMagn = sensorMan.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
        sensorMan.registerListener(this, sensorAcce,
                SensorManager.SENSOR_DELAY_GAME);
        sensorMan.registerListener(this, sensorMagn,
                SensorManager.SENSOR_DELAY_GAME);

       /* sensorRot= sensorMan.getSensorList(Sensor.TYPE_ROTATION_VECTOR).get(0);
        sensorMan.registerListener(this, sensorRot,
                SensorManager.SENSOR_DELAY_GAME);*/
        Started=true;
    }

    public void stopSensing(){
        if( sensorMan != null){
            sensorMan.unregisterListener(this,sensorAcce);
            sensorMan.unregisterListener(this,sensorMagn);
        }
        Started=false;
    }

    public boolean IsStarted(){
        return Started;
    }
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent evt) {

        if (clear.tryAcquire()) {
                int type = evt.sensor.getType();
                // Smoothing the sensor data a bit seems like a good
                // idea.
                if (type == Sensor.TYPE_MAGNETIC_FIELD) {
                    orientation = lowPass(evt.values, orientation, 0.1f);
                } else if (type == Sensor.TYPE_ACCELEROMETER) {

                    acceleration = lowPass(evt.values, acceleration,
                            0.05f);
                }
                if (oldAcceleration != null || oldOrientation != null) {
                    accelBufferAlgo.execute(oldAcceleration,
                            acceleration);
                    magnetBufferAlgo.execute(oldOrientation,
                            orientation);
                }
                else {
                    oldAcceleration = acceleration;
                    oldOrientation = orientation;
                }

          /*  if(type == Sensor.TYPE_ROTATION_VECTOR) {
               SensorManager.getQuaternionFromVector(Quat,evt.values);
                InvokeQuatEvent();
            }*/
                if ((type == Sensor.TYPE_MAGNETIC_FIELD)
                        || (type == Sensor.TYPE_ACCELEROMETER)) {
                    newMat = new float[16];
                    SensorManager.getRotationMatrix(newMat, null,
                            oldAcceleration, oldOrientation);
                    SensorManager.remapCoordinateSystem(newMat,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Z, newMat);
                    InvokeRoationEvent();
                }
                clear.release();
        }
    }

    private void InvokeRoationEvent(){
        for(HeadRotationInterface listener : RotationListeners){
            listener.onRotationChanged(newMat);
        }
    }

    private void InvokeQuatEvent(){
        for(HeadRotationInterface listener : RotationListeners){
            listener.onQuaternionChanged(Quat);
        }
    }
    public void registerRotationListener(HeadRotationInterface listener){
        RotationListeners.add(listener);
    }
    public void unRegisterRotationListener(HeadRotationInterface listener){
        RotationListeners.remove(listener);
    }

    // Filter Class1
    public class BufferAlgo1 {

        private final float a;
        private final float b;
        private final float m;
        private final float n;

        public BufferAlgo1(float a, float b) {
            this.a = a;
            this.b = b;
            m = 1f / (b - a);
            n = a / (a - b);
        }

        public boolean execute(float[] target, float[] values) {
            target[0] = morph(target[0], values[0]);
            target[1] = morph(target[1], values[1]);
            target[2] = morph(target[2], values[2]);
            return true;
        }

        /**
         * @param v
         * @param newV
         * @return newT=t+f(|v-t|)
         */
        private float morph(float v, float newV) {
            float x = newV - v;
            if (x >= 0) {
                if (x < a)
                    return v; // v+0*x
                if (b <= x)
                    return newV; // v+1*x
                return v + x * m + n;
            } else {
                if (-x < a)
                    return v;
                if (b <= -x)
                    return newV;
                return v + x * m + n;
            }
        }

    }

    // LowPass Filter
    protected float[] lowPass(float[] input, float[] output, float alpha) {
        if (output == null)
            return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }
}
