package classes.CaptureColor;

import org.bytedeco.javacpp.opencv_core.CvScalar;

import static org.bytedeco.javacpp.opencv_core.cvScalar;

/**
 * Created by Chauncey on 11/10/15.
 */
public class ColoredObject {

    private String color;
    private double posX;
    private double posY;
    private boolean appearing;

    public CvScalar getMin() {
        return min;
    }

    public void setMin(CvScalar min) {
        this.min = min;
    }

    private CvScalar min;

    public CvScalar getMax() {
        return max;
    }

    public void setMax(CvScalar max) {
        this.max = max;
    }

    private CvScalar max;

    public ColoredObject(String color) {
        appearing = false;
        this.color = color;
        switch (color) {
            case "Red":
                min = cvScalar(0, 0, 200, 0);
                max = cvScalar(100, 100, 255, 0);
                break;
            case "Green":
                min = cvScalar(0, 200, 0, 0);
                max = cvScalar(100, 255, 100, 0);
                break;
            case "Blue":
                min = cvScalar(200, 0, 0, 0);
                max = cvScalar(255, 100, 100, 0);
                break;
        }
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setAppearing (boolean appearing) {
        this.appearing = appearing;
    }

    public boolean isAppearing () {
        return appearing;
    }

}
