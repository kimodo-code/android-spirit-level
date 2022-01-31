

public class Values {

    float z;
    float x;
    float y;

    float independentVal;




    public Values () {

    }
    //object just x
    //object just y
    //object xy
    public Values (float z, float x, float y) {
        this.z = z;
        this.x = x;
        this.y = y;

    }

    public Values (float independentVal) {

        this.independentVal = independentVal;

    }


    public Values (float x, float y) {

       this.x = x;
       this.y = y;

    }





    @Override
    public String toString() {
        return "Values{" +
                "z=" + z +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
