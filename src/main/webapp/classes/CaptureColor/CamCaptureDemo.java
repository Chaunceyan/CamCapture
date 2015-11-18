package classes.CaptureColor;

import java.io.IOException;

import classes.CaptureColor.CamCapture;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CamCaptureDemo {
    public static void main(String[] args) throws InterruptedException, IOException, FrameGrabber.Exception {
        //BufferedImage bufferedImage = ImageIO.read(new File("/Users/Chauncey/Downloads/Tangibles3.png"));

        // Normal configuration
        /*CamCapture demo = new CamCapture();
        demo.detectImage();*/

        showCamImage();
    }

    private static void showCamImage() throws FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);
        CanvasFrame frame = new CanvasFrame("Cropping");
        grabber.start();
        while(true) {
            frame.showImage(grabber.grab());
        }
    }
}
