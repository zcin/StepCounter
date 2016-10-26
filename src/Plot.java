import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class Plot {
	public static void main(String[] args) {
		double rate = 5;
		int size = 100;
		
		String filepath = "/Users/rohanrodrigues/Documents/GyroTest2out.csv";
		CSVData data = CSVData.readCSVFile(filepath, 5);
		
		double[] gyroX = data.getColumn("");
		double[] gyroY = data.getColumn("");
		double[] gyroZ = data.getColumn("");
		
		Plot2DPanel plot = new Plot2DPanel();
		// add a line plot to the PlotPanel
		plot.addLinePlot("Gyro-x", gyroX);
		plot.addLinePlot("Gyro-y", gyroY);
		plot.addLinePlot("Gyro-z", gyroZ);
		
		//plot.addLinePlot("Output of Running Average", result);

		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("Smoothing result");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}
	
	/***
	 * Returns a new array whose elements are a running average of adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array of length array.length-1 each of whose elements is an adjacent
	 * pair of averaged elements from array
	 */
	public static double[] runningAverage(double[] array) {
		return null;
	}

	/***
	 * Returns a new array whose elements are a running average of 3 adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array of length array.length-2 each of whose elements is 3 adjacent
	 * averaged elements from array
	 */
	public static double[] runningAverageOfThree(double[] array) {
		return null;
	}
	
	/***
	 * Returns a new array whose elements are each an average of n adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array, each of whose elements is the average of n adjacent values from array
	 */
	public static double[] generalRunningAverage(double[] array, int n) {
		return null;
	}
	
	/***
	 * Return a randomly generated sample array of size size, whose values are mean +/- noiseAmount.
	 * @param size the size of the array
	 * @param mean the mean value of all the numbers in the array
	 * @param noiseAmount the magnitude of the maximum deviation from the mean of any value.
	 * @return an array of the sample data
	 */
	public static double[] getRandomSample(int size, double mean, double noiseAmount) {
		double[] sample = new double[size];
		for (int i = 0; i < sample.length; i++)
			sample[i] = mean + Math.random()*(2*noiseAmount) - noiseAmount;
		return sample;
	}
}