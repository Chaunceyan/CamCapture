

package webapp.classes.CaptureColor;

import java.awt.*;
import java.io.IOException;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CamCaptureDemo {
    private static OpenCVFrameConverter.ToIplImage IplConverter = new OpenCVFrameConverter.ToIplImage();
    private static CanvasFrame regFrame = new CanvasFrame("RegularImage");
    private static CanvasFrame filteredFrame = new CanvasFrame("FilertedImage");
    private static Java2DFrameConverter J2DConverter = new Java2DFrameConverter();
    private static Map<String, ColoredObject> objectList;
    private static JButton captureButton = new JButton("Capture");
    private static JFrame frame = new JFrame("The Testing UI");

    public static void main(String[] args) throws InterruptedException, IOException, FrameGrabber.Exception {

        objectList = new HashMap<String, ColoredObject>();
        objectList.put("Yellow", new ColoredObject("Yellow"));
        objectList.put("Red", new ColoredObject("Red"));
        objectList.put("Green", new ColoredObject("Green"));
        objectList.put("Blue", new ColoredObject("Blue"));
        // Normal configuration
        // CamCaptureTypes demo = new CamCaptureTypes();
        // demo.detectImage();
//         showLocalImage();
        showCamImage();
    }

    private static void showCamImage() throws FrameGrabber.Exception {
        FrameGrabber grabber = new FFmpegFrameGrabber(new java.io.File("/dev/video0"));
        grabber.start();
//        for (String color : objectList.keySet()) {
//            ColoredObject object = objectList.get(color);
//            printImageRGB(IplConverter.convert(detectImage(grabber.grab(), object)), object.getColor());
//        }
//        regFrame.showImage(IplConverter.convert(cropImage(detectImage(grabber.grab(), objectList.get("Green")))));
        while(true) {
            IplImage image = IplConverter.convert(grabber.grab());
            IplImage result = image.clone();
            for (String color : objectList.keySet()) {
                IplImage indivResult = result.clone();
                ColoredObject object = objectList.get(color);
                image = detectImage(image, object);
                printImageRGB(detectImage(indivResult,object), object.getColor());
            }
            regFrame.setLayout(new BorderLayout());
            regFrame.add(captureButton);
            regFrame.showImage(IplConverter.convert(cropImage(image)));
            regFrame.setResizable(true);
            regFrame.pack();
        }
    }

    private static IplImage detectImage(IplImage image, ColoredObject object) {

        if (image != null) {
            opencv_core.IplImage threshold = getThresholdImage(image, object.getMin(), object.getMax());
            return getContours(threshold, image);
        }
        return null;
    }


    public static IplImage getThresholdImage(IplImage iplImage, CvScalar min, CvScalar max) {
        IplImage imgThreshold = cvCreateImage(cvGetSize(iplImage), 8, 1);
        cvInRangeS(iplImage, min, max, imgThreshold);// red
        cvSmooth(imgThreshold, imgThreshold, CV_BLUR_NO_SCALE, 7, 7, 0, 0);
//        cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 15,0,0,0);
        filteredFrame.showImage(IplConverter.convert(imgThreshold));
        return imgThreshold;
    }

    public static IplImage getContours (IplImage imgThreshold, IplImage original) {
        CvMemStorage storage = CvMemStorage.create();
        CvSeq contours = new CvContour(null);

        int noOfContors  = cvFindContours(imgThreshold, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));
        System.out.println(noOfContors);

        CvPoint p1 = new CvPoint(0,0), p2 = new CvPoint(0,0);

        for (CvSeq ptr = contours; ptr != null&&!ptr.isNull(); ptr = ptr.h_next()) {

            CvScalar color = CvScalar.ALPHA255;
            CvRect sq = cvBoundingRect(ptr, 0);


            System.out.println("X ="+ sq.x()+" Y="+ sq.y());
            System.out.println("Height ="+sq.height()+" Width ="+sq.width());
            System.out.println("");

            p1.x(sq.x());
            p2.x(sq.x()+sq.width());
            p1.y(sq.y());
            p2.y(sq.y()+sq.height());
            cvRectangle(original, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
            // cvDrawContours(original, ptr, color, CV_RGB(0,0,0), -1, CV_FILLED, 8, cvPoint(0,0));
        }
        return original;
    }

    public static IplImage cropImage (IplImage orig) {

        CvRect r = new CvRect(160, 100, 310, 330);
            //After setting ROI (Region-Of-Interest) all processing will only be done on the ROI
        cvSetImageROI(orig, r);
        IplImage cropped = cvCreateImage(cvGetSize(orig), orig.depth(), orig.nChannels());
            //Copy original image (only ROI) to the cropped image
        cvCopy(orig, cropped);
        return cropped;
    }

    public static void printImageRGB(IplImage image, String title) {
        cvSaveImage(title + ".jpg", cropImage(image));
    }
}

