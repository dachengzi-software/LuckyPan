package cn.bigorange.wheel.utils;

public class AngleUtils {

    public static float angle0To360(float angle) {
        angle = angle % 360; //converts angle to range -360 + 360
        if (angle >= 0.0f) { //converts angle to range 1-360 degrees
            return angle;
        } else {
            return angle + 360.0f;
        }
    }

    //检查角度是否介于：)//check if angle is between angles

    public static boolean angleIsBetweenAngles(float a, float b) {

        a = angle0To360(a);//normalize angles to be 1-360 degrees

        b = angle0To360(b);

        if (a < b) {
            return a <= 270.0f && 270.0f <= b;
        }
        return a <= 270.0f || 270.0f <= b;

    }

//    public static boolean angleIsBetweenAngles2(float a, float b, float middle) {
//
//        middle = angle0To360(middle);//normalize angles to be 1-360 degrees
//
//        if (a < b) {
//            return a <= middle && middle <= b;
//        }
//        return a <= middle || middle <= b;
//
//    }
}
