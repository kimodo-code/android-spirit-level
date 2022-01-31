
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    private TextView tv;
    private SensorManager sm;
    private float rotation_matrix[] = new float[16];
    private float orientation_values[] = new float[4];
    ArrayList<Values> arrliVals = new ArrayList<Values>();
    View view1;
    View view2;

    private OneDimension oneDimen;
    private TwoDimension twoDimen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oneDimen = new OneDimension(getApplicationContext());// a method to fetch values from the sensor in Main Activity is in OneDimension view, therefore need OneDimension object
        twoDimen = new TwoDimension(getApplicationContext());

       // tv = (TextView) findViewById(R.id.tv);



        view1 = View.inflate(this, R.layout.one_dimension, null);
        view2 = View.inflate(this, R.layout.two_dimension, null);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);


        sm.registerListener(new SensorEventListener() {

                                @Override
                                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                }


                                @Override
                                public void onSensorChanged(SensorEvent event) {

                                    SensorManager.getRotationMatrixFromVector(rotation_matrix, event.values);
                                    SensorManager.getOrientation(rotation_matrix, orientation_values);




                                    orientation_values[0] = (float) Math.toDegrees(orientation_values[0]);
                                    orientation_values[1] = (float) Math.toDegrees(orientation_values[1]);
                                    orientation_values[2] = (float) Math.toDegrees(orientation_values[2]);
                                    // tv.setText("orientation values (deg) bearing (z): " + orientation_values[0] + " pitch (x): " + orientation_values[1] + " roll (y): " + orientation_values[2]);
//if flat, set content view to 1d custom view, if not flat, set to 2d custom view

                                    float z = orientation_values[0];
                                    float x = orientation_values[1];
                                    float y = orientation_values[2];

                                    oneDimen.updateX(orientation_values[1]);//this passes values to the spirit level view
                                    oneDimen.updateY(orientation_values[2]);

                                    twoDimen.updateX(orientation_values[1]);//this passes values to the spirit level view
                                    twoDimen.updateY(orientation_values[2]);
                                    twoDimen.updateAzimuth(orientation_values[0]);
                                    Values obj = new Values(z, x, y);




                                    //copyOfOrientationValues = orientation_values.clone();

                                 Log.d("Vals", "object " + obj.toString());
                                    addTo(obj);//method to store the values in arraylist


                                    if ((orientation_values[1] > 20) || (orientation_values[1] < -20) || (orientation_values[2] > 20) || (orientation_values[2] < -20)) {


                                        setContentView(oneDimen);

                                        // Log.d("tag", "x " + orientation_values[1] + "y " + orientation_values[2] + "z " + orientation_values[0]);
                                    } else {
                                        setContentView(twoDimen);
                                    }
                                }

                            },

                sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI);


    }
//store in datastructure the last 500 values, keep updated

    public void addTo (Values obj) {

        if (arrliVals.size() < 500) {
            arrliVals.add(obj);
           // Log.d("Vals", "Vals " + Arrays.deepToString(arrliVals.toArray()));

        } else if (arrliVals.size() >= 500) {
            arrliVals.remove(0);
            arrliVals.add(obj);
            //Log.d("Vals", "OVERVals " + Arrays.deepToString(arrliVals.toArray()));

        }


    }







}
