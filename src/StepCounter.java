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
		resetThreshold();
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
	
	public double getThreshold(){
		return THRESHOLD;
	}
	
	public void resetThreshold(){
		int[] peaks = getPeaks();
		double currentThreshold = calculateStandardDeviation(magnitudes, calculateMean(magnitudes)) + calculateMean(magnitudes);
		double newThreshold = currentThreshold;
		do{
			currentThreshold = newThreshold;
			newThreshold = calculateThreshold((0.2)*getStandardDeviation(), currentThreshold, peaks);
		} while(currentThreshold != newThreshold);
		THRESHOLD = newThreshold;
	}
	
	public double calculateThreshold(double nearMissThreshold, double currentThreshold, int[] peaks){
		int nearMisses = 0;
		for(int i = 0; i < peaks.length; i++){
			if(peaks[i] == 1 && magnitudes[i] < currentThreshold)
				if(currentThreshold - magnitudes[i] <= nearMissThreshold) nearMisses++;
		}
		if(nearMisses > 3) return currentThreshold - nearMissThreshold;
		return currentThreshold;
	}
	
	public static double calculateMagnitude(double x, double y, double z){
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public static double[] calculateMagnitudesFor(double[][] sensorData){
		double[] magnitudes = new double[sensorData.length];
		for(int i = 0; i < sensorData.length; i++)
			magnitudes[i] = calculateMagnitude(sensorData[i][0], sensorData[i][1], sensorData[i][2]);
		return magnitudes;
	}
	
	public static double calculateStandardDeviation(double[] arr, double mean){
		int sum = 0;
		for(int i = 0; i < arr.length; i++)
			sum+=(arr[i]-mean)*(arr[i]-mean);
		return Math.sqrt(sum/(arr.length-1));
	}
	
	public double getStandardDeviation(){
		return calculateStandardDeviation(magnitudes, calculateMean(magnitudes));
	}
	
	public static double calculateMean(double[] arr){
		double sum = 0;
		for(int i = 0; i < arr.length; i++) sum+=arr[i];
		return sum/(double)(arr.length);
	}
	
	public double getMean(){
		return calculateMean(magnitudes);
	}
}