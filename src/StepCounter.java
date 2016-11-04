import java.util.ArrayList;
import java.util.Arrays;

public class StepCounter {

	private double PEAK_HEIGHT_THRESHOLD;
	private double NEAR_MISS_THRESHOLD;
	private int NEAR_MISS_COUNT_THRESHOLD;
	private double STARTING_THRESHOLD;
	private double PEAK_TROUGH_DIFFERENCE_THRESHOLD;
	private double STANDARD_DEVIATION;

	private double[] magnitudes;
	private double[] times;
	private double[][] sensorData;
	private double[] peaksAndTroughs;
	private int numberSteps;

	private static double[] peakTimes;
	private static double[] peakMagnitudes;

	public StepCounter(double[] times, double[][] sensorData, int numberSteps) {
		this.times = times;
		this.sensorData = sensorData;
		this.numberSteps = numberSteps;

		this.magnitudes = calculateMagnitudesFor(sensorData);
		this.STANDARD_DEVIATION = calculateStandardDeviation(magnitudes, calculateMean(magnitudes));
		this.STARTING_THRESHOLD = STANDARD_DEVIATION + calculateMean(magnitudes);
		this.PEAK_TROUGH_DIFFERENCE_THRESHOLD = 0.5*STANDARD_DEVIATION;
		this.NEAR_MISS_COUNT_THRESHOLD = (int) ((1.0 / 10.0) * ((double) numberSteps));
		this.NEAR_MISS_THRESHOLD = (0.4) * STANDARD_DEVIATION;

		peaksAndTroughs = getPeaksAndTroughs();
		resetThreshold();
	}

	public int countStepsByThresholdCrossings() {
		int stepCount = 0;
		boolean ifCrossed = false;
		boolean differenceBigEnough = false;
		for (int i = 1; i < magnitudes.length - 1; i++)
			if (magnitudes[i - 1] < PEAK_HEIGHT_THRESHOLD && magnitudes[i] > PEAK_HEIGHT_THRESHOLD) {
				if(ifCrossed) stepCount++;
				else ifCrossed = true;
			}
		return stepCount;
	}
	
	public int countStepsByPeakTroughThreshold() {
		int stepCount = 0;
		boolean ifCrossed = false;
		boolean differenceBigEnough = false;
		for (int i = 1; i < magnitudes.length - 1; i++)
			if (magnitudes[i] > PEAK_HEIGHT_THRESHOLD && peaksAndTroughs[i] > PEAK_TROUGH_DIFFERENCE_THRESHOLD) 
				stepCount++;
		return stepCount;
	}

	public double[] getPeaksAndTroughs() {
		double[] arr = new double[times.length];
		for (int i = 1; i < times.length - 1; i++)
			if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1])
				arr[i] = getTroughToPeakDifference(i);
		return arr;
	}

	public double getTroughToPeakDifference(int index) {
		for (int i = index; i > 0; i--)
			if (magnitudes[i] < magnitudes[i - 1] && magnitudes[i] < magnitudes[i + 1])
				return magnitudes[index] - magnitudes[i];
		return -1;
	}

	public double getThreshold() {
		return PEAK_HEIGHT_THRESHOLD;
	}

	public double getOriginalThreshold() {
		return STANDARD_DEVIATION + calculateMean(magnitudes);
	}

	public void resetThreshold() {
		double currentThreshold = STARTING_THRESHOLD;
		double newThreshold = currentThreshold;
		do {
			currentThreshold = newThreshold;
			newThreshold = calculateThreshold(NEAR_MISS_THRESHOLD, currentThreshold);
		} while (currentThreshold != newThreshold);
		PEAK_HEIGHT_THRESHOLD = newThreshold;
	}

	public double calculateThreshold(double nearMissThreshold, double currentThreshold) {
		int nearMisses = 0;
		for (int i = 0; i < peaksAndTroughs.length; i++) {
			if (peaksAndTroughs[i] != 0.0 && magnitudes[i] < currentThreshold)
				if (currentThreshold - magnitudes[i] <= nearMissThreshold)
					nearMisses++;
		}
		if (nearMisses >= NEAR_MISS_COUNT_THRESHOLD)
			return currentThreshold - nearMissThreshold;
		return currentThreshold;
	}

	public static double calculateMagnitude(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public static double[] calculateMagnitudesFor(double[][] sensorData) {
		double[] magnitudes = new double[sensorData.length];
		for (int i = 0; i < sensorData.length; i++)
			magnitudes[i] = calculateMagnitude(sensorData[i][0], sensorData[i][1], sensorData[i][2]);
		return magnitudes;
	}

	public static double calculateStandardDeviation(double[] arr, double mean) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++)
			sum += (arr[i] - mean) * (arr[i] - mean);
		return Math.sqrt(sum / (arr.length - 1));
	}

	public double getStandardDeviation() {
		return STANDARD_DEVIATION;
	}

	public static double calculateMean(double[] arr) {
		double sum = 0;
		for (int i = 0; i < arr.length; i++)
			sum += arr[i];
		return sum / (double) (arr.length);
	}

	public double getMean() {
		return calculateMean(magnitudes);
	}

	public String toString() {
		return ("starting threshold: " + STARTING_THRESHOLD + '\n')
				+ ("peak height threshold: " + PEAK_HEIGHT_THRESHOLD + '\n')
				+ ("near miss threshold: " + NEAR_MISS_THRESHOLD + '\n')
				+ ("near miss count threshold: " + NEAR_MISS_COUNT_THRESHOLD + '\n')
				+ ("peak trough difference threshold: " + PEAK_TROUGH_DIFFERENCE_THRESHOLD + '\n')
				+ ("standard deviation: " + STANDARD_DEVIATION);
	}
}