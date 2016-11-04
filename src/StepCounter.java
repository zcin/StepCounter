import java.util.ArrayList;
import java.util.Arrays;

public class StepCounter {

	private double THRESHOLD;
	private double[] magnitudes;
	private double[] times;
	private double[][] sensorData;
	private double[] peaksAndTroughs;
	private int numberSteps;
	
	private static double[] peakTimes;
	private static double[] peakMagnitudes;

	public StepCounter(double[] times, double[][] sensorData, int numberSteps){
		this.times = times;
		this.sensorData = sensorData;
		this.numberSteps = numberSteps;
		magnitudes = calculateMagnitudesFor(sensorData);
		peaksAndTroughs = getPeaksAndTroughs();
		resetThreshold();
	}
	
	public int countSteps1(){
		int stepsCount = 0;
		for(int i = 0; i < peaksAndTroughs.length; i++){
			if(peaksAndTroughs[i] > (1.0/4.0)*getStandardDeviation()*getStandardDeviation()) stepsCount++;
		}
		return stepsCount;
	}
	
	public int countSteps2(){
		int stepCount = 0;
		boolean ifCrossed = false;
		for(int i = 1; i < magnitudes.length-1; i++)
			if(magnitudes[i-1] < THRESHOLD && magnitudes[i] > THRESHOLD) {
				if(ifCrossed) stepCount++;
				else ifCrossed = true;
			}
		return stepCount;
	}
	
	public double[] getPeaksAndTroughs(){
		double[] arr = new double[times.length];
		for(int i = 1; i < times.length-1; i++)
			if(magnitudes[i] > magnitudes[i-1] && magnitudes[i] > magnitudes[i+1])
				arr[i] = magnitudes[i]-getPreviousTrough(i);
		System.out.println(Arrays.toString(arr));
		return arr;
	}
	
	public double getPreviousTrough(int index){
		for(int i = index; i > 0; i--)
			if(magnitudes[i] < magnitudes[i-1] && magnitudes[i] < magnitudes[i+1])
				return magnitudes[i];
		return -1;
	}
	
	public double getThreshold(){
		return THRESHOLD;
	}
	
	public double getOriginalThreshold(){
		return calculateStandardDeviation(magnitudes, calculateMean(magnitudes)) + calculateMean(magnitudes);
	}
	
	public void resetThreshold(){
		double currentThreshold = calculateStandardDeviation(magnitudes, calculateMean(magnitudes)) + calculateMean(magnitudes);
		double newThreshold = currentThreshold;
		do{
			currentThreshold = newThreshold;
			newThreshold = calculateThreshold((0.3)*getStandardDeviation(), currentThreshold);
		} while(currentThreshold != newThreshold);
		THRESHOLD = newThreshold;
	}
	
	public double calculateThreshold(double nearMissThreshold, double currentThreshold){
		int nearMisses = 0;
		for(int i = 0; i < peaksAndTroughs.length; i++){
			if(peaksAndTroughs[i] != 0.0 && magnitudes[i] < currentThreshold)
				if(currentThreshold - magnitudes[i] <= nearMissThreshold) 
					if(peaksAndTroughs[i] >= 3.0) nearMisses++;
		}
		if(nearMisses > (1.0/10.0)*(numberSteps)) return currentThreshold - nearMissThreshold;
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