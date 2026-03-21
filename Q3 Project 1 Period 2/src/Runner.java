import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Queue;

public class Runner {

	private static String[][] mapArr; // creates an array for the map to be stored in
	private static int rows;
	private static int cols;
	private static int nums; // total sections
	private static int wolvX;
	private static int wolvY;
	private static int goalX;
	private static int goalY;
	private static boolean coordInput = false;

	public static void main(String[] name) {
		try {
			String mapFile = "hardMap2"; // change this to your map file name
			
			// peek at the 4th token in the file
			// coordinate maps have single character tokens like "w", "@", "$"
			// text maps have full row tokens like "@@@@.w" which are longer than 1 character
			Scanner check = new Scanner(new File(mapFile));
			check.next(); // skip rows number
			check.next(); // skip cols number
			check.next(); // skip sections number
			String firstToken = check.next(); // first map token
			check.close();
			
			// if the token is 1 character long it is coordinate format
			if(firstToken.length() == 1) {
				coordInput = true;
				readQueueFile(mapFile);
			} else {
				coordInput = false;
				readFile(mapFile);
			}
			
			Queue();
			
		} catch(IllegalMapCharacterException e) {
			System.out.println(e.getMessage());
		} catch(IncompleteMapException e) {
			System.out.println(e.getMessage());
		} catch(IncorrectMapFormatException e) {
			System.out.println(e.getMessage());
		} catch(IllegalCommandLineInputException e) {
			System.out.println(e.getMessage());
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void readFile(String fileName) throws IllegalMapCharacterException, IncompleteMapException, IncorrectMapFormatException, IllegalCommandLineInputException {
		File file = new File(fileName);

		try {

			Scanner scanner = new Scanner(file);

			// amount of rows, columns, and sections there are in the map is saved to an int
			// variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());

			// check integers for the first row
			if (rows <= 0 || cols <= 0 || nums <= 0) {
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
					if(!(newRow.substring(c, c+1).equals("w")) && !(newRow.substring(c, c+1).equals("W")) && !(newRow.substring(c, c+1).equals("@")) && !(newRow.substring(c, c+1).equals(".")) && !(newRow.substring(c, c+1).equals("|")) && !(newRow.substring(c, c+1).equals("$"))) {
						throw new IllegalMapCharacterException("IllegalMapCharacterException");
					}
					else {
						mapArr[r][c] = newRow.substring(c, c+1); // getting each character from the string
						// save wolverine start and goal position
						if((mapArr[r][c].equals("w") || mapArr[r][c].equals("W")) && wolvX == 0 && wolvY == 0) {
							wolvX = r;
							wolvY = c;
						}
						if(mapArr[r][c].equals("$")) {
							goalX = r;
							goalY = c;
						}
					}
				}
			}
			
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// handle exception
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

			if (rows <= 0 || cols <= 0 || nums <= 0) {
				throw new IncorrectMapFormatException("IncorrectMapFormatException");
			}

			mapArr = new String[rows * nums][cols]; // 2d array with columns, rows, and sections

			while(scanner.hasNext()) {
				String value = scanner.next();
				
				if(!value.equals("w") && !value.equals("W") && !value.equals("@") && !value.equals("$") && !value.equals("|") && !value.equals(".")) {
					throw new IllegalMapCharacterException("IllegalMapCharacterException");
				}
				
				//saves the row, col, and section for each character
				int row = Integer.parseInt(scanner.next());
				int col = Integer.parseInt(scanner.next());
				int num = Integer.parseInt(scanner.next());

				//the row location is equal to the row in the file plus the total amount of rows already read 
				int actualRow = row + (rows * num);
				if(row < rows && col < cols && num < nums) {
					mapArr[actualRow][col] = value;
					// save wolverine start and goal positions
					if(value.equals("w") || value.equals("W")) {
						wolvX = actualRow;
						wolvY = col;
					}
					if(value.equals("$")) {
						goalX = actualRow;
						goalY = col;
					}
				}
			}
			// iterates through the map and fills all blank or "null" values with a period
			for (int r = 0; r < mapArr.length; r++) {
				for (int c = 0; c < cols; c++) {
					if (mapArr[r][c] == null) {
						mapArr[r][c] = ".";
					}
				}
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// handle exception
		}
	}

	public static void Queue() {
		// use two ArrayLists to store the row and col of each position in the queue
		// front keeps track of which position we are looking at next
		ArrayList<Integer> queueRow = new ArrayList<>();
		ArrayList<Integer> queueCol = new ArrayList<>();
		int front = 0;

		// enqueue start position
		queueRow.add(wolvX);
		queueCol.add(wolvY);

		// visited array keeps track of already checked
		boolean[][] visited = new boolean[mapArr.length][cols];

		// arrays store old so can trace the path back
		int[][] oldX = new int[mapArr.length][cols];
		int[][] oldY = new int[mapArr.length][cols];
		for (int r = 0; r < mapArr.length; r++) {
			for (int c = 0; c < cols; c++) {
				oldX[r][c] = -1; // -1 means not visited yet
				oldY[r][c] = -1;
			}
		}
		
		visited[wolvX][wolvY] = true;

		boolean found = false;

		while (front < queueRow.size() && !found) {

			// dequeue next location
			int r = queueRow.get(front);
			int c = queueCol.get(front);
			front++; // move to the next item in the queue

			// check all 4 sides — north, south, east, west
			int[] sideR = { r - 1, r + 1, r, r };
			int[] sideC = { c, c, c + 1, c - 1 };

			for (int i = 0; i < 4; i++) {
				int sr = sideR[i];
				int sc = sideC[i];

				// only look at side if it is possible to move on
				if (sr >= 0 && sr < mapArr.length && sc >= 0 && sc < cols) {
					if (!mapArr[sr][sc].equals("@")) {
						if (!visited[sr][sc]) {
							visited[sr][sc] = true;
							oldX[sr][sc] = r;
							oldY[sr][sc] = c;

							// check if any spaces hold the coin
							if (sr == goalX && sc == goalY) {
								found = true;
								break;
							}

							// enqueue this side to check next side later
							queueRow.add(sr);
							queueCol.add(sc);
						}
					}
				}
			}

			// if on the portal connection - connect to next section
			if (mapArr[r][c].equals("|")) {
				int sr = r + rows; // same row/col but one section down
				if (sr < mapArr.length && !mapArr[sr][c].equals("@") && !visited[sr][c]) {
					visited[sr][c] = true;
					oldX[sr][c] = r;
					oldY[sr][c] = c;
					if (sr == goalX && c == goalY) {
						found = true;
					} else {
						queueRow.add(sr);
						queueCol.add(c);
					}
				}
			}
		}

		if (!found) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}

		// coin found — walk from goal to start and mark each step with +
		int r = goalX;
		int c = goalY;
		while (!(r == wolvX && c == wolvY)) {
			int pr = oldX[r][c];
			int pc = oldY[r][c];
			if (!mapArr[r][c].equals("w") && !mapArr[r][c].equals("W") && !mapArr[r][c].equals("$")
					&& !mapArr[r][c].equals("|")) {
				mapArr[r][c] = "+";
			} 
			r = pr;
			c = pc;
			// print the whole map
			if(coordInput) {
				// coordinate output — only print the + path steps
				for(int r2 = 0; r2 < mapArr.length; r2++) {
					for(int c2 = 0; c2 < cols; c2++) {
						if(mapArr[r2][c2].equals("+")) {
							int section = r2 / rows;
							int localRow = r2 % rows;
							System.out.println("+ " + localRow + " " + c2 + " " + section);
						}
					}
				}
				} else {
					for (int r2 = 0; r2 < mapArr.length; r2++) {
						String line = "";
						for (int c2 = 0; c2 < cols; c2++) {
							line = line + mapArr[r2][c2];
						}
						System.out.println(line);
					}
			}
		}
	}
	
	public static void Stack() {
		Stack<ArrayList<Integer>> stack = new Stack<>();
		ArrayList<Integer> start = new ArrayList<>();
		
		start.add(wolvX); // add start to arraylsit
		start.add(wolvY);
		
		stack.push(start); // push start to stack
		
		boolean[][] visited = new boolean[mapArr.length][cols]; //store visited values 
		int[][] oldX = new int[mapArr.length][cols]; // store old values
		int[][] oldY = new int[mapArr.length][cols];
		
		for(int r = 0; r < mapArr.length; r++) { //iterate through rows and cols 
			for(int c = 0; c < cols; c++) {
				oldX[r][c] = -1; // not visited
				oldY[r][c] = -1; 
			}
		}
		
		visited[wolvX][wolvY] = true;
		boolean found = false;
		
		while(!stack.isEmpty() && !found) {
			// pop next location
			ArrayList<Integer> cur = stack.pop();
			int r = cur.get(0);
			int c = cur.get(1);
			
			if(r == goalX && c == goalY) {
				found = true;
				break;
			}
			
			// check all 4 sides — north, south, east, west
			int[] sideR = {r-1, r+1, r,   r  };
			int[] sideC = {c,   c,   c+1, c-1};
			
			for(int i = 0; i < 4; i++) {
				int sr = sideR[i];
				int sc = sideC[i];
				
				if(sr >= 0 && sr < mapArr.length && sc >= 0 && sc < cols) {
					if(!mapArr[sr][sc].equals("@")) {
						if(!visited[sr][sc]) {
							visited[sr][sc] = true;
							oldX[sr][sc] = r;
							oldY[sr][sc] = c;
							ArrayList<Integer> next = new ArrayList<>();
							next.add(sr);
							next.add(sc);
							stack.push(next);
						}
					}
				}
			}
			
			// if on the portal connection, connect to next section
			if(mapArr[r][c].equals("|")) {
				int sr = r + rows;
				if(sr < mapArr.length && !mapArr[sr][c].equals("@") && !visited[sr][c]) {
					visited[sr][c] = true;
					oldX[sr][c] = r;
					oldY[sr][c] = c;
					ArrayList<Integer> next = new ArrayList<>();
					next.add(sr);
					next.add(c);
					stack.push(next);
				}
			}
		}
		
		if(!found) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		
		// coin found — walk backwards from goal to start and mark each step with +
		int r = goalX;
		int c = goalY;
		while(!(r == wolvX && c == wolvY)) {
			int pr = oldX[r][c];
			int pc = oldY[r][c];
			if(!mapArr[r][c].equals("w") && !mapArr[r][c].equals("W") && !mapArr[r][c].equals("$") && !mapArr[r][c].equals("|")) {
				mapArr[r][c] = "+";
			}
			r = pr;
			c = pc;
		}
		
		// print the solved map
		for(int r2 = 0; r2 < mapArr.length; r2++) {
			String line = "";
			for(int c2 = 0; c2 < cols; c2++) {
				line = line + mapArr[r2][c2];
			}
			System.out.println(line);
		}

		
	}
}
