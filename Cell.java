import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/** class for cellular Automation
 * 	GameOfLife,	ForrestFire, UniverseSim, AggDiff:
 * @author phillics
 */

public class Cell {
	//PRIVATE
	private int xLeft; //location
	private int yTop;
	private int size;
	
	private int status; 
	//GOL: 1 alive, 0 dead, 	
	//DA:	0 attached, 1 empty, 2 particle		
	//FF:	-9 to -6 fire, -5 empty, -4 to -1 plant
	
	private int direction;
	//for brownian motion: 0 is up, 1-7 are filled clockwise... -1 is not moving
	
	public Cell(int x, int y, int s){
		xLeft = x;
		yTop = y;
		status = 0;
		size = s;
		direction = 0;
	}
	
	public int get_status(){ return status;}
	public int get_xLeft(){ return xLeft;}
	public int get_yTop(){return yTop;}
	public int get_direction(){return direction;}
	
	public void set_direction(int dir){direction = dir;}
	
	public void move(int dx, int dy){
		xLeft += dx; 
		yTop += dy;
	}
	
	public void moveto(int posX, int posY) {
		xLeft = posX;
		yTop = posY;
	}
	
	public void set_status(int s){ status = s;}

	public void draw(Graphics2D g2){
		Rectangle body = new Rectangle(xLeft, yTop, size, size);
		Color color;
		
		switch(status){
		//Game of Life
		case 0: color = Color.WHITE;
			break;
		case 1: color = Color.BLACK;
			break;
			
		//DiffusionAggregation
		case 2: color = Color.DARK_GRAY;
			break;
			
		//Forest Fire Simulation
		case -9: color = Color.RED;	//bright Red
			break;	
		case -8: color = new Color(190,0,0);
			break;
		case -7: color = new Color(127,0,0);
			break;
		case -6: color = new Color(63,0,0);
			break;
		case -5: color = Color.BLACK;
			break;
		case -4: color = new Color(0, 100, 0);
			break;	
		case -3: color = new Color(0,140,0);
			break;
		case -2: color = new Color(0,170,0);
			break;
		case -1: color = new Color(0,200,0);	// Green
			break;
		//Universe Simulation
		default: color = Color.WHITE;
		}
		
		
		g2.setColor(color);
		g2.fill(body);
		g2.draw(body);
	}
}
/*Color scheme
 * 1-10	Aggregation and Universe 	(black (1) -> blue (5) -> white (10)
 * 1 - Black	Game of Life
 * 0 - White
 * -1 to -9	forest fire 			(green (-1) -> black (-5) <- red (-9))
 * */