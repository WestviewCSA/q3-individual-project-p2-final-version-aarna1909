import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Queue;

public class Runner {
	
	private static String[][] mapArr;  // creates an array for the map to be stored in
	private static int rows;
	private static int cols;
	private static int nums; // total sections
	private static int wolvX;
	private static int wolvY;
	

	
	public static void main(String[] name) {
		try {
			readFile("easyMap1"); // calls the method to run
		} catch(IllegalMapCharacterException e) {
			System.out.println(e.getMessage());
		} catch(IncompleteMapException e) {
			System.out.println(e.getMessage());
		} catch (IncorrectMapFormatException e) {
			System.out.println(e.getMessage());
		} catch (IllegalCommandLineInputException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void readFile(String fileName) throws IllegalMapCharacterException, IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputException {
		File file = new File(fileName);

		try {
		
			Scanner scanner = new Scanner(file);


			// amount of rows, columns, and sections there are in the map is saved to an int variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			//check integers for the first row 
			if(rows <= 0 || cols <= 0 || nums <= 0) {
				 throw new IncorrectMapFormatException("IncorrectMapFormatException");
			}

			
			mapArr = new String[rows*nums][cols]; // 2d array with columns, rows, and sections
			
			for(int r = 0; r < mapArr.length; r++) {
				String newRow = scanner.next(); // gets next value
				
				if(newRow.length() != cols) {
					throw new IncompleteMapException("IncompleteMapException");
				}
								
				for(int c = 0; c < cols; c++) {
					//check for illegal characters
					if(!(newRow.substring(c, c+1).equals("w")) && !(newRow.substring(c, c+1).equals("@")) && !(newRow.substring(c, c+1).equals(".")) && !(newRow.substring(c, c+1).equals("|")) && !(newRow.substring(c, c+1).equals("$"))) {
						 throw new IllegalMapCharacterException("IllegalMapCharacterException");
					
					}
					else {
						mapArr[r][c] = newRow.substring(c, c+1); // getting each character from the string

					}
				}
			}
			
			System.out.println(Arrays.deepToString(mapArr)); // printing map
			scanner.close();


						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//handle exception
		}
	}
	
	public static void readQueueFile(String fileName) throws IllegalMapCharacterException, IncorrectMapFormatException {
		File file = new File(fileName);

		try {
			Scanner scanner = new Scanner(file);
			
			// amount of rows, columns, and sections there are in the map is saved to an int variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());
			
			if(rows <= 0 || cols <= 0 || nums <= 0) {
				 throw new IncorrectMapFormatException("IncorrectMapFormatException");
			}

			mapArr = new String[rows*nums][cols]; // 2d array with columns, rows, and sections

			while(scanner.hasNext()) {
				String value = scanner.next();
				
				if(!value.equals("w") && !value.equals("@") && !value.equals("$") && !value.equals("|")) {
					 throw new IllegalMapCharacterException("IllegalMapCharacterException");
				}
				
				//saves the row, col, and section for each character
				int row = Integer.parseInt(scanner.next());
				int col = Integer.parseInt(scanner.next());
				int num = Integer.parseInt(scanner.next());

				
				//the row location is equal to the row in the file plus the total amount of rows already read 
				if(row >= rows || col >= cols || num>= nums) { 
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
	
	public static void Queue() {
		Queue<HashMap<String, ArrayList<Integer>>> myQueue = new LinkedList<>(); // queue hashmaps
		ArrayList<Integer> coords = new ArrayList<>();
		HashMap<String, ArrayList<Integer>> chars = new HashMap<>(); // hashmap for coordinates: characters and the coordinates
		
		//get position of wolverine buck
		for(int i = 0; i < mapArr.length; i++) {
			for(int c = 0; c < mapArr[0].length; c++ ) {
				if(mapArr[i][c] == "w") {
					coords.add(i);
					coords.add(c);
				}
			}
		}
		
		//enqueue
		
		
	}
	
}


