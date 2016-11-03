import java.util.ArrayList;

public class StepCounter {

	private static double THRESHOLD;
	private double[] magnitudes;
	private double[] times;
	private double[][] sensorData;
	
	private static double[] peakTimes;
	private static double[] peakMagnitudes;

	public StepCounter(double[] times, double[][] sensorData){
		this.times = times;
		this.sensorData = sensorData;
		magnitudes = calculateMagnitudesFor(sensorData);
		THRESHOLD = calculateStandardDeviation(magnitudes, calculateMean(magnitudes)) + calculateMean(magnitudes);
	}
	
	public int countSteps(){
		THRESHOLD = calculateStandardDeviation(magnitudes, calculateMean(magnitudes));
		int stepCount = 0;
		for(int i = 1; i < times.length; i++)
			if(magnitudes[i] > magnitudes[i-1] && magnitudes[i] > magnitudes[i+1])
				if(magnitudes[i] > THRESHOLD) stepCount++;
		return stepCount;
	}
	
	public int[] getPeaks(){
		int[] peaks = new int[times.length];
		for(int i = 1; i < times.length-1; i++)
			if(magnitudes[i] > magnitudes[i-1] && magnitudes[i] > magnitudes[i+1])
				peaks[i] = 1;
		return peaks;
	}
	
	public static double calculateMagnitude(double x, double y, double z){
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	private static double[] calculateMagnitudesFor(double[][] sensorData){
		double[] magnitudes = new double[sensorData.length];
		for(int i = 0; i < sensorData.length; i++)
			magnitudes[i] = calculateMagnitude(sensorData[i][0], sensorData[i][1], sensorData[i][2]);
		return magnitudes;
	}
	
	private static double calculateStandardDeviation(double[] arr, double mean){
		int sum = 0;
		for(int i = 0; i < arr.length; i++)
			sum+=(arr[i]-mean)*(arr[i]-mean);
		return Math.sqrt(sum/(arr.length-1));
	}
	
	public double getStandardDeviation(){
		return calculateStandardDeviation(magnitudes, calculateMean(magnitudes));
	}
	
	private static double calculateMean(double[] arr){
		double sum = 0;
		for(int i = 0; i < arr.length; i++) sum+=arr[i];
		return sum/(double)(arr.length);
	}
}