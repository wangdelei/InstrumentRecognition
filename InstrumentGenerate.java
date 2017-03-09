package point.instrument;

import java.text.BreakIterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class InstrumentGenerate {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Scanner sc = new Scanner(System.in);
		System.out.println("请输入夹角数值(0,360)：");
		double value = sc.nextDouble();

		Mat img = new Mat();
		img = imgGenerate(value);

		Imgcodecs.imwrite("C:\\Users\\Administrator\\Desktop\\tmp.png", img);
	}

	public static Mat imgGenerate(double value) {
		if (value > 180)
			value = value - 360;

		Point center = new Point(170, 130);
		// int r = 120;
		int r = 110;
		Mat img = new Mat();
		// img = new Mat(new Size(300, 300), CvType.CV_8UC3, new Scalar(255,
		// 255, 255));

		String imgPath = "C:\\Users\\Administrator\\Desktop\\8.png";// 图片地址
		img = Imgcodecs.imread(imgPath); // 读图像
		Imgproc.circle(img, center, r, new Scalar(0, 255, 0), 2);
		// Imgproc.circle(img, center, r, new Scalar(0, 0, 0), 2);
		int x = 0, y = 0;
		// Pont O = new Point(150, 150);
		Point O = new Point(170, 130);
		// Random rand = new Random();
		// x = rand.nextInt(240) + 40;
		// if (rand.nextInt(2) == 0) {
		// y = (int) Math.sqrt(Math.pow(r, 2) - Math.pow((x - 170), 2)) + 130;
		// } else {
		// y = -(int) Math.sqrt(Math.pow(r, 2) - Math.pow((x - 170), 2)) + 130;
		// }

		if (value >= 90) {
			System.out.println(Math.sin((180 - value) * Math.PI / 180));
			x = (int) (170 + r
					* Math.abs(Math.sin((180 - value) * Math.PI / 180)));
			y = (int) (130 + r
					* Math.abs(Math.cos((180 - value) * Math.PI / 180)));
		} else if (value >= 0) {
			x = (int) (170 + r * Math.abs(Math.sin(value * Math.PI / 180)));
			y = (int) (130 - r * Math.abs(Math.cos(value * Math.PI / 180)));
		} else if (value >= -90) {
			x = (int) (170 - r * Math.abs(Math.sin(value * Math.PI / 180)));
			y = (int) (130 - r * Math.abs(Math.cos(value * Math.PI / 180)));
		} else {
			x = (int) (170 - r
					* Math.abs(Math.sin((180 - value) * Math.PI / 180)));
			y = (int) (130 + r
					* Math.abs(Math.cos((180 - value) * Math.PI / 180)));
		}

		System.out.println("X:" + x + "\t" + "Y:" + y);
		Point P = new Point(x, y);

		Imgproc.line(img, center, P, new Scalar(0, 0, 0), 1);

		return img;
	}

}
