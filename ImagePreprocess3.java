package point.instrument;

import java.util.Vector;
import point.instrument.PlotCircle;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImagePreprocess3 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat img = new Mat();
		String imgPath = "C:\\Users\\Administrator\\Desktop\\clock.jpg";// 图片地址
		img = Imgcodecs.imread(imgPath); // 读图像

		InstrumentsRecognition(img);
		
	}

	public static void InstrumentsRecognition(Mat img) {
		// TODO Auto-generated method stub

		double midValue = 0;// 中线值
		double valueOfPerDegree = 1;// 每度数值
		Mat interestingImg = new Mat();// 目标图像
		// Point centerEnd = new Point();// 圆点
		// Point flagPoint = new Point();
		// double slope = 0;// 斜率
		double angle = 0;// 角度
		double read = 0;// 读数

		// 图片大小归一化，size(300,300)
//		Imgproc.resize(img, img, new Size(307, 340));
		Imgcodecs.imwrite(
				"C:\\Users\\Administrator\\Desktop\\normalization.png", img);

		// 灰度图
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);// RGB==>Gray
		Imgproc.blur(gray, gray, new Size(3, 3));

		Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\gray.png", gray);

		// 二值化
		Mat binarization = new Mat();
		Imgproc.adaptiveThreshold(gray, binarization, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25,
				10);// 局部自适应阈值化

		Imgcodecs.imwrite(
				"C:\\Users\\Administrator\\Desktop\\binarization.png",
				binarization);

		// 边缘检测
		Mat edge = new Mat();
		Imgproc.GaussianBlur(gray, gray, new Size(7, 7), 2, 2);// 高斯滤波：光滑处理,降噪
		Imgproc.Canny(gray, edge, 20, 60);// 3,4像素上下限，5矩阵大小3*3
		Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\canny.png", edge);
		// 表盘检测，圆，直线
		Mat circles = new Mat();

		double x1 = 0.0;// x1
		double y1 = 0.0;// y1
		double x2 = 0.0;// x2
		double y2 = 0.0;// y2

		double x = 0.0;// 圓心，x1
		double y = 0.0;// 圓心，y1
		double r = 0.0;// 圆半径，r

		// Vector<Mat> circlesList = new Vector<Mat>();
		int min_radius = 100;
		Imgproc.HoughCircles(edge, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 50,
				200, 30, min_radius, 0);// 检测圆
		System.out.println("circles:" + circles.rows() + "circles"
				+ circles.cols() + "\t" + circles.depth());

		// PlotCircle.plot(img, circles);//绘制所有圆
		// System.exit(0);

		for (int i = 0; i < circles.rows(); i++) {
			double[] data = circles.get(i, 0);
			x = data[0];
			
			y = data[1];
			r = data[2];
			System.out.println("X:" + x + "\t" + "Y:" + y + "\t" + "R:" + r);
			Point center = new Point(x, y);
			// circle center
			Imgproc.circle(img, center, 3, new Scalar(0, 255, 0), -2);
			// circle outline
			Imgproc.circle(img, center, (int) r, new Scalar(0, 0, 255), 2);

			Rect bbox = new Rect((int) Math.abs(x - r), (int) Math.abs(y - r),
					(int) r * 2, (int) r * 2);
			Mat croped_image = new Mat(img, bbox);
			// Imgproc.resize(croped_image, croped_image, new Size(160,
			// 160));//图片大小

			Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\circle.png",
					croped_image);
			interestingImg = croped_image;
		}

		Mat target = new Mat();
		Imgproc.cvtColor(interestingImg, target, Imgproc.COLOR_BGR2GRAY);// RGB==>Gray
		Imgproc.blur(target, target, new Size(3, 3));
		Imgproc.adaptiveThreshold(target, target, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25,
				10);
		Mat lines = new Mat();

		Imgproc.HoughLinesP(target, lines, 1, Math.PI / 180, 30, 80, 8); // 检测直线
		/*
		 * image- 8位，单通道二进制源图像。该图像可以由功能进行修改。
		 * lines-线的输出载体。每行是由一个4元向量（X_1，Y_1，X_2， y_2），其中（X_1，Y_1）和（X_2，y_2）分别检测到的线段的结束点来表示。 
		 * RHO - 以像素为单位累加器的距离分辨率。
		 * theta - 用弧度累加器的角度分辨率。
		 *  threshold- 累加器阈值参数。只有获得足够的票数（>阈值）这些行返回。
		 * minLineLength - 最低线的长度。线段短于那个长度被拒绝。 
		 * maxLineGap -最大允许在同一行上的点之间的间隙将它们连接
		 */
		Vector<Mat> linesList = new Vector<Mat>();
		System.out.println("lines:" + lines.rows() + "\t" + lines.cols() + "\t"
				+ lines.depth());

		double dx1, dx2, dy1, dy2;
		for (int k = 0; k < lines.rows(); k++) {
			double[] data1 = lines.get(k, 0);
			x1 = data1[0];
			y1 = data1[1];
			x2 = data1[2];
			y2 = data1[3];

			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2);
			if ((Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) > Math.pow(
					r / 2, 2)
					&& ((r / 2 < x1 && x1 < r * 1.5 && r / 2 < y1 && y1 < r * 1.5))) {
				System.out.println("x1:" + x1 + "\t" + "y1:" + y1 + "\t"
						+ "x2:" + x2 + "\t" + "y2:" + y2);
				Imgproc.line(interestingImg, start, end, new Scalar(0, 0, 255),
						2);
				linesList.add(interestingImg);

				dx1 = x2 - x1;
				dy1 = y2 - y1;

				dx2 = 0;
				dy2 = 0 - r;

				angle = calculate(dx1, dy1, dx2, dy2);
				read = Math.abs(valueOfPerDegree * angle) + midValue;
			} else if ((Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) > Math
					.pow(r / 2, 2)
					&& r / 2 < x2
					&& x2 < r * 1.5
					&& r / 2 < y2
					&& y2 < r * 1.5) {
				System.out.println("x1:" + x1 + "\t" + "y1:" + y1 + "\t"
						+ "x2:" + x2 + "\t" + "y2:" + y2);
				Imgproc.line(interestingImg, start, end, new Scalar(0, 0, 255),
						2);
				linesList.add(interestingImg);

				dx1 = x1 - x2;
				dy1 = y1 - y2;

				dx2 = 0;
				dy2 = 0 - r;

				angle = calculate(dx1, dy1, dx2, dy2);
				read = -Math.abs(valueOfPerDegree * angle) + midValue;
				angle = 360 -Math.abs(valueOfPerDegree * angle);
			}

			System.out.println("angle:" + angle);

//			System.out.println("read:" + read);
		}

		Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\line.png",
				interestingImg);
		// //形态学处理
		// //进行腐蚀
		// //1、建立腐蚀模板
		// Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, (new
		// Size(15,5)));
		// //2、建立输出图像对象
		// Mat dstImage = new Mat();
		// //3、进行腐蚀
		// Imgproc.dilate(binarization, dstImage, element);//腐蚀
		// Imgproc.erode(dstImage, dstImage, element);//膨胀
		//
		// Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\bi.png",dstImage);

	}

	private static double calculate(double dx1, double dy1, double dx2,
			double dy2) {
		// TODO Auto-generated method stub
		double angle = 0;
		angle = Math.acos((dx1 * dx2 + dy1 * dy2)
				/ (Math.sqrt(dx1 * dx1 + dy1 * dy1) * (float) Math.sqrt(dx2
						* dx2 + dy2 * dy2)));

		if (180 * angle / Math.PI < 180) {
			angle = 180 * angle / Math.PI;
		} else {
			angle = 360 - 180 * angle / Math.PI;
		}
		// slope = (b - d) / (a - c);
		// System.out.println("slope:" + slope);
		// angle = Math.atan(slope) * 180;
		return angle;
	}
}
