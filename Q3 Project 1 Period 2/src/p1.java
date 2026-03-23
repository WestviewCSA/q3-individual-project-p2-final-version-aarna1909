import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Queue;
import java.util.Stack;

public class p1 {
	
	private static String[][] mapArr; // create an array for the map to be stored in
	private static int rows;
	private static int cols;
	private static int nums;// total sections
	private static int wolvX;
	private static int wolvY;
	private static int goalX;
	private static int goalY;
	private static boolean coordInput = false;
	private static boolean wolvFound = false; 

	public static void main(String[] name) {
		try {
			mapArr = null; // set variables
			rows = 0;
			cols = 0;
			nums = 0;
			wolvX = 0;
			wolvY = 0;
			goalX = -1;
			goalY = -1;
			coordInput = false;
			wolvFound = false;
			
	        String mapFile = name[name.length - 1]; // gets the last item in the array (name: which stores the map name last)
	        
	        boolean useStack = false; // switches
	        boolean inCoord = false;
	        boolean outCoord = false;
	        boolean useQueue = false;
	        boolean useOpt = false;


	         
	        //command line arguments
	        for(int i = 0; i < name.length - 1; i++) { // for loop to check the switches
	            if(name[i].equals("--Stack")) {
	            	useStack = true; 
	            }
	            if(name[i].equals("--Opt")) {
	                useOpt = true;
	            }
	            if(name[i].equals("--Incoordinate")) {
	            	inCoord  = true;
	            }
	            if(name[i].equals("--Outcoordinate")) {
	            	outCoord = true;
	            }
	            if(name[i].equals("--Queue")) {
	                useQueue = true;
	            }
	            
	            if(name[i].equals("--Help")) {
	            	System.out.println("This program is used to guide the Wolverine through a maze to find the Wolverine Buck. ");
	            	System.out.println("'--Stack' : this switch is turned on to use Stack-based pathfinding.");
	            	System.out.println("'--Queue' : this switch is turned on to use Queue-based pathfinding.");
	            	System.out.println("'--Time' : this switch is turned on to print the runtime in seconds.");
	            	System.out.println("'--Opt' : this switch is turned on to use the most optimal path.");
	            	System.out.println("'--Incoordinate' : this switch is turned on to input file is in coordinate format.");
	            	System.out.println("'--Outcoordinate' : this switch is turned on to output in coordinate format.");
	            	System.out.println("'--Help' : this switch is turned on to print this help message.");
	                System.exit(0);
	            }
	        }
	        
	        coordInput = outCoord;
	        
	        if(inCoord) {
	            readQueueFile(mapFile);
	        } else {
	            readFile(mapFile);
	        }
	        
	        if(goalX == -1) {
	        	System.out.println("The Wolverine Store is closed.");
	        	return;
	        }
	        
	        if(useStack) {
	            Stack();
	        } else if(useQueue) {
	            Queue();
	        } else {
	        	Queue();
	        }
			
		} catch(IllegalMapCharacterException e) {
			System.out.println(e.getMessage());
		} catch(IncompleteMapException e) {
			System.out.println(e.getMessage());
		} catch(IncorrectMapFormatException e) {
			System.out.println(e.getMessage());
		} catch(IllegalCommandLineInputException e) {
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
			
			//check the first row 
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
					if(!(newRow.substring(c, c+1).equals("w")) && !(newRow.substring(c, c+1).equals("W")) && !(newRow.substring(c, c+1).equals("@")) && !(newRow.substring(c, c+1).equals(".")) && !(newRow.substring(c, c+1).equals("|")) && !(newRow.substring(c, c+1).equals("$"))) {
						 throw new IllegalMapCharacterException("IllegalMapCharacterException");
					}
					else {
						mapArr[r][c] = newRow.substring(c, c+1); // getting each character from the string
						if((mapArr[r][c].equals("w") || mapArr[r][c].equals("W")) && !wolvFound) {
							wolvX = r;
							wolvY = c;
							wolvFound = true;
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
					// save first w as start, save $ as goal 
					if((value.equals("w") || value.equals("W")) && !wolvFound) {
						wolvX = actualRow;
						wolvY = col;
						wolvFound = true;
					}
					if(value.equals("$")) {
						goalX = actualRow;
						goalY = col;
					}
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

			scanner.close();


						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void Queue() {
		
		// use two ArrayLists to store the row and col of each position in the queue
		// front keeps track of which position looking at next
		ArrayList<Integer> queueRow = new ArrayList<>();
		ArrayList<Integer> queueCol = new ArrayList<>();
		int front = 0;
		
		// enqueue start position
		queueRow.add(wolvX);
		queueCol.add(wolvY);
		
		// visited array keeps track of already checked
		boolean[][] visited = new boolean[mapArr.length][cols];
		
		// arrays store where  came from to trace the path back
		int[][] oldX = new int[mapArr.length][cols];
		int[][] oldY = new int[mapArr.length][cols];
		for(int r = 0; r < mapArr.length; r++) {
			for(int c = 0; c < cols; c++) {
				oldX[r][c] = -1;
				oldY[r][c] = -1;
			}
		}
		visited[wolvX][wolvY] = true;
		
		boolean found = false;
		
		while(front < queueRow.size() && !found) {
			
			// dequeue next location
			int r = queueRow.get(front);
			int c = queueCol.get(front);
			front++; // move to the next item in the queue
			
			// check all 4 sides north, south, east, west
			int[] sideR = {r-1, r+1, r,   r  };
			int[] sideC = {c,   c,   c+1, c-1};
			
			for(int i = 0; i < 4; i++) {
				int sr = sideR[i];
				int sc = sideC[i];
				
				// only look at this side if it is possible to move on
				if(sr >= 0 && sr < mapArr.length && sc >= 0 && sc < cols) {
					if(!mapArr[sr][sc].equals("@")) {
						if(!visited[sr][sc]) {
							
							visited[sr][sc] = true;
							oldX[sr][sc] = r;
							oldY[sr][sc] = c;
							
							// check if any of these spaces hold the coin
							if(sr == goalX && sc == goalY) {
								found = true;
								break;
							}
							
							// enqueue this neighbor to check its neighbors later
							queueRow.add(sr);
							queueCol.add(sc);
						}
					}
				}
			}
			
			//if on "|", jump to the next "w" in the next section
			if(mapArr[r][c].equals("|")) {
				int nextSection = (r / rows) + 1;
				if(nextSection < nums) {
					for(int nr = nextSection * rows; nr < nextSection * rows + rows; nr++) {
						for(int nc = 0; nc < cols; nc++) {
							if((mapArr[nr][nc].equals("w") || mapArr[nr][nc].equals("W")) && !visited[nr][nc]) {
								visited[nr][nc] = true;
								oldX[nr][nc] = r;
								oldY[nr][nc] = c;
								if(nr == goalX && nc == goalY) {
									found = true;
								} else {
									queueRow.add(nr);
									queueCol.add(nc);
								}
							}
						}
					}
				}
			}
		}
		
		if(!found) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		
		// coin found walk backwards from goal to start and mark each step with "+"
		int r = goalX;
		int c = goalY;
		while(!(r == wolvX && c == wolvY)) {
			int pr = oldX[r][c];
			int pc = oldY[r][c];
			if(!mapArr[r][c].equals("w") && !mapArr[r][c].equals("W") && !mapArr[r][c].equals("$") && !mapArr[r][c].equals("|")) {
				mapArr[r][c] = "+";
			}
			// if the parent is a "|", we crossed sections skip over the | and keep tracing
			if(pr != -1 && mapArr[pr][pc].equals("|")) {
				r = pr;
				c = pc;
				continue;
			}
			r = pr;
			c = pc;
		}
		
		// print the solved map
		if(coordInput) {
			// coordinate output only print the "+" path steps
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
			for(int r2 = 0; r2 < mapArr.length; r2++) {
				String line = "";
				for(int c2 = 0; c2 < cols; c2++) {
					line = line + mapArr[r2][c2];
				}
				System.out.println(line);
			}
		}
	}
	
	public static void Stack() {
		
		Stack<ArrayList<Integer>> stack = new Stack<>();
		
		// push start position
		ArrayList<Integer> startPos = new ArrayList<>();
		startPos.add(wolvX);
		startPos.add(wolvY);
		stack.push(startPos);
		
		// visited array keeps track of already checked
		boolean[][] visited = new boolean[mapArr.length][cols];
		
		// arrays store where we came to trace the path back
		int[][] oldX = new int[mapArr.length][cols];
		int[][] oldY = new int[mapArr.length][cols];
		for(int r = 0; r < mapArr.length; r++) {
			for(int c = 0; c < cols; c++) {
				oldX[r][c] = -1;
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
			
			// check all 4 sides north, south, east, west
			int[] sideR = {r-1, r+1, r,   r  };
			int[] sideC = {c,   c,   c+1, c-1};
			
			for(int i = 0; i < 4; i++) {
				int sr = sideR[i];
				int sc = sideC[i];
				
				int curSection = r / rows;
				int srSection = sr / rows;
				if(sr >= 0 && sr < mapArr.length && sc >= 0 && sc < cols && srSection == curSection) {
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
			
			//if on |, go to the next w in the next section
			if(mapArr[r][c].equals("|")) {
				int nextSection = (r / rows) + 1;
				if(nextSection < nums) {
					for(int nr = nextSection * rows; nr < nextSection * rows + rows; nr++) {
						for(int nc = 0; nc < cols; nc++) {
							if((mapArr[nr][nc].equals("w") || mapArr[nr][nc].equals("W")) && !visited[nr][nc]) {
								visited[nr][nc] = true;
								oldX[nr][nc] = r;
								oldY[nr][nc] = c;
								ArrayList<Integer> next = new ArrayList<>();
								next.add(nr);
								next.add(nc);
								stack.push(next);
							}
						}
					}
				}
			}
		}
		
		if(!found) {
			System.out.println("The Wolverine Store is closed.");
			return;
		}
		
		// coin found walk backwards from goal to start and mark each step with +
		int r = goalX;
		int c = goalY;
		while(!(r == wolvX && c == wolvY)) {
			int pr = oldX[r][c];
			int pc = oldY[r][c];
			if(!mapArr[r][c].equals("w") && !mapArr[r][c].equals("W") && !mapArr[r][c].equals("$") && !mapArr[r][c].equals("|")) {
				mapArr[r][c] = "+";
			}
			// if the parent is a |, we crossed sections skip over the | and keep tracing
			if(pr != -1 && mapArr[pr][pc].equals("|")) {
				r = pr;
				c = pc;
				continue;
			}
			r = pr;
			c = pc;
		}
		
		// print the solved map
		if(coordInput) {
		    // coordinate output only print the + path steps
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
		    // text map output print the whole map
		    for(int r2 = 0; r2 < mapArr.length; r2++) {
		        String line = "";
		        for(int c2 = 0; c2 < cols; c2++) {
		            line = line + mapArr[r2][c2];
		        }
		        System.out.println(line);
		    }
		}
	}
	
}