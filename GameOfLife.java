import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JComponent;

public class GameOfLife extends JComponent{
	private static final int ROWS = 100;
	private static final int COLS = 190;
	private static final int CELL_SIZE = 10;
	
	private Cell[][] map;    //display using this
	private Cell[][] buffer; //write updates to this, then swap pointers
	
	public GameOfLife(){
		map = new Cell[ROWS][COLS];
		buffer = new Cell[ROWS][COLS];
		
		for (int i = 0; i < ROWS; i++) 
			for (int j = 0; j < COLS; j++){
				map[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				buffer[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
			}
		
		for(int i = 0; i < ROWS*COLS / 2; i++){
			int randX = ThreadLocalRandom.current().nextInt(0, ROWS-1);
			int randY = ThreadLocalRandom.current().nextInt(0, COLS-1);
			map[randX][randY].set_status(1);
		}
		
		//TODO:initialize starting values in better way
		//map[0][1].set_status(1);
		//map[0][2].set_status(1);
		//map[0][3].set_status(1);
		//map[1][3].set_status(1);
		//map[2][2].set_status(1);
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
		count += get_CellStatus(rowAbove,colLeft);
		count += get_CellStatus(row, colLeft);
		count += get_CellStatus(rowBelow, colLeft);
		count += get_CellStatus(rowBelow, column);
		count += get_CellStatus(rowBelow, colRight);
		count += get_CellStatus(row, colRight);
		count += get_CellStatus(rowAbove, colRight);
		count += get_CellStatus(rowAbove, column);
		
		return count;
	}
	
	//update map according to GOL rules
	public void update(){
		
		for(int r = 0; r < ROWS; r++)
			for(int c = 0; c < COLS; c++){//for each cell in buffer
				int neighbors = get_NeighborCount(r,c);
				int alive = get_CellStatus(r,c);

				//rules: if dead, needs 3 n for life::: else needs 2 or 3 n for life
				if(alive == 0){
					if(neighbors == 3)
						buffer[r][c].set_status(1);
					else
						buffer[r][c].set_status(0);
					
				} else {
					if(neighbors == 2 || neighbors == 3)
						buffer[r][c].set_status(1);
					else
						buffer[r][c].set_status(0);
				}
			}//end inner for loop
		
		//TODO: does this work? swap buffers
		Cell[][] temp = map;
		map = buffer;
		buffer = temp;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//render background white
		Rectangle background = new Rectangle(0, 0, WIDTH, HEIGHT);
		g2.setColor(Color.WHITE);
		g2.fill(background);
		g2.draw(background);
		
		//render living cells
		for(int r = 0; r < ROWS; r++)
			for(int c = 0; c < COLS; c++) //for each cell
				if(map[r][c].get_status() == 1) //if alive
					map[r][c].draw(g2);				//draw
	}
}
