import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CSVData {
	private double[][] data;
	private String[] columnNames;
	private String filePathToCSV;
	private int numRows;

	public CSVData(String[] lines, String[] columnNames, int startRow) {
		// number of data points
		int n = lines.length - startRow;
		this.numRows = n;
		int numColumns = columnNames.length;
		// create storage for column names
		this.columnNames = columnNames;
		// create storage for data
		this.data = new double[n][numColumns];
		for (int i = 0; i < lines.length - startRow; i++) {
			String line = lines[startRow + i];
			String[] coords = line.split(",");
			for (int j = 0; j < numColumns; j++) {
				if (coords[j].endsWith("#"))
					coords[j] = coords[j].substring(0, coords[j].length() - 1);
				double val = Double.parseDouble(coords[j]);
				data[i][j] = val;
			}
		}
	}

	private static String readFileAsString(String filepath) {
		StringBuilder output = new StringBuilder();
		try (Scanner scanner = new Scanner(new File(filepath))) {
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				output.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	/***
	 * returns a new CSVData object for a file ignoring the lines at the top It
	 * uses the first row as the column names. all other data is stored in 2D
	 * array
	 * 
	 * @param filename
	 *            name of the CSV file
	 * @param numLinesIgnore
	 *            the number of lines to ignore from the top of the file
	 * @param columnHeaders
	 *            the names/descriptions of each column of values
	 * @return CSVData
	 */
	public static CSVData readCSVFile(String filepath, int numLinesIgnore, String[] columnNames) {
		String dataString = readFileAsString(filepath);
		int i1 = 0;
		for(int newLine = 0; newLine < numLinesIgnore; i1++)
			if(dataString.charAt(i1) == '\n') newLine++;
		dataString = dataString.substring(i1);
		String[] lines = dataString.split("\n");
		
		return new CSVData(lines, columnNames, numLinesIgnore);
	}

	/***
	 * returns a new CSVData object for a file ignoring the lines at the top It
	 * uses the first row as the column names. all other data is stored in 2D
	 * array
	 * 
	 * @param filepath to the CSV file
	 * @param numLinesIgnore
	 *            the number of lines to ignore from the top of the file
	 * @return CSVDATA
	 */
	public static CSVData readCSVFile(String filepath, int numLinesIgnore) {
		String dataString = readFileAsString(filepath);
		int i1 = 0, i2;
		for(int newLine = 0; newLine < numLinesIgnore; i1++)
			if(dataString.charAt(i1) == '\n') newLine++;
		for(i2 = i1; dataString.charAt(i2) != '\n'; i2++);
		String[] columnNames = dataString.substring(i1, i2).split(",");
		dataString = dataString.substring(i2);
		String[] lines = dataString.split("\n");
		
		return new CSVData(lines, columnNames, numLinesIgnore);
	}

	/***
	 * returns the individual row out of the array
	 * 
	 * @param rowIndex
	 *            index of the row
	 * @return the array of the rows
	 */

	public double[] getRow(int rowIndex) {
		return null;
	}

	/***
	 * returns the individual col out of the array
	 * 
	 * @param columnIndex
	 *            index of the col
	 * @return returns the array of the columns
	 */

	public double[] getColumn(int columnIndex) {
		return null;
	}

	/***
	 * returns the individual col out of the array
	 * 
	 * @param name
	 *            the name of the col
	 * @return returns the name of the columns
	 */

	public double[] getColumn(String name) {
		return null;
	}

	/***
	 * returns multiple rows out of the data array
	 * 
	 * @param startIndex
	 *            the index you are starting at
	 * @param endIndex
	 *            the index you are ending at
	 * @return returns the array of the rows
	 */

	public double[][] getRows(int startIndex, int endIndex) {
		return null;
	}

	/***
	 * returns multiple rows out of the data array
	 * 
	 * @param rowIndexes
	 *            the array of the indexes of the row
	 * @return returns the array of the rows
	 */

	public double[][] getRows(int[] rowIndexes) {
		return null;
	}

	/***
	 * returns the multiple columns out of the data array
	 * 
	 * @param columnIndexes
	 *            the array of the indexes of the columns
	 * @return returns the array of the columns
	 */

	public double[][] getColumns(int[] columnIndexes) {
		return null;
	}

	/***
	 * returns the multiple rows out of the data array
	 * 
	 * @param startIndex
	 *            the index you are starting at
	 * @param endIndex
	 *            the index you are ending at
	 * @return returns the array of the columns
	 */
	public double[][] getColumns(int startIndex, int endIndex) {
		return null;
	}

	/***
	 * returns the multiple columns out of the data array
	 *
	 * @param colNames
	 *            the name of the column
	 * @return returns the array of the columns
	 */

	public double[][] getColumns(String colNames) {
		return null;
	}

	/***
	 * sets the value of the the element in the array
	 * 
	 * @param rowIndex
	 *            the index of the row
	 * @param colIndex
	 *            the index of the column
	 * @param newValue
	 *            the value you want to set the element to
	 */
	public void setValue(int rowIndex, int colIndex, double newValue) {

	}

	/***
	 * sets the values of the rows
	 * 
	 * @param values
	 *            the array of the row values
	 */

	public void setRow(double[] values) {

	}

	/***
	 * sets the values of the columns
	 * 
	 * @param values
	 *            the array of the col values
	 */

	public void setColumn(double[] values) {

	}

	/***
	 * gets the names of the columns
	 * 
	 * @return returns the array of the column names
	 */

	public String[] getColumnTitles() {
		return null;
	}

	/***
	 * saves the current state of the object back into a CSV
	 * 
	 * @param filename
	 *            the name of the file
	 */
	public void saveToFile(String filename) {

	}
}