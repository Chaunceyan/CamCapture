

import classes.CaptureColor.CamCapture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FrameGrabber;

import java.io.IOException;

/**
 * Created by Chauncey on 11/14/15.
 */
public class AjaxServlet extends javax.servlet.http.HttpServlet {
    Logger logger = null;

    public void init() {
        logger = LogManager.getLogger();
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("Test");
        logger.info("Get The Get Req");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        CamCapture cam = new CamCapture();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(cam.detectImage());
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        logger.info("Get The Get Req");
    }
}