package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.content.res.AssetManager;
import android.util.Log;

//import gov.nasa.arc.astrobee.Result;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

import org.opencv.aruco.Aruco;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.android.Utils;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;



/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {


    private List<Double> ratio = new ArrayList<>();
    //private String[] defaultapk = {"beaker","coma","goggles","hammer","pipette","screwdriver","spanner","tape","thm","watch"};
    private String[] beakerloco = {"/Users/johnbee/Downloads/Arcadia-Guinea-Pigs/app/src/main/assets/beaker.png"};
    private int index = 1;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void runPlan1() {

        Log.i(TAG, "start mission");

        //start
        api.startMission();

        //late move the x value to 11.1d so we can check whether image processing just isn't seeing the image right :P

        //SLOT 1
        Quaternion quaternion = new Quaternion(0f, 0f, -0.707f, 0.707f);
        Point point1 = new Point(10.9d, -9.92284d, 5.195);
        //Point point1 = new Point(11.20d, -10.10d, 5.30d);
        //Quaternion quaternion1 = new Quaternion(0f, 0f, 0f, 0f);
        api.moveTo(point1, quaternion, true);
        navImage();
        /*
        //move forward
        Point pointA = new Point(11.20d, -9.00d, 5.50d);
        api.moveTo(pointA, quaternion, true);


        //SLOT 2
        Point pointB = new Point(11.00d, -8.75d, 4.40d);
        //insert quaternion code look up
        Quaternion quaternion1 = new Quaternion(0f, 0.707f, 0f, 0.707f);
        api.moveTo(pointB, quaternion1, true);
        //navImage(); //slot 2


        //SLOT 3
        Point pointE = new Point(11.20d, -7.75d, 4.40d);
        //insert quaternion code look up
        api.moveTo(pointE, quaternion1, true);
        //navImage(); //slot 3

        Point pointD = new Point(10.40d, -7.75d, 4.40d);
        api.moveTo(pointD, quaternion1, true);

        Point pointF = new Point(10.40d, -7.25d, 4.40d);
        api.moveTo(pointF, quaternion1, true);


        //SLOT 4
        Point pointG = new Point(10.40d, -6.7607d, 4.9654d);
        //insert quaternion code look right
        Quaternion quaternion2 = new Quaternion(0f, 0f, -1f, 0f); //fix
        api.moveTo(pointG, quaternion2, true);

        //navImage();


        //astronaut time
        Point astro = new Point(11.143d, -6.7607d, 4.9654d);
        Quaternion astro1 = new Quaternion(0f, 0f, 0.707f, 0.707f);
        api.moveTo(astro, astro1, true);
        */
        /* **************************************************** */
        /* Let's move to the each area and recognize thmyteame items. */
        /* **************************************************** */

        // When you move to the front of the astronaut, report the rounding completion.
        api.reportRoundingCompletion();
        /* ********************************************************** */
        /* Write your code to recognize which item the astronaut has. */
        /* ********************************************************** */

        // Let's notify the astronaut when you recognize it.
        api.notifyRecognitionItem();
        /*
         ******************************************************************************************************* */
        /* Write your code to move Astrobee to the location of the target item (what the astronaut
        is looking for) */
        /*
         ******************************************************************************************************* */

        // Take a snapshot of the target item.
        api.takeTargetItemSnapshot();

    }

    @Override
    protected void runPlan2() {
        // write your plan 2 here

    }

    @Override
    protected void runPlan3() {
        // write your plan 3 here
    }

    //Resize image/
    //image recognition code as a method
    private void navImage() {
        // Get a camera image.
        Log.i(TAG, "image recognition");
        Mat image = api.getMatNavCam();

        //Detect AR
        Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_5X5_250);
        List<Mat> corners = new ArrayList<>();
        Mat markerIds = new Mat();
        Aruco.detectMarkers(image, dictionary, corners, markerIds);

        //Get Camera matrix
        Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
        cameraMatrix.put(0, 0, api.getNavCamIntrinsics()[0]);
        //Get lens distortion paramters
        Mat cameraCoefficients = new Mat(1, 5, CvType.CV_64F);
        cameraCoefficients.put(0, 0, api.getNavCamIntrinsics()[1]);
        cameraCoefficients.convertTo(cameraCoefficients, CvType.CV_64F);

        //undistort image
        Mat undistortImg = new Mat();
        Calib3d.undistort(image, undistortImg, cameraMatrix, cameraCoefficients);

        //Pattern matching
        //Load template images
        //Mat[] templates = new Mat[defaultapk.length];
        //Mat[] histogram = new Mat[defaultapk.length];


        Log.i(TAG, "Perimeter making, loading image from file!!!!!!");
        //AssetManager assets = getAssets();
        //for (int i = 0; i < defaultapk.length; i++) {
            //try {

                //open the template image file in Bitmap from the file name and convert to Mat
                //InputStream inputStream = assets.open(defaultapk[i] + ".png");
                Mat mat = Imgcodecs.imread("/Users/johnbee/Downloads/Arcadia-Guinea-Pigs/app/src/main/assets/beaker.png");
                mat.convertTo(mat, CvType.CV_64FC3);
                //int size = (int) (mat.total() * mat.channels());
                //double[] temp = mat.get(0, 0);
                //for (int i = 0; i < size; i++) {
                //    Log.i(TAG, "temp[" + i + "]: " + temp[i]);
                //}
                //    color[i] = (color[i] / 2);
                //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //Mat mat = new Mat();
                //Utils.bitmapToMat(bitmap, mat);
                //Convert to grayscale
                //Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
                //Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB);
                //Assign to an array of templates
                //templates[i] = mat;
                int start_x = 0;
                int end_x = 0;
                int start_y = 0;
                int end_y = 0;

                int cols  = mat.cols();
                int rows = mat.rows();
                int chs = mat.channels();
                int white_count = 0;
                int black_count = 0;
                int count = 0;
                //byte[] color = new byte[3];
                Log.i(TAG, "find x");
                //min for x
                for (int c = 0; c < cols; c++) {
                    boolean foundStart = false;
                    for (int r = 0; r < rows; r++) {
                        double[] color = mat.get(r, c);
                        for (int k = 0; k < rows; k++) {
                            Log.i(TAG, "color[" + count + "]: " + color[count]);
                            count++;
                        }

                        if((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                            start_x = c;
                            Log.i(TAG, "start_x: " + start_x);
                            foundStart = true;
                            break;
                        }
                    }
                    if (foundStart) {
                        break;
                    }
                }
                //max for x
                for (int z = start_x; z < cols; z++) {
                    boolean blackTrue = true;
                    for (int y = 0; y < rows; y++) {
                        double[] color = mat.get(y, z);
                        if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                            break;
                        } else {
                            blackTrue = false;
                            break;
                        }
                    }
                    if (!blackTrue){
                        Log.i(TAG, "end_x: " + end_x);
                        end_x = z - 1;
                        break;
                    }
                }
                Log.i(TAG, "find y");
                //min for y
                for (int r = 0; r < rows; r++) {
                    boolean foundStart = false;
                    for (int c = start_x; c <  cols; c++) {
                        //double[] color = targetImg.get(r, c);
                        double[] color = mat.get(r, c);
                        if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                            start_y = r;
                            Log.i(TAG, "start_y: " + start_y);
                            foundStart = true;
                            break;
                        }
                    }
                    if (foundStart) {
                        break;
                    }
                }
                //max for y
                for (int z = start_y; z < rows; z++) {
                    boolean blackTrue = true;
                    for (int y = 0; y < cols; y++)  {
                        double[] color = mat.get(z, y);
                        if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                            break;
                        } else {
                            blackTrue = false;
                            break;
                        }
                    }
                    if (!blackTrue){
                        end_y = z - 1;
                        Log.i(TAG, "end_y: " + end_y);
                        break;
                    }
                }

                for (int r = start_y; r < end_y; r++) {
                    for (int c = start_x; c < end_x; c++) {
                        //double[] color = mat.get(r, c);
                        double[] color = mat.get(r, c);
                        if ((color[0] >= 224) && (color[1] >= 224) && (color[2] >= 224)) {
                            white_count++;
                            // count this range color WHITE
                        }
                        else{
                            black_count++;
                            // all count as BLACK
                        }

                    }
                }
                Log.i(TAG, "black: " + black_count);
                Log.i(TAG, "white: " + white_count);
                double x = (double)black_count / (black_count + white_count);
               ratio.add(x);

               //inputStream.close();


            //} catch (IOException e) {
            //    e.printStackTrace();
           // }
        //}

        Log.i(TAG, "perimeter for scan");
        Mat targetImg = undistortImg.clone();
        start_x = 0;
        end_x = 0;
        start_y = 0;
        end_y = 0;

        cols  = targetImg.cols();
        rows = targetImg.rows();
        white_count = 0;
        black_count = 0;
        Imgproc.cvtColor(targetImg, targetImg, Imgproc.COLOR_RGBA2RGB);
        byte[] color = new byte[3];
        Log.i(TAG, "x find");
        //min for x
        for (int c = 0; c < cols; c++) {
            boolean foundStart = false;
            for (int r = 0; r < rows; r++) {
                //double[] color = targetImg.get(r, c);
                targetImg.get(r, c, color);
                if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                    start_x = c;
                    Log.i(TAG, "SCAN-start_x: " + start_x);
                    foundStart = true;
                    break;
                }
            }
            if (foundStart) {
                break;
            }
        }

        //max for x
        for (int z = start_x; z < cols; z++) {
            boolean blackTrue = true;
            for (int y = 0; y < rows; y++) {
                targetImg.get(y, z, color);
                if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                    break;
                } else {
                    blackTrue = false;
                    break;
                }
            }
            if (!blackTrue){
                end_x = z - 1;
                Log.i(TAG, "SCAN-end_x: " + end_x);
            break;
            }
        }
        Log.i(TAG, "y find");
        //min for y
        for (int r = 0; r < rows; r++) {
            boolean foundStart = false;
            for (int c = start_x; c <  cols; c++) {
                //double[] color = targetImg.get(r, c);
                targetImg.get(r, c, color);
                if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                    start_y = r;
                    Log.i(TAG, "SCAN-start_y: " + start_y);
                    foundStart = true;
                    break;
                }
            }
            if (foundStart) {
                break;
            }
        }
        //max for y
        for (int z = start_y; z < rows; z++) {
            boolean blackTrue = true;
            for (int y = 0; y < cols; y++)  {
                targetImg.get(z, y, color);
                if ((color[0] < 224) && (color[1] < 224) && (color[2] < 224)) {
                    break;
                } else {
                    blackTrue = false;
                    break;
                }
            }
            if (!blackTrue){
                end_y = z - 1;
                Log.i(TAG, "SCAN-end_y: " + end_y);
                break;
            }
        }

        //ratio testing
        Log.i(TAG, "ratio testing");
        for (int r = start_y; r < end_y; r++) {
            for (int c = start_x; c < end_x; c++) {
                //double[] color = targetImg.get(r, c);
                targetImg.get(r, c, color);
                if ((color[0] >= 224) && (color[1] >= 224) && (color[2] >= 224)) {
                    white_count++;
                    // count this range color WHITE
                }
                else{
                    black_count++;
                    // all count as BLACK
                }

            }
        }
        Log.i(TAG, "Scan-black: " + black_count);
        Log.i(TAG, "Scan-white: " + white_count);
        Log.i(TAG, "areaIn");
        double compare = (double)black_count / (black_count + white_count);
        for(int i = 0; i < ratio.size(); i++) {
            if(Math.abs(compare - ratio.get(i)) < .05) {
                //api.setAreaInfo(index, defaultapk[i], 1);
                index++;
            }

        }



    }

    private Mat resizeImg(Mat img, int width) {
        int height = (int) (img.rows() * ((double) width / img.cols()));
        Mat resizedImg = new Mat();
        Imgproc.resize(img, resizedImg, new Size(width, height));

        return resizedImg;
    }

    // Rotate Image
    private Mat rotImg(Mat img, int angle) {
        org.opencv.core.Point center = new org.opencv.core.Point(img.cols() / 2.0, img.rows() / 2.0);
        Mat rotatedMat = Imgproc.getRotationMatrix2D(center, angle, 1.0);
        Mat rotatedImg = new Mat();
        Imgproc.warpAffine(img, rotatedImg, rotatedMat, img.size());

        return rotatedImg;
    }

    //Remove multiple detections
    private static List<org.opencv.core.Point> removeDuplicates(List<org.opencv.core.Point> points) {
        double length = 10; //width 10 px
        List<org.opencv.core.Point> filteredList = new ArrayList<>();

        for (org.opencv.core.Point point : points) {
            boolean isInclude = false;
            for (org.opencv.core.Point checkPoint : filteredList) {
                double distance = calculateDistance(point, checkPoint);

                if (distance <= length) {
                    isInclude = true;
                    break;
                }
            }

            if (!isInclude) {
                filteredList.add(point);
            }
        }
        return filteredList;
    }

    //Find the distance between two points
    private static double calculateDistance(org.opencv.core.Point p1, org.opencv.core.Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    //get the maximum value of an array
    private int getMaxIndex(int[] array) {
        int max = 0;
        int maxIndex = 0;

        //find the index of the element with the largest value
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }



}