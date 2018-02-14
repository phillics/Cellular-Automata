import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UpdateButton extends JButton implements ActionListener{

	private static int mode;
	
	private GameOfLife game;
	private ForestFire fire;
	private DiffusionAggregation structure;
	private BrownianAggregation brown;
	private RandomWander wander;
	
	
	public UpdateButton(GameOfLife game){
		super("Click to Update");
		this.game = game;
		this.mode = 1;
		
		//tells java to call this class if user clicks
		this.addActionListener(this);
	}
	
	public UpdateButton(ForestFire fire){
		super("Click to Update");
		this.fire = fire;
		this.mode = 2;
		this.addActionListener(this);
	}
	
	public UpdateButton(DiffusionAggregation structure){
		super("Click to Update");
		this.structure = structure;
		this.mode = 3;
		this.addActionListener(this);
	}
	
	public UpdateButton(RandomWander wander){
		super("Click to Update");
		this.wander = wander;
		this.mode = 4;
		this.addActionListener(this);
	}
	
	public UpdateButton(BrownianAggregation brown) {
		super("Click to Update");
		this.brown = brown;
		this.mode = 5;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(mode == 1){ //Game of Life
			this.game.update();
			this.game.repaint();
		} else if(mode == 2){
			this.fire.update();
			this.fire.repaint();
		} else if(mode == 3){
			this.structure.update();
			this.structure.repaint();
		} else if(mode == 4){
			this.wander.update();
			this.wander.repaint();
		}
		else if(mode == 5){
			this.brown.update(); 
			this.brown.repaint();
		}
	}

}
