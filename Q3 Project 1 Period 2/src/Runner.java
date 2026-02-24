import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Runner {
	
	private static String[][] mapArr;  // creates an array for the map to be stored in
	private static int rows;
	private static int cols;
	private static int nums; // total sections
	
	public static void main(String[] name) {
		readQueueFile("hardMap1c"); // calls the method to run
	}
	
	public static void readFile(String fileName) {
		File file = new File(fileName);

		try {
		
			Scanner scanner = new Scanner(file);
			
			// amount of rows, columns, and sections there are in the map is saved to an int variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			mapArr = new String[rows*nums][cols]; // 2d array with columns, rows, and sections

			for(int r = 0; r < mapArr.length; r++) {
				String newRow = scanner.next(); // gets next value
				
				for(int c = 0; c < cols; c++) {
					mapArr[r][c] = newRow.substring(c, c+1); // getting each character from the string
				}
			}

			System.out.println(Arrays.deepToString(mapArr)); // printing map
			scanner.close();


						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//handle exception
		}
	}
	
	public static void readQueueFile(String fileName) {
		File file = new File(fileName);

		try {
			Scanner scanner = new Scanner(file);
			
			// amount of rows, columns, and sections there are in the map is saved to an int variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			mapArr = new String[rows*nums][cols]; // 2d array with columns, rows, and sections
			
			while(scanner.hasNext()) {
				String value = scanner.next();
				
				//saves the row, col, and section for each character
				int row = Integer.parseInt(scanner.next());
				int col = Integer.parseInt(scanner.next());
				int num = Integer.parseInt(scanner.next());
				
				//the row location is equal to the row in the file plus the total amount of rows already read 
				if(nums == 0) {
					mapArr[row][col] = value;
				}
				else {
					mapArr[row + (rows*num)][col] = value;
				}
			}
			
			//iterates through the map and fills all blank or "null" values with a period
			for(int r = 0; r < mapArr.length; r++) {
				for(int c = 0; c < cols; c++) {
					if(mapArr[r][c] == null) {
						mapArr[r][c] = ".";
					}
				}
			}

			System.out.println(Arrays.deepToString(mapArr)); // prints maps
			scanner.close();


						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//handle exception
		}
	}
	
}


