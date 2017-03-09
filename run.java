package point.instrument;

import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class run {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入夹角数值(0,360)：");
		double value = sc.nextDouble();
		
		Mat img = new Mat();
		img = InstrumentGenerate.imgGenerate(value);
		
		ImagePreprocess3.InstrumentsRecognition(img);
	}
}
