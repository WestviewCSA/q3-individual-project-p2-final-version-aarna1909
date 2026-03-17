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

	private static String[][] mapArr; // creates an array for the map to be stored in
	private static int rows;
	private static int cols;
	private static int nums; // total sections
	private static int wolvX;
	private static int wolvY;
	private static int goalX;
	private static int goalY;

	public static void main(String[] name) {
		try {
			readFile("easyMap1"); // calls the method to run
		} catch (IllegalMapCharacterException e) {
			System.out.println(e.getMessage());
		} catch (IncompleteMapException e) {
			System.out.println(e.getMessage());
		} catch (IncorrectMapFormatException e) {
			System.out.println(e.getMessage());
		} catch (IllegalCommandLineInputException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void readFile(String fileName) throws IllegalMapCharacterException, IncompleteMapException,
			IncorrectMapFormatException, IllegalCommandLineInputException {
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

			mapArr = new String[rows * nums][cols]; // 2d array with columns, rows, and sections

			for (int r = 0; r < mapArr.length; r++) {
				String newRow = scanner.next(); // gets next value

				if (newRow.length() != cols) {
					throw new IncompleteMapException("IncompleteMapException");
				}

				for (int c = 0; c < cols; c++) {
					// check for illegal characters
					if (!(newRow.substring(c, c + 1).equals("w")) && !(newRow.substring(c, c + 1).equals("@"))
							&& !(newRow.substring(c, c + 1).equals(".")) && !(newRow.substring(c, c + 1).equals("|"))
							&& !(newRow.substring(c, c + 1).equals("$"))) {
						throw new IllegalMapCharacterException("IllegalMapCharacterException");

					} else {
						mapArr[r][c] = newRow.substring(c, c + 1); // getting each character from the string

					}
				}
			}

			System.out.println(Arrays.deepToString(mapArr)); // printing map
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

			// amount of rows, columns, and sections there are in the map is saved to an int
			// variable
			rows = Integer.parseInt(scanner.next());
			cols = Integer.parseInt(scanner.next());
			nums = Integer.parseInt(scanner.next());

			if (rows <= 0 || cols <= 0 || nums <= 0) {
				throw new IncorrectMapFormatException("IncorrectMapFormatException");
			}

			mapArr = new String[rows * nums][cols]; // 2d array with columns, rows, and sections

			while (scanner.hasNext()) {
				String value = scanner.next();

				if (!value.equals("w") && !value.equals("@") && !value.equals("$") && !value.equals("|")) {
					throw new IllegalMapCharacterException("IllegalMapCharacterException");
				}

				// saves the row, col, and section for each character
				int row = Integer.parseInt(scanner.next());
				int col = Integer.parseInt(scanner.next());
				int num = Integer.parseInt(scanner.next());

				// the row location is equal to the row in the file plus the total amount of
				// rows already read
				if (row >= rows || col >= cols || num >= nums) {
					mapArr[row][col] = value;
				} else {
					mapArr[row + (rows * num)][col] = value;
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

			System.out.println(Arrays.deepToString(mapArr)); // prints maps
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
		// -1 means not visited yet
		int[][] parentX = new int[mapArr.length][cols];
		int[][] parentY = new int[mapArr.length][cols];
		for (int r = 0; r < mapArr.length; r++) {
			for (int c = 0; c < cols; c++) {
				parentX[r][c] = -1;
				parentY[r][c] = -1;
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

				// only look at this side if it is possible to move on
				if (sr >= 0 && sr < mapArr.length && sc >= 0 && sc < cols) {
					if (!mapArr[sr][sc].equals("@")) {
						if (!visited[sr][sc]) {
							visited[sr][sc] = true;
							parentX[sr][sc] = r;
							parentY[sr][sc] = c;

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
					parentX[sr][c] = r;
					parentY[sr][c] = c;
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
			int pr = parentX[r][c];
			int pc = parentY[r][c];
			if (!mapArr[r][c].equals("w") && !mapArr[r][c].equals("W") && !mapArr[r][c].equals("$")
					&& !mapArr[r][c].equals("|")) {
				mapArr[r][c] = "+";
			}
			r = pr;
			c = pc;
		}
		// print the whole map
		for (int r2 = 0; r2 < mapArr.length; r2++) {
			String line = "";
			for (int c2 = 0; c2 < cols; c2++) {
				line = line + mapArr[r2][c2];
			}
			System.out.println(line);
		}
	}
}
