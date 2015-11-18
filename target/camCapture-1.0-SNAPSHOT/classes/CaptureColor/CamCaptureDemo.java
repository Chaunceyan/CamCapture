package main.webapp.classes.CaptureColor;

import java.io.IOException;

import classes.CaptureColor.CamCapture;
import org.bytedeco.javacv.FrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CamCaptureDemo {
    public static void main(String[] args) throws InterruptedException, IOException, FrameGrabber.Exception {
        BufferedImage bufferedImage = ImageIO.read(new File("/Users/Chauncey/Downloads/Tangibles3.png"));
        CamCapture demo = new CamCapture();
        demo.detectImage();
    }
}
