import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

public class Plot {
	
	static String filepath = "data/female_comfortable_walk.csv";
	static CSVData data = CSVData.readCSVFile(filepath, 0); 
	static double[][] accelerometerData = data.getAccelColumns(); 
	
	public static void main(String[] args) {
		double[] accMagnitudes = StepCounter.calculateMagnitudesFor(accelerometerData);
		
		StepCounter stepcounter = new StepCounter(data.getColumn(0), accelerometerData, 30);
		double threshold = stepcounter.getThreshold();
		System.out.println("steps1:" + stepcounter.countStepsByThresholdCrossings());
		System.out.println("steps2:" + stepcounter.countStepsByPeakTroughThreshold());
		
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("Accel", accMagnitudes);
		plot.addLinePlot("threshold", toArray(threshold, accMagnitudes.length));
		
		JFrame frame = new JFrame("Accelerometer Data");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);	
	}
	
	/***
	 * Returns a double array with the input saved in every element of the array
	 * @param d - the input value
	 * @param size - the size of the array
	 * @return a double array
	 */
	public static double[] toArray(double d, int size){
		double[] arr = new double[size];
		for(int i = 0; i < size; i++) arr[i] = d;
		return arr;
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

}