package point.instrument;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class transform {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String imgPath = "C:\\Users\\Administrator\\Desktop\\tilt.jpg";
		Mat img = Imgcodecs.imread(imgPath);
		
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\gray.png", gray);
		
		Imgproc.resize(gray, gray, new Size(300, 300));
		
		// 直方图均衡化--灰度增强
		Imgproc.equalizeHist(gray, gray);
		Imgcodecs.imwrite(
				"C:\\Users\\Administrator\\Desktop\\equalizeHist.png", gray);
		Mat binarization = new Mat();
		
		Imgproc.GaussianBlur(gray, gray, new Size(7, 7), 2, 2);// 高斯滤波：光滑处理,降噪
		Imgproc.adaptiveThreshold(gray, binarization, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25,
				10);// 局部自适应阈值化
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(binarization, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));

		System.out.println(contours.get(0).rows());
		for (MatOfPoint contour:contours){
//			Imgproc.fitEllipse(contour.);
		}
		
	}
}
