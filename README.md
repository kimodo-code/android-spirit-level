# android-spirit-level
Two dimensional spirit level using rotational sensors
This app uses a rotation vector composite sensor. This sensor is a composite sensor, which combines accelerometer and gyroscope information to discover the device’s full orientation. A rotation vector sensor can tell us compass information as well as pitch and roll information, therefore it was the most suitable choice for this spirit level with compass application.

MainActivity.java

This application uses two custom view, a view for the two dimensional case and another for the one dimensional case. The two dimensional custom view is the one that’s shown when the phone is flat (give or take 20 degrees) and the one dimensional view is shown when the phone is not flat. The idea is that when the phone is perpendicular to a flat surface, such as a table, a horizontal spirit level is shown where the bubble motion is left to right, and when the phone is flat on the table, a bubble level view is shown in which the bubble can move in both the x and y directions.
The code that selects which selects which view to call is as follows:
if ((orientation_values[1] > 20) || (orientation_values[1] < -20) || (orientation_values[2] > 20) || (orientation_values[2] < -20)) {


    setContentView(oneDimen);

    // Log.d("tag", "x " + orientation_values[1] + "y " + orientation_values[2] + "z " + orientation_values[0]);
} else {
    setContentView(twoDimen);
}

The values are stored in a rotation matrix and converted to angles. What the statement says is if either the x or y angles exceed 20 degrees, set the one dimensional view as the view that is displayed, otherwise set the two dimensional view.
The views in this case are passed as objects. Objects were created for the views because to get the sensor values from the onSensorChanged method – 
public void onSensorChanged(SensorEvent event) {

one cannot return values as the method type is void, therefore to pass sensor values from the onSensorChanged method in MainActivity to each view, updateX, updateY and updateAzimuth methods were created in the views.  This line of code
oneDimen.updateX(orientation_values[1]);//this passes values to the spirit level view



and similar for y and z values were put in onSensorChanged in Main Activity so that each time a sensor value changed, this method was called, and accurate values were passed into the views without having to register the sensor a second time in a view, which would cause excessive polling. 
Also in MainActivity, was an addition for Bracket 2, where the last 500 values are kept in an array and maintained. In the application, this method is not used directly as other methods using similar code get the specific angles in question, rather than all three angles as the one in MainActivity does. Nevertheless, the explanation for the code for this is as follows:
Values obj = new Values(z, x, y);

A values object is created in onSensorChanged, so every time the sensor values change, a new object is created.  Then addTo adds this object to an arraylist - arrliVals. If there are less 500 objects in the arraylist, the object is added directly. Otherwise, as items in lists are added to the end and not the beginning of the list, to remove the 500th element once the arraylist becomes that size, the item at position(0) is removed and the new item is added normally. Removing an item shifts everything in the list back a place. Therefore a constant size of 500 is maintained, so long as a values object is removed each time one is added.

public void addTo (Values obj) {

    if (arrliVals.size() < 500) {
        arrliVals.add(obj);
       // Log.d("Vals", "Vals " + Arrays.deepToString(arrliVals.toArray()));

    } else if (arrliVals.size() >= 500) {
        arrliVals.remove(0);
        arrliVals.add(obj);
        //Log.d("Vals", "OVERVals " + Arrays.deepToString(arrliVals.toArray()));

    }

OneDimension.java
First there are the usual three constructors for a custom view, then there is a shared init method and a onDraw method. The init method initialises paint objects, sets their colour and various other style attributes like line width. It’s good practise to do this here in the init method. The drawing in this case is simple, a rectangle for the horizontal level strip, and a coloured dot to represent the bubble. 
In the onDraw, the rectangular box is drawn. The height and width are taken from the onMeasure method to get the precise dimensions of whatever device the app is run on. The box has a height of 100 (center of screen +/- 50 in either direction) and a width that takes up the entire width of the device. 


canvas.drawRect(0, (height / 2) + 50, width, (height / 2) - 50, black);//box for bubble
 
To measure the roll, we need the y position values. These are being continually updated on each value change by the updateY method. What this line of code is saying is to alter the left/right position off center based on whatever the y value is (with a multiplication by 50 scale up so that the bubble movement is more noticeable), keep the height constant, draw the circle of set radius(50 in this case) and colour it green. There is a negative sign in front of positionY so that the dot behaves like a bubble, and rolls away from the direction the phone is being tilted in.

canvas.drawCircle((-positionY * 50 ) + (width / 2), height / 2, radius, green);

There is also some additional functionality which was not specifically requested in the brief, but added for a better UI experience, and that is to turn the dot red whenever the values of +10/-10 are breached. 
if (positionY > 10 || positionY < -10) {//turn the dot red when exceeded + 10 or - 10 degrees


    canvas.drawCircle(-positionY + (width / 2), height / 2, radius, red);

}

The text boxes to display the maximum and minimum values reached were created using the canvas.drawText method. Although this is slightly more cumbersome then using a TextView, because of the way in which the view was passed into setContentView in MainActivity as an object rather than as a view, there were some issues with XML styling, therefore the simplest solution in this case was to use the canvas.drawText method.

To get these minima and maxima, there is an addToY method in OneDimension. This stores the last 500 y values in an arraylist, and then every time there is a new minimum or maximum, this new value is displayed in the appropriate text box.

The storing of the last 500 values part is the same as for the method in MainActivity, except float values are stored rather than Values objects. The finding of new maxima and minima is handled here:
public void addToY(float positionY) {




    if (positionY < minY) {
        minY = positionY;


    }

    if (positionY > maxY) {
        maxY = positionY;

    }
This says: If the new value is greater than the old maximum, make this value the new maximum, and vice versa for minimum values. minY and maxY are then the values displayed to the user in the min/max values box.
TwoDimension.java

As usual, there are three constructors, an init method and an onDraw method. The init method contains the shapes – which this time include a dot and a larger outer ring. This is because the dot can now move horizontally and vertically therefore a bullseye type spirit level is required.
The values needed this time will be both x and y, so there are update value and store value for max/min methods, identical to the ones in OneDimension, but this time one for x and another for y.

int radius = 50;
int ring = 500;

canvas.drawCircle(width / 2, height / 2, ring, black);
 
canvas.drawCircle(-(positionY * 50) + width / 2, (positionX * 50) + height / 2, radius, blue);
 
The outer ring, ten times the size of the dot so that when x = +10/-10 or y = +10/-10 the dot lies outside of the ring. Again each position value, x and y, is the offset from the centre with a scale up for visible movement of the bubble. Negative sign to make the dot behave like an air bubble in a traditional, physical spirit level. 
 
There is an additional function in the two dimensional view and that is for a compass, which can be adjusted.
 
canvas.drawLine(
        width / 2,//start in centre of circle
        height / 2,
        (float) (width / 2 + ring
                * Math.sin((double) (-positionZ - decConstant + incConstant) / 180 * 3.143)),//add in dec and int constants, these are triggered by tapping on dec and inc rectangles
        (float) (height / 2 - ring
                * Math.cos((double) (-positionZ - decConstant + incConstant) / 180 * 3.143)), black);
 
This compass takes the form of a moving line. This value is passed from the sensor in MainActivity, and it is the Z value which goes by several names, the bearing, the azimuth or yaw. It’s the rotation around an axis that comes from the screen towards the sky. When Z is 0, it’s pointing at north. 
As you can see in this code, there is a decConstant and an incConstant. This increases or decreases Z by a value of ten, and it’s triggered by the user tapping on an increase or decrease button. In this way, the user can move the line to any point they choose on the compass circle. For example, 5 taps on increase, and three taps on decrease, will lead to 50-30 being added to the Z position, therefore the resulting Z position will be +20 degrees away from North.

Here is the method that handles this:
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
In other words, if the user taps anywhere within bounds of the rectInc or rectDec “button”, ten is added to the constant so that the user can move the line in increments of ten.

EULER ANGLE – Y VALUE FLIPS WHEN PITCH IS 90 DEGREES.
When device is in portrait and x pitches to 90 degrees, Y and Z are perpendicular to the ground and cannot detect roll. Unfortunately this issue was spotted too late to implement a proper fix. Various approaches that could work: transforming the matrix so that x value never reaches 90 degrees,  using quaterniums.
