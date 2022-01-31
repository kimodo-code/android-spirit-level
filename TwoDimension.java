

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class TwoDimension extends View {

    Paint blue, black, red, blackText, blackTextRight, orange, blackTextBox;
    private Rect rectDec;
    private Rect rectInc;
    float positionX = 0;//initial values before sensor update
    float positionY = 0;
    float positionZ = 0;
    ArrayList<Float> arrliXVals = new ArrayList<Float>();
    ArrayList<Float> arrliYVals = new ArrayList<Float>();
    float minX = 0;
    float maxX = 0;
    float minY = 0;
    float maxY = 0;

    float xTap = 0;
    float yTap = 0;

    int decConstant = 0;
    int incConstant = 0;

    int counter = 0;//keep track of taps
    public TwoDimension(Context c) {
        super(c);
        init();
    }

    public TwoDimension(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    public TwoDimension(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    private void init() {
        //drawables, squares, circles, bitmaps etc

        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        orange = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.parseColor("#000000"));
        blue.setColor(Color.parseColor("#0000FF"));
        red.setColor(Color.parseColor("#FF0000"));
        orange.setColor(Color.parseColor("#FF8C00"));



        black.setStyle(Paint.Style.STROKE);
        black.setStrokeWidth(10);
        blue.setStyle(Paint.Style.FILL);
        red.setStyle(Paint.Style.FILL);
        orange.setStrokeWidth(10);
        orange.setStyle(Paint.Style.STROKE);

        blackText = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackText.setColor(Color.parseColor("#000000"));
        blackText.setTextSize(60);

        blackTextBox = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackTextBox.setColor(Color.parseColor("#000000"));
        blackTextBox.setTextSize(150);

        blackTextRight = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackTextRight.setColor(Color.parseColor("#000000"));
        blackTextRight.setTextSize(60);

        //the increase/decrease line angle boxes
       rectDec = new Rect (20,150,170,250);
        rectInc = new Rect(555, 150, 705,250);


    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = 50;
        int ring = 500;



//Log.d("measurements", "width " + width );

        // Log.d("measurements", "height " + height );
        //canvas.drawCircle(width/2, height/2, radius, blue);//starting circle
//width = 1080, height = 2031 on this phone
        //outer circle (bullseye ring)
        canvas.drawCircle(width / 2, height / 2, ring, black);

        //Log.d("Vals", "positionX " + positionX);
        //Log.d("Vals", "positionY " + positionY);

        int centre = (width + height) / 2;

        canvas.drawText("Y Max " + maxY, 0, 50, blackText);
        canvas.drawText("Y Min " + minY, 0, 100, blackText);

        canvas.drawText("X Max " + maxX, 555, 55, blackTextRight);
        canvas.drawText("X Min " + minX, 555, 105, blackTextRight);
//increase and decrease box with <> symbols
        canvas.drawRect(rectDec, black);
        canvas.drawRect(rectInc, black);
        canvas.drawText("<", 20, 250, blackTextBox);
        canvas.drawText(">", 555, 250, blackTextBox);

        canvas.drawCircle(-(positionY * 50) + width / 2, (positionX * 50) + height / 2, radius, blue);


        canvas.drawLine(
                width / 2,//start in centre of circle
                height / 2,
                (float) (width / 2 + ring
                        * Math.sin((double) (-positionZ - decConstant + incConstant) / 180 * 3.143)),//add in dec and int constants, these are triggered by tapping on dec and inc rectangles
                (float) (height / 2 - ring
                        * Math.cos((double) (-positionZ - decConstant + incConstant) / 180 * 3.143)), black);
        Log.d("Z", "positionZ " + positionZ);

        /*
        if (positionZ > -2 && positionZ < 2) {//when phone is pointed north, turn orange

            canvas.drawLine(
                    width/2,
                    height/2,
                    (float) (width/2 + ring
                            * Math.sin((double) (-positionZ) / 180 * 3.143)),
                    (float) (height/2 - ring
                            * Math.cos((double) (-positionZ) / 180 * 3.143)), orange);
        }


         */


        if (positionY > 10 || positionY < -10) {//turn the dot red when exceeded + 10 or - 10 degrees
            canvas.drawCircle(-(positionY * 50) + width / 2, (positionX * 50) + height / 2, radius, red);

        } else if (positionX > 10 || positionX < -10) {//turn the dot red when exceeded + 10 or - 10 degrees
            canvas.drawCircle(-(positionY * 50) + width / 2, (positionX * 50) + height / 2, radius, red);
        }

    }//end onDraw

    public void updateX(float positionX) {
        this.positionX = positionX;
        invalidate();
        addToX(positionX);

    }

    public void updateY(float positionY) {
        this.positionY = positionY;
        invalidate();
        addToY(positionY);

    }

    public void updateAzimuth(float positionZ) {
        this.positionZ = positionZ;
        invalidate();


    }

    @Override
    public boolean onTouchEvent (MotionEvent event){


        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {


//get position on board
            int xTap = (int) event.getX();
            int yTap = (int) event.getY();

            if(rectDec.contains(xTap,yTap)) {

                decConstant = decConstant + 10;
            }
            if(rectInc.contains(xTap,yTap)) {

                incConstant = incConstant + 10;
            }




        }

        return true;
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
           // Log.d("XVals", "Vals " + Arrays.toString(arrliXVals.toArray()));

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
            //Log.d("XVals", "Vals " + Arrays.toString(arrliYVals.toArray()));

        } else if (arrliYVals.size() >= 500) {
            arrliYVals.remove(0);
            arrliYVals.add(positionY);
           // Log.d("XVals", "OVERVals " + Arrays.toString(arrliYVals.toArray()));

        }


    }
}
