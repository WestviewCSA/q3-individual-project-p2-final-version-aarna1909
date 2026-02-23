import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Runner {
	
	private static String[][] mapArr; 
	private static int rows;
	private static int cols;
	private static int nums;
	
	public static void main(String[] name) {
		readFile("hardMap1");
	}
	
	public static void readFile(String fileName) {
		File file = new File(fileName);

		try {
		
			Scanner scanner = new Scanner(file);
			
			
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			mapArr = new String[rows*nums][cols];

			for(int r = 0; r < mapArr.length; r++) {
				String newRow = scanner.next();
				for(int c = 0; c < cols; c++) {
					mapArr[r][c] = newRow.substring(c, c+1);
				}
			}

			System.out.println(Arrays.deepToString(mapArr));
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
			
			
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			mapArr = new String[rows*nums][cols];

			for

			System.out.println(Arrays.deepToString(mapArr));
			scanner.close();


						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//handle exception
		}
	}
	
}


