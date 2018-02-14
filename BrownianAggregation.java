import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;

/*5% of space is filled with particles
 * particles move randomly, but repel each other. (two particles cannot occupy same space)
 * if particles are next to each other, they move apart in opposite direction
 * */

public class BrownianAggregation extends JComponent{
	private static final int NUM_PARTICLES = 1200;	//1000 is more realistic, but much slower
	private static final int ROWS = 200;
	private static final int COLS = 380;
	private static final int CELL_SIZE = 5;
	
	private static final int EXPANSION_RATE = 3;
	
	private Cell[][] map;    //display using this
	private Cell[][] buffer; //write updates to this, then swap pointers
	
	public BrownianAggregation(){
		map = new Cell[ROWS][COLS];
		buffer = new Cell[ROWS][COLS];
		
		for (int i = 0; i < ROWS; i++) 
			for (int j = 0; j < COLS; j++){
				map[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				map[i][j].set_status(1); //black
				buffer[i][j] = new Cell(j*CELL_SIZE, i*CELL_SIZE, CELL_SIZE);
				buffer[i][j].set_status(1); //black
			}
		
		for (int i = 0; i < NUM_PARTICLES-1; i++){
			//get random starting positions
			int randX = ThreadLocalRandom.current().nextInt(0, ROWS-1);
			int randY = ThreadLocalRandom.current().nextInt(0, COLS-1);
			int randDir = ThreadLocalRandom.current().nextInt(0, 8);
		
			map[randX][randY].set_status(2); //grey
			map[randX][randY].set_direction(randDir);
		}
		map[ROWS/2][COLS/2].set_status(0); //white
		map[ROWS/2][COLS/2].set_direction(-1);//don't move
	}
	
	
	/*given cell coordinates, 
	 * determine if collision with structure
	 * determine if collision with particle*/
	private void moveDirection(int row, int column){
		
		//temp variables for calculation
		int direction = map[row][column].get_direction();
		int rowAbove = row-1;
		int rowBelow = row+1;
		int colLeft = column-1;
		int colRight = column+1;
		
		//assign 2% of particles a random direction
		int randDir = ThreadLocalRandom.current().nextInt(0, 100);
		if(randDir < 3)
			direction = ThreadLocalRandom.current().nextInt(0, 8);
		
		//Wrap edges
		if(row == 0){ rowAbove = ROWS - 1; }
		if(column == 0){colLeft = COLS - 1;}
		if(row == ROWS-1){rowBelow = 0;}
		if(column == COLS-1){colRight = 0;}
		
		//check if collision with structure, if collision, return
		if(map[rowAbove][column].get_status() == 0){ //if collides with structure
			buffer[row][column].set_status(0);
			return; //exit function... work here is done
		} else if(map[rowAbove][colRight].get_status() == 0){ buffer[row][column].set_status(0); return;}
		else if(map[row][colRight].get_status() == 0){ buffer[row][column].set_status(0);return;}
		else if(map[rowBelow][colRight].get_status() == 0){ buffer[row][column].set_status(0);return;}
		else if(map[rowBelow][column].get_status() == 0){ buffer[row][column].set_status(0);return;}
		else if(map[rowBelow][colLeft].get_status() == 0){ buffer[row][column].set_status(0);return;}
		else if(map[row][colLeft].get_status() == 0){ buffer[row][column].set_status(0);return;}
		else if(map[rowAbove][colLeft].get_status() == 0){ buffer[row][column].set_status(0);return;}
		
		
		
		
		//check for nearby neighbors, if nearby neighbor update buffer and change direction
		if(map[rowAbove][column].get_status() == 2){ //neighbor above
			//if direction 4, 5, 3, or 0, 	go 4
			//if direction 1 or 2, 			direction++
			//if direction 6 or 7, 			direction--
			if(direction == 1 || direction == 2){
				buffer[row][column].set_status(2);
				buffer[row][column].set_direction(++direction);
			} else if(direction == 6 || direction == 7){
				buffer[row][column].set_status(2);
				buffer[row][column].set_direction(--direction);
			} else {
				buffer[row][column].set_direction(4);
			}
		} else if(map[rowAbove][colRight].get_status() == 2){ //neighbor above right
			//if direction 5, 6, 4, or 1, go 5
			//if direction 2 or 3, direction++
			//if direction 7 or 0, direction--
			if(direction == 2 || direction == 3){buffer[row][column].set_direction(++direction);}
			else if(direction == 7 || direction == 0){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(5);}
		} else if(map[row][colRight].get_status() == 2){ //neighbor right
			if(direction == 3 || direction == 4){buffer[row][column].set_direction(++direction);}
			else if(direction == 0 || direction == 1){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(6);}
		}else if(map[rowBelow][colRight].get_status() == 2){ //neighbor down right
			if(direction == 4 || direction == 5){buffer[row][column].set_direction(++direction);}
			else if(direction == 1 || direction == 2){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(7);}
		}else if(map[rowBelow][column].get_status() == 2){ //neighbor down
			if(direction == 5 || direction == 6){buffer[row][column].set_direction(++direction);}
			else if(direction == 2 || direction == 3){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(0);}
		}else if(map[rowBelow][colLeft].get_status() == 2){ //neighbor down left
			if(direction == 6 || direction == 7){buffer[row][column].set_direction(++direction);}
			else if(direction == 3 || direction == 4){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(1);}
		}else if(map[row][colLeft].get_status() == 2){ //neighbor left
			if(direction == 7 || direction == 0){buffer[row][column].set_direction(++direction);}
			else if(direction == 4 || direction == 5){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(2);}
		}else if(map[rowAbove][colLeft].get_status() == 2){ //neighbor above left
			if(direction == 0 || direction == 1){buffer[row][column].set_direction(++direction);}
			else if(direction == 5 || direction == 6){buffer[row][column].set_direction(--direction);}
			else{buffer[row][column].set_direction(3);}
		}
			
		
		//if space open, move
		int moveBad = 0; //1 if bad move
		switch(direction){
		case 0: 
			if(buffer[rowAbove][column].get_status() == 2){ //if collides with particle
					moveBad = 1; //
					return; //exit function... work here is done
			}else{
				buffer[rowAbove][column].set_status(2);
				buffer[rowAbove][column].set_direction(direction); 
			}
			break;
		case 1:	
			if(buffer[rowAbove][colRight].get_status() == 2){moveBad = 1;}
			else{
				buffer[rowAbove][colRight].set_status(2);
				buffer[rowAbove][colRight].set_direction(direction);return;
			}
			break;
		case 2: if(buffer[row][colRight].get_status() == 2){ moveBad = 1;}
			else{
				buffer[row][colRight].set_status(2);
				buffer[row][colRight].set_direction(direction);return;
			}
			break;
		case 3: if(buffer[rowBelow][colRight].get_status() == 2){ moveBad = 1;}
			else{
				buffer[rowBelow][colRight].set_status(2);
				buffer[rowBelow][colRight].set_direction(direction);return;
			}
			break;
		case 4: if(buffer[rowBelow][column].get_status() == 2){ moveBad = 1;}
			else{
				buffer[rowBelow][column].set_status(2);
				buffer[rowBelow][column].set_direction(direction);return;
			}
			break;
		case 5: if(buffer[rowBelow][colLeft].get_status() == 2){ moveBad = 1;}
			else{
				buffer[rowBelow][colLeft].set_status(2);
				buffer[rowBelow][colLeft].set_direction(direction);return;
			}
			break;
		case 6: if(buffer[row][colLeft].get_status() == 2){ moveBad = 1;}
			else{
				buffer[row][colLeft].set_status(2);
				buffer[row][colLeft].set_direction(direction); return;
			}
			break;
		case 7: if(buffer[rowAbove][colLeft].get_status() == 2){ moveBad = 1;}
			else{
				buffer[rowAbove][colLeft].set_status(2);
				buffer[rowAbove][colLeft].set_direction(direction); return;
			}
			break;
		default:
			break;
		}
		
		if(moveBad == 1){
			buffer[row][column].set_status(2);
			randDir = ThreadLocalRandom.current().nextInt(0, 8);
			buffer[row][column].set_direction(randDir);
		}
	}
	
	public void generateParticles(){
		for(int i = 0; i < 10; i++){
			int randX = ThreadLocalRandom.current().nextInt(0, ROWS-1);
			int randY = ThreadLocalRandom.current().nextInt(0, COLS-1);
			int randDir = ThreadLocalRandom.current().nextInt(0, 8);
			
			//don't appear in center
			if(randX > ROWS/4 && randX < ROWS * 3 / 4)
				randX = (randX + ROWS/2) % ROWS;
			if(randY > COLS/4 && randY < COLS * 3 / 4)
				randY = (randY + COLS/2) % COLS;
		
			if(buffer[randX][randY].get_status() == 1){
				buffer[randX][randY].set_status(2);
				buffer[randX][randY].set_direction(randDir);
			}
		}	
	}
	
	public void update(){
		//count number of active particles, if num < 1000, generate more around corners
		int numActive = 0;
		
		//for each particle, determine neighbors and detect collisions
		//if collision, move apart		else, keep moving in original direction
		for(int row = 0; row < ROWS; row++){ 
			for(int column = 0; column < COLS; column++){
				//if inactive cell, skip
				if(map[row][column].get_status() == 1){	continue;}
				//else, move and do collision detection
				if(map[row][column].get_status() == 0){
					buffer[row][column].set_status(0);
					continue;
				}
				numActive++;
				moveDirection(row, column);
			}
		}
		
		if(numActive < NUM_PARTICLES)
			generateParticles();
		
		//swap buffer and map
		Cell[][] temp = map;
		map = buffer;
		buffer = temp;
		clear(buffer);
	}
	
	private void clear(Cell[][] buffer2) {
		for(int i = 0; i < ROWS; i++)
			for(int j = 0; j < COLS; j++)
				if(buffer2[i][j].get_status()!=0)
					buffer2[i][j].set_status(1);
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
