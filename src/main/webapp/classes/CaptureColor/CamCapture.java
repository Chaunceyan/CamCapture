package classes.CaptureColor;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.*;
import org.json.simple.JSONObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class CamCapture {

    private OpenCVFrameGrabber grabber;
    private Java2DFrameConverter J2DConverter;
    private OpenCVFrameConverter.ToIplImage IplConverter;
    private Map<String, ColoredObject> objectList;
    private CvScalar minRed, maxRed,
            minGreen, maxGreen,
            minBlue, maxBlues;

    public CamCapture() {
        // Get the camera Number zero;
        grabber = new OpenCVFrameGrabber(1);
        //minRed = cvScalar(0, 0, 130, 0);//BGR-A
        //maxRed = cvScalar(100, 100, 255, 0);//BGR-A
        J2DConverter = new Java2DFrameConverter();
        IplConverter = new OpenCVFrameConverter.ToIplImage();
        objectList = new HashMap<String, ColoredObject>();
        objectList.put("Red", new ColoredObject("Red"));
        objectList.put("Blue", new ColoredObject("Blue"));
        objectList.put("Green", new ColoredObject("Green"));
    }

    public String detectImage(BufferedImage bufferedImage) {
        IplImage iplImage = IplConverter.convert(J2DConverter.convert(bufferedImage));
        for (String color : objectList.keySet()) {
            ColoredObject object = objectList.get(color);

            if (iplImage != null) {
                IplImage threshold = getThresholdImage(iplImage, object.getMin(), object.getMax());
                getCoordinates(threshold, object);
                System.out.println(object.getPosX() + " , " + object.getPosY());
            }
        }
        return generateJson();
    }

    public String detectImage() throws IOException, FrameGrabber.Exception {
        grabber.start();
        for (String color : objectList.keySet()) {
            ColoredObject object = objectList.get(color);
            IplImage iplImage = IplConverter.convert(grabber.grab());

            if (iplImage != null) {
                IplImage threshold = getThresholdImage(iplImage, object.getMin(), object.getMax());
                getCoordinates(threshold, object);
                System.out.println(object.getPosX() + " , " + object.getPosY());
            }
        }
        grabber.stop();
        grabber.release();
        return generateJson();
    }

    public IplImage getThresholdImage(IplImage iplImage, CvScalar min, CvScalar max) {
        IplImage imgThreshold = cvCreateImage(cvGetSize(iplImage), 8, 1);
        cvInRangeS(iplImage, min, max, imgThreshold);// red
        //cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 15,0,0,0);
        //new CanvasFrame("Cropping").showImage(IplConverter.convert(imgThreshold));
        return imgThreshold;
    }

    public void getCoordinates(IplImage iplImage, ColoredObject object) {
        opencv_imgproc.CvMoments moments = new opencv_imgproc.CvMoments();
        cvMoments(iplImage, moments, 1);
        double momX10 = cvGetSpatialMoment(moments, 1, 0); // (x,y)
        double momY01 = cvGetSpatialMoment(moments, 0, 1);// (x,y)
        double area = cvGetCentralMoment(moments, 0, 0);
        object.setPosX(momX10 / area);
        object.setPosY(momY01 / area);
    }

    public String generateJson() {
        JSONObject obj = new JSONObject();

        for (String color : objectList.keySet()
                ) {
            JSONObject subObj = new JSONObject();
            subObj.put("PositionX", objectList.get(color).getPosX());
            subObj.put("PositionY", objectList.get(color).getPosY());
            obj.put(color, subObj);
        }

        return obj.toJSONString();
    }
}
