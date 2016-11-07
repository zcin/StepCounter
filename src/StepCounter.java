import java.util.ArrayList;
import java.util.Arrays;

public class StepCounter {

	private double PEAK_HEIGHT_THRESHOLD;
	private double NEAR_MISS_THRESHOLD;
	private int NEAR_MISS_COUNT_THRESHOLD;
	private double PEAK_TROUGH_DIFFERENCE_THRESHOLD;
	private double STANDARD_DEVIATION;

	private double[] magnitudes;
	private double[] times;
	private double[][] sensorData;
	private double[] peaksAndTroughs;
	private int numberSteps;

	public StepCounter(double[] times, double[][] sensorData, int numberSteps) {
		this.times = times;
		this.sensorData = sensorData;
		this.numberSteps = numberSteps;

		this.magnitudes = calculateMagnitudesFor(sensorData);
		this.STANDARD_DEVIATION = calculateStandardDeviation(magnitudes, calculateMean(magnitudes));
		this.PEAK_TROUGH_DIFFERENCE_THRESHOLD = 0.7 * STANDARD_DEVIATION + 2;
		this.NEAR_MISS_COUNT_THRESHOLD = (int) ((1.0 / 10.0) * ((double) numberSteps));
		this.NEAR_MISS_THRESHOLD = (0.4) * STANDARD_DEVIATION;

		peaksAndTroughs = getPeaksAndTroughs();
		resetThreshold();
	}

	/***
	 * Counts the number of steps based on threshold crossings.
	 * 
	 * @return the number of steps based on threshold crossings.
	 */
	public int countStepsByThresholdCrossings() {
		int stepCount = 0;
		boolean ifCrossed = false;
		for (int i = 1; i < magnitudes.length - 1; i++){
			if (magnitudes[i - 1] < PEAK_HEIGHT_THRESHOLD && magnitudes[i] > PEAK_HEIGHT_THRESHOLD) {
				if (ifCrossed)
					stepCount++;
				else
					ifCrossed = true;
			}
		}
		return stepCount;
	}

	/***
	 * Counts the number of steps based on threshold crossings and peak trough threshold
	 * @return the number of steps based on threshold crossings and peak trough threshold
	 */
	public int countStepsByPeakTroughThreshold() {
		int stepCount = 0;
		for (int i = 1; i < magnitudes.length - 1; i++)
			if (magnitudes[i] > PEAK_HEIGHT_THRESHOLD && peaksAndTroughs[i] > PEAK_TROUGH_DIFFERENCE_THRESHOLD)
				stepCount++;
		return stepCount;
	}

	/***
	 * Returns an array that, for every peak, stores the difference between the
	 * previous trough and the current peak.
	 * @return an array that, for every peak, stores the difference between the
	 *         previous trough and the current peak.
	 */
	public double[] getPeaksAndTroughs() {
		double[] arr = new double[times.length];
		for (int i = 1; i < times.length - 1; i++)
			if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1])
				arr[i] = getTroughToPeakDifference(i);
		return arr;
	}

	/***
	 * Finds the difference between the previous trough and the current peak magnitude.
	 * @param index - The index of the current peak
	 * @return the difference between the previous trough and the current peak magnitude.
	 */
	public double getTroughToPeakDifference(int index) {
		for (int i = index; i > 0; i--)
			if (magnitudes[i] < magnitudes[i - 1] && magnitudes[i] < magnitudes[i + 1])
				return magnitudes[index] - magnitudes[i];
		return -1;
	}

	/***
	 * Returns threshold
	 * @return current threshold
	 */
	public double getThreshold() {
		return PEAK_HEIGHT_THRESHOLD;
	}

	/***
	 * Resets the threshold value based on an algorithm contained in the
	 * calculateThreshold method.
	 */
	public void resetThreshold() {
		double currentThreshold = STANDARD_DEVIATION + calculateMean(magnitudes);;
		double newThreshold = currentThreshold;
		do {
			currentThreshold = newThreshold;
			newThreshold = calculateThreshold(NEAR_MISS_THRESHOLD, currentThreshold);
		} while (currentThreshold != newThreshold);
		PEAK_HEIGHT_THRESHOLD = newThreshold;
	}

	/***
	 * Calculates threshold based on the current threshold by lowering the
	 * threshold every time a sufficient number of peaks barely missed the threshold
	 * @param nearMissThreshold - if the peak is less than this amount away from the current
	 * threshold, it is considered a near miss
	 * @param currentThreshold - the current threshold
	 * @return new threshold
	 */
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

	/***
	 * Calculates the magnitude of the 3D vector
	 * @param x - the x component
	 * @param y - the y component 
	 * @param z - the z component
	 * @return the magnitude
	 */
	public static double calculateMagnitude(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/***
	 * Returns an array that contains the magnitudes for each row of the sensor data
	 * @param sensorData - the data array
	 * @return an array of magnitudes
	 */
	public static double[] calculateMagnitudesFor(double[][] sensorData) {
		double[] magnitudes = new double[sensorData.length];
		for (int i = 0; i < sensorData.length; i++)
			magnitudes[i] = calculateMagnitude(sensorData[i][0], sensorData[i][1], sensorData[i][2]);
		return magnitudes;
	}

	/***
	 * Calculates the Standard Deviation of the data array
	 * @param arr - The array of data points
	 * @param mean - the mean of the data set.
	 * @return the Standard Deviation of the data array
	 */
	public static double calculateStandardDeviation(double[] arr, double mean) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++)
			sum += (arr[i] - mean) * (arr[i] - mean);
		return Math.sqrt(sum / (arr.length - 1));
	}

	/***
	 * Returns the Standard Deviation of the magnitudes array
	 * @return the Standard Deviation
	 */
	public double getStandardDeviation() {
		return STANDARD_DEVIATION;
	}

	/***
	 * Calculates the mean of the data array
	 * @param arr - the array of data points
	 * @return the mean of the data array
	 */
	public static double calculateMean(double[] arr) {
		double sum = 0;
		for (int i = 0; i < arr.length; i++)
			sum += arr[i];
		return sum / (double) (arr.length);
	}

	/***
	 * Returns the mean of the magnitudes array
	 * @return the mean
	 */
	public double getMean() {
		return calculateMean(magnitudes);
	}

	/***
	 * Returns a String containing important information about the data array.
	 * @return output string
	 */
	public String toString() {
		return ("peak height threshold: " + PEAK_HEIGHT_THRESHOLD + '\n')
				+ ("near miss threshold: " + NEAR_MISS_THRESHOLD + '\n')
				+ ("near miss count threshold: " + NEAR_MISS_COUNT_THRESHOLD + '\n')
				+ ("peak trough difference threshold: " + PEAK_TROUGH_DIFFERENCE_THRESHOLD + '\n')
				+ ("standard deviation: " + STANDARD_DEVIATION);
	}
}