import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class Plot {
	
	static String filepath = "data/male_stairs copy.txt";
	static CSVData data = CSVData.readCSVFile(filepath, 0); 
	static double[][] accelerometerData = data.getColumns(new String[] { "accelerometer-x", "accelerometer-y", "accelerometer-z" }); 
	
	public static void main(String[] args) {
		ChangeTime.changeTimeStamp(data, 1, filepath);
		
		double rate = 5;
		int size = 100;
		
		double[] accelerometerX = data.getColumn("accelerometer-x");
		double[] accelerometerY = data.getColumn("accelerometer-y");
		double[] accelerometerZ = data.getColumn("accelerometer-z");
		double[] accMagnitudes = StepCounter.calculateMagnitudesFor(accelerometerData);
		
		StepCounter stepcounter = new StepCounter(data.getColumn(0), accelerometerData, 30);
		double beginningThreshold = stepcounter.getOriginalThreshold();
		double threshold = stepcounter.getThreshold();
		double mean = stepcounter.getMean();
		System.out.println("steps1:" + stepcounter.countStepsByThresholdCrossings());
		System.out.println("steps2:" + stepcounter.countStepsByPeakTroughThreshold());
		System.out.println(stepcounter);
		
		
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("Accel", accMagnitudes);
		plot.addLinePlot("threshold", toArray(threshold, accMagnitudes.length));
		//plot.addLinePlot("beginning threshold", toArray(beginningThreshold, accMagnitudes.length));
		plot.addLinePlot("mean", toArray(mean, accMagnitudes.length));
		
		JFrame frame = new JFrame("Accelerometer Data");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);	
	}
	
	public static double[] toArray(double d, int size){
		double[] arr = new double[size];
		for(int i = 0; i < size; i++) arr[i] = d;
		return arr;
	}
	
	
	/***
	 * Returns a new array whose elements are a running average of adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array of length array.length-1 each of whose elements is an adjacent
	 * pair of averaged elements from array
	 */
	public static double[] runningAverage(double[] array) {
		double[] output = new double[array.length-1];
		for(int i = 0; i < array.length-1; i++){
			for(int j = 0; j < 2; j++)
				output[i] += array[i+j];
			output[i] /= 2;
		}	
		return output;
	}

	/***
	 * Returns a new array whose elements are a running average of 3 adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array of length array.length-2 each of whose elements is 3 adjacent
	 * averaged elements from array
	 */
	public static double[] runningAverageOfThree(double[] array) {
		double[] output = new double[array.length-2];
		for(int i = 0; i < array.length-2; i++){
			for(int j = 0; j < 3; j++)
				output[i] += array[i+j];
			output[i] /= 3;
		}	
		return output;
	}
	
	/***
	 * Returns a new array whose elements are each an average of n adjacent elements of array
	 * @param array the sample signal values (to be averaged)
	 * @return double[] an array, each of whose elements is the average of n adjacent values from array
	 */
	public static double[] generalRunningAverage(double[] array, int n) {
		double[] output = new double[array.length-n+1];
		for(int i = 0; i < array.length-n+1; i++){
			for(int j = 0; j < n; j++)
				output[i] += array[i+j];
			output[i]/=n;
		}	
		return output;
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