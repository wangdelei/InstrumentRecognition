package point.instrument;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class PlotCircle {
	public static void plot(Mat img, Mat circles) {
		Mat tmp = img;
		double a = 0.0;// x1
		double b = 0.0;// y1
		double c = 0.0;// x2
		double d = 0.0;// y2

		double x = 0.0;// 圓心，x1
		double y = 0.0;// 圓心，y1
		double r = 0.0;// 圆半径，r
		for (int i = 0; i < circles.rows(); i++) {

			for (int j = 0; j < circles.cols(); j++) {
				double[] data = circles.get(i, j);
				x = data[0];
				// xtmp[j] = data[0];
				y = data[1];
				// ytmp[j] = data[1];
				// System.out.println("X:" + xtmp[j] + "\t" + "Y:" + ytmp[j]);
				// rtmp[j] = data[2];
				r = data[2];
				// System.out
				// .println("X:" + x + "\t" + "Y:" + y + "\t" + "R:" + r);
				// System.out.println("R：" + rtmp[j]);
				Point center = new Point(x, y);
				// circle center
				Imgproc.circle(tmp, center, 3, new Scalar(0, 255, 0), -2);
				// circle outline
				Imgproc.circle(tmp, center, (int) r, new Scalar(0, 0, 255), 2);

				// Rect bbox = new Rect((int) Math.abs(x - r), (int) Math.abs(y
				// - r), (int) r * 2, (int) r * 2);
				// Mat croped_image = new Mat(img, bbox);
				// Imgproc.resize(croped_image, croped_image, new Size(160,
				// 160));//
				// 图片大小

				Imgcodecs.imwrite(
						"C:\\Users\\Administrator\\Desktop\\allCircle.png", tmp);
				// double flagX = x;
				// double flagY = croped_image.rows();
				// flagPoint = new Point(flagX,flagY);
			}
		}
	}

}
