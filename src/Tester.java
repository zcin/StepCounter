import java.util.Arrays;

public class Tester {
	public static void main(String[] args) {
		double[] array = {5.6, 5.7, 3.2, 7.6, 1.2};
		System.out.println(Arrays.toString(Plot.runningAverage(array)));
		
		System.out.println(Arrays.toString(Plot.generalRunningAverage(array, 4)));
		
		System.out.println(Arrays.toString(Plot.runningAverageOfThree(array)));
	}
}
