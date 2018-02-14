import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JComponent;

public class RandomWander extends JComponent{
	private static final int CELL_SIZE = 5;
	private static final int STEP_SIZE = 5;
	
	private Cell taran;
	
	RandomWander(){
		this.taran = new Cell(950, 500, CELL_SIZE);
		this.taran.set_status(1);
	}
	
	public void update(){
		//find random direction to move in
		int randomNum = ThreadLocalRandom.current().nextInt(0, 8); //get number from 0-7
		switch(randomNum){
		case 0: taran.move(STEP_SIZE, 0);			//right
			break;
		case 1: taran.move(STEP_SIZE, STEP_SIZE);	//right down
			break;
		case 2: taran.move(0, STEP_SIZE);			//down
			break;
		case 3: taran.move(-STEP_SIZE, STEP_SIZE);	//left, down
			break;
		case 4: taran.move(-STEP_SIZE, 0);			//left
			break;
		case 5: taran.move(-STEP_SIZE, 0);			//left, up
			break;
		case 6: taran.move(0, -STEP_SIZE);			//up
			break;
		case 7: taran.move(STEP_SIZE, -STEP_SIZE);	//right, up
			break;
		default: 
			break;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//render living cells
		taran.draw(g2);				//draw
	}
	
}
