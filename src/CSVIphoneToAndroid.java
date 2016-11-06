import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVIphoneToAndroid {
	
	public static void iphoneToAndroidFormat(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		
		String data = "@ HyperIMU - rohanrodrigues - 2016\n@ Date: Fri Nov 04 10:30:03 PDT 2016\n@ Sensors: BMI160_accelerometer - BMI160_gyroscope\n@ SamplingTime: 100ms" + "\n";
		String columnNames = "time,accelerometer-x,accelerometer-y,accelerometer-z,gyro-x,gyro-y,gyro-z" + "\n";
		data += "\n" + columnNames;
		String temp = "";
		int count = 0;
		
		while ((temp = br.readLine()) != null) {
			count++;
			if (count <= 3) {
				continue;
			}		
			if (temp.contains(",,,,,,,,,,,,,,,,")) {
				continue;
			}
			
			String[] columns = temp.split(",");
			String timestamp = columns[0];
			String accelX = columns[10];
			String accelY = columns[11];
			String accelZ = columns[12];
			String gyroX = columns[3];
			String gyroY = columns[4];
			String gyroZ = columns[5];
			
			String finalTemp;
			if (count == 4) {
				finalTemp = timestamp + ",0.0,0.0,0.0,0.0,0.0,0.0#";
			}
			else {
				finalTemp = timestamp + "," + accelX + "," + accelY + "," + accelZ + "," + gyroX + "," + gyroY + "," + gyroZ + "#";
			}
			data += finalTemp + "\n"; 
		}
		
		String outputLocation = fileName;
		File file = new File(outputLocation);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(data);
		bw.close();
	}

}
