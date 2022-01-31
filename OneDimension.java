
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class OneDimension extends View {

    Paint green, black, red, blackText;
    float positionX = 0;//initial values before sensor update
    float positionY = 0;
    float positionY90 = 0;
    TextView tv_min;
    View viewTV;
    ArrayList<Float> arrliXVals = new ArrayList<Float>();
    ArrayList<Float> arrliYVals = new ArrayList<Float>();
    float minX = 0;
    float maxX = 0;
    float minY = 0;
    float maxY = 0;
    float minY90 =0;
    float maxY90 = 0;
    ArrayList<Float> arrliYVals90 = new ArrayList<Float>();


    public OneDimension(Context c) {
        super(c);
        init();
        Log.d("Args", "one argument constructor");
    }

    public OneDimension(Context c, AttributeSet as) {
        super(c, as);
        init();
        Log.d("Args", "two argument constructor");
    }

    public OneDimension(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
        Log.d("Args", "three argument constructor");
    }

    private void init() {
        //drawables, squares, circles, bitmaps etc

        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackText = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.parseColor("#000000"));
        green.setColor(Color.parseColor("#006400"));
        red.setColor(Color.parseColor("#FF0000"));
        blackText.setColor(Color.parseColor("#000000"));


        black.setStyle(Paint.Style.STROKE);
        black.setStrokeWidth(10);
        green.setStyle(Paint.Style.FILL);
        red.setStyle(Paint.Style.FILL);
        blackText.setTextSize(60);


    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Configuration c = getResources().getConfiguration();//for portrait vs landscape check




        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = 50;


        //canvas.drawCircle(width/2, height/2, radius, green);//starting circle

        canvas.drawRect(0, (height / 2) + 50, width, (height / 2) - 50, black);//box for bubble


        if (c.orientation == Configuration.ORIENTATION_PORTRAIT) {//if in portrait measure the y roll
            canvas.drawText("Y Minimum " + minY, 0, 50, blackText);
            canvas.drawText("Y Maximum " + maxY, 55, 105, blackText);
            canvas.drawCircle((-positionY * 50) + (width / 2), height / 2, radius, green);




            // Log.d("Vals", "positionY " + positionY);

            if (positionY > 10 || positionY < -10) {//turn the dot red when exceeded + 10 or - 10 degrees


                canvas.drawCircle((-positionY * 50) + (width / 2), height / 2, radius, red);

            }


        } else if (c.orientation == Configuration.ORIENTATION_LANDSCAPE) {//if in landscape, measure the x pitch
            canvas.drawText("Y Minimum " + minY90, 0, 50, blackText);
            canvas.drawText("Y Maximum " + maxY90, 55, 105, blackText);

            Log.d("Vals", "positionY " + (positionY - 90));

            canvas.drawCircle((-(positionY - 90) * 100) + (width / 2), height / 2, radius, green);//positionX is value coming from sensor in main activity. * 100 scales up the movement of the dot. y stays constant in middle of screen..

            if (positionY -90 > 10 || positionY -90 < -10) {//turn the dot red when exceeded + 10 or - 10 degrees
                canvas.drawCircle((-(positionY - 90) * 100) + (width / 2), height / 2, radius, red);

            }
        }



    }//end onDraw

    public void updateX(float positionX) {
        this.positionX = positionX;
        invalidate();
        addToX(positionX);


    }

    public void updateY(float positionY) {
        this.positionY = positionY;
        positionY90 = positionY - 90;
        invalidate();
       addToY(positionY);
       addToY90(positionY90);


    }

    public void addToX(float positionX) {






            if (positionX < minX) {
                minX = positionX;
                //Toast.makeText(getContext(), "New X Minimum = " + minX, Toast.LENGTH_SHORT).show();

            }

            if (positionX > maxX) {
                maxX = positionX;
                //Toast.makeText(getContext(), "New X Maximum = " + maxX, Toast.LENGTH_SHORT).show();
        }


//}


        if (arrliXVals.size() < 500) {
            arrliXVals.add(positionX);
             //Log.d("XVals", "Vals " + Arrays.toString(arrliXVals.toArray()));

        } else if (arrliXVals.size() >= 500) {
            arrliXVals.remove(0);
            arrliXVals.add(positionX);
           // Log.d("XVals", "OVERVals " + Arrays.toString(arrliXVals.toArray()));

        }


    }

    public void addToY(float positionY) {




        if (positionY < minY) {
            minY = positionY;
            // Toast.makeText(getContext(), "New Y Minimum = " + minY, Toast.LENGTH_SHORT).show();

        }

        if (positionY > maxY) {
            maxY = positionY;
            // Toast.makeText(getContext(), "New Y Maximum = " + maxY, Toast.LENGTH_SHORT).show();
        }


//}


        if (arrliYVals.size() < 500) {
            arrliYVals.add(positionY);
            //  Log.d("XVals", "Vals " + Arrays.toString(arrliYVals.toArray()));

        } else if (arrliYVals.size() >= 500) {
            arrliYVals.remove(0);
            arrliYVals.add(positionY);
            // Log.d("XVals", "OVERVals " + Arrays.toString(arrliYVals.toArray()));

        }


    }

    public void addToY90(float positionY90) {




        if (positionY90 < minY90) {
            minY90 = positionY90;
            // Toast.makeText(getContext(), "New Y Minimum = " + minY, Toast.LENGTH_SHORT).show();

        }

        if (positionY90 > maxY90) {
            maxY90 = positionY90;
            // Toast.makeText(getContext(), "New Y Maximum = " + maxY, Toast.LENGTH_SHORT).show();
        }


//}


        if (arrliYVals90.size() < 500) {
            arrliYVals90.add(positionY90);
            //  Log.d("XVals", "Vals " + Arrays.toString(arrliYVals.toArray()));

        } else if (arrliYVals90.size() >= 500) {
            arrliYVals90.remove(0);
            arrliYVals90.add(positionY90);
            // Log.d("XVals", "OVERVals " + Arrays.toString(arrliYVals.toArray()));

        }


    }
}
