/*
 * Notes on status colors:
 * Black is default (-5)
 * Bright Red is (-9)
 * Bright Green is (-1)
 * */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;

public class ForestFire extends JComponent{

	private static final int ROWS = 200;
	private static final int COLS = 380;
	private static final int CELL_SIZE = 5;
	
	private static final double SPONTANIOUS_FIRE_CHANCE = .0000023;
	private static final double FIRE_SPREAD_CHANCE = 2.2;
	private static final double SPONTANIOUS_PLANT = .010;
	
	private Cell[][] map;    //display using this
	private Cell[][] buffer; //write updates to this, then swap pointers

	public ForestFire(){
		map = new Cell[ROWS][COLS];
		buffer = new Cell[ROWS][COLS];
		
		for (int i = 0; i < ROWS; i++) 
			for (int j = 0; j < COLS; j++){
				map[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				map[i][j].set_status(-5);
				buffer[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				buffer[i][j].set_status(-5);
			}
	}
	
	//Get Cell Status
	private int get_CellStatus(int row, int column){
		return map[row][column].get_status();
	}
		
	//Get Neighbor Count (edge wrapping)
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
		//only count hot fires (tmp < -7)
		int tmp = get_CellStatus(rowAbove,colLeft);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(row, colLeft);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(rowBelow, colLeft);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(rowBelow, column);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(rowBelow, colRight);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(row, colRight);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(rowAbove, colRight);
		if(tmp < -7){count += tmp; tmp = 0;}
		tmp = get_CellStatus(rowAbove, column);
		if(tmp < -7){count += tmp; tmp = 0;}
			
		return count;
	}
	
	public void update() {
		for(int r = 0; r < ROWS; r++){
			for(int c = 0; c < COLS; c++){
				int status = get_CellStatus(r, c);
				
				if(status < -5){ //if On Fire, increment
					buffer[r][c].set_status(++status);
					continue;
				} else if(status == -5){ //if empty, check for spontaneous generation
					 int growthChance = (int)(SPONTANIOUS_PLANT * 1000);
					 int randomNum = ThreadLocalRandom.current().nextInt(0, 1000);
					 
					 if(growthChance > randomNum)
						 buffer[r][c].set_status(-4);
					 else
						 buffer[r][c].set_status(-5);
					 continue;
				} else {//if plant, check surroundings
					int neighbors = get_NeighborCount(r,c) * -1; //some value between 6 and 72
					int fireChance, randomNum;
					
					if(neighbors < 5){//if no burning neighbors, try random fire
						fireChance = (int)(SPONTANIOUS_FIRE_CHANCE*100000);
						randomNum = ThreadLocalRandom.current().nextInt(0, 100000);
						if(fireChance >= randomNum){
							buffer[r][c].set_status(-9);
						} else {
							randomNum = ThreadLocalRandom.current().nextInt(0, 30);
							if(randomNum < 1)
								if(status < -1){status++;}
						
							buffer[r][c].set_status(status);
						}
			
					}else { //catch fire from burning neighbors?
						fireChance = neighbors * (int) (FIRE_SPREAD_CHANCE * 100);
						randomNum = ThreadLocalRandom.current().nextInt(0, 10000);
					
						if(fireChance >= randomNum)
							buffer[r][c].set_status(-9);//set fire at max
						else {
							randomNum = ThreadLocalRandom.current().nextInt(0, 30);
							if(randomNum < 1)
								if(status < -1){status++;}
							
							buffer[r][c].set_status(status);
						}
					}
				}
			}//end inner for
		}//end outer for
		
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
