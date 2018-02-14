/*White Crystals on black background
 * */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;

public class DiffusionAggregation extends JComponent{

	private static final int ROWS = 200;
	private static final int COLS = 380;
	private static final int CELL_SIZE = 5;
	
	private static final int EXPANSION_RATE = 3;
	
	private Cell[][] map;    //display using this
	private Cell[][] buffer; //write updates to this, then swap pointers
	
	public DiffusionAggregation(){
		map = new Cell[ROWS][COLS];
		buffer = new Cell[ROWS][COLS];
		
		for (int i = 0; i < ROWS; i++) 
			for (int j = 0; j < COLS; j++){
				map[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				map[i][j].set_status(1); //black
				buffer[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				buffer[i][j].set_status(1); //black
			}
		map[ROWS/2][COLS/2].set_status(0); //white
	}
	
	//Get Cell Status
	private int get_CellStatus(int row, int column){
		return map[row][column].get_status();
	}
		
	//Get Neighbor Count (implement edge wrapping)
	private int get_NeighborCount(int row, int column){
		int count = 0;
		
		//temp variables for calculation
		int rowAbove = row-1;
		int rowBelow = row+1;
		int colLeft = column-1;
		int colRight = column+1;
			
		//Wrap edges
		if(row == 0){ rowAbove = ROWS - 1; }
		if(column == 0){colLeft = COLS - 1;}
		if(row == ROWS-1){rowBelow = 0;}
		if(column == COLS-1){colRight = 0;}
		
		//starting at top left, work counter-clockwise
		//check for
		int tmp = get_CellStatus(rowAbove, colLeft);
		if(tmp == 0){count++; tmp = -1;}
		tmp = get_CellStatus(row, colLeft);
		if(tmp == 0){count++; tmp = -1;}
		tmp = get_CellStatus(rowBelow, colLeft);
		if(tmp == 0){count++; tmp = -1;}
		
		tmp = get_CellStatus(rowBelow, column);
		if(tmp == 0){count++; tmp = -1;}
		
		tmp = get_CellStatus(rowBelow, colRight);
		if(tmp == 0){count++; tmp = -1;}
		tmp = get_CellStatus(row, colRight);
		if(tmp == 0){count++; tmp = -1;}
		tmp = get_CellStatus(rowAbove, colRight);
		if(tmp == 0){count++; tmp = -1;}
		
		tmp = get_CellStatus(rowAbove, column);
		if(tmp == 0){count++; tmp = -1;}
			
		return count;
	}
	
	public void update() {
		for(int r = 0; r < ROWS; r++){
			for(int c = 0; c < COLS; c++){
				int status = get_CellStatus(r, c);
				
				if(status == 0){//nothing to do here, already in structure
					buffer[r][c].set_status(0);
					continue;
				} else if(status == 2){//check if neighbors, if so, add to structure
					int neighbors = get_NeighborCount(r,c);
					
					if (neighbors == 1){
						buffer[r][c].set_status(0); //white
						continue;
					} else {
						buffer[r][c].set_status(1); //black
						continue;
					}
				} else { //randomly generate new status 2 stuff
					int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
					
					if(randomNum < EXPANSION_RATE)
						buffer[r][c].set_status(2);
					else
						buffer[r][c].set_status(1);
					continue;
				}
			}
		}
		
		Cell[][] temp = map;
		map = buffer;
		buffer = temp;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//render living cells
		for(int r = 0; r < ROWS; r++)
			for(int c = 0; c < COLS; c++) //for each cell
					map[r][c].draw(g2);				//draw
	}

}
