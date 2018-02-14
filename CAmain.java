/*Goals:
 * Game of Life: 				needs better initialization
 * Forest Fire Simulation: 		Done
 * Diffusion Aggregation 		Done
 * Wandering Random				Done
 * Brownian Aggregation			Needs work...  
 * Brownian Motion with projectile disturbance:	TODO
 * River delta					TODO:
 * Gravity
 * Universe Simulation
 * */

import java.awt.BorderLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.Timer;

public class CAmain {
	private static final int WIDTH = 1900;
	private static final int HEIGHT = 1000;
	
	
	public static void main(String[] args) {
		// allow choice of simulation
		switch(displayMenu()){
		case 1: launchGameOfLife();
			break;
		case 2: launchForestFire();
			break;
		case 3: DiffusionAggregation();
			break;
		case 4: RandomWandering();
			break;
		case 5: BrownianAggregation();
			break;
		default: 
			System.out.print("Exiting");
			System.exit(0);
		}
	}



	private static void RandomWandering() {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Random Wandering");
		
		final RandomWander wanderer = new RandomWander();
		frame.add(wanderer);
		
		final UpdateButton updateButton = new UpdateButton(wanderer);
		frame.add(updateButton, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer clickTimer = new Timer(50, updateButton);
		clickTimer.start();
	}


	private static void BrownianAggregation() {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Brownian Diffusion Limited Aggregation");
		
		final BrownianAggregation brown = new BrownianAggregation();
		frame.add(brown);
		
		final UpdateButton updateButton = new UpdateButton(brown);
		frame.add(updateButton, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer clickTimer = new Timer(100, updateButton);
		clickTimer.start();
	}


	private static void DiffusionAggregation() {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Diffusion Limited Aggregation");
		
		final DiffusionAggregation structure = new DiffusionAggregation();
		frame.add(structure);
		
		final UpdateButton updateButton = new UpdateButton(structure);
		frame.add(updateButton, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer clickTimer = new Timer(50, updateButton);
		clickTimer.start();
	}

	private static void launchForestFire() {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Forest Fire");
		
		final ForestFire fire = new ForestFire();
		frame.add(fire);
		
		final UpdateButton updateButton = new UpdateButton(fire);
		frame.add(updateButton, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer clickTimer = new Timer(50, updateButton);
		clickTimer.start();
	}

	private static void launchGameOfLife() {
		JFrame frame = new JFrame();

		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Conway's Game of Life");
		
		final GameOfLife game = new GameOfLife();
		frame.add(game);
		
		final UpdateButton updateButton = new UpdateButton(game);
		frame.add(updateButton, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer clickTimer = new Timer(20, updateButton);
		clickTimer.start();
	}

	private static int displayMenu(){
		System.out.println("Which Simulation Would You Like To View?\n");
		System.out.println("(0) QUIT");
		System.out.println("(1) Game of Life Simulation");
		System.out.println("(2) Forest Fire Simulation");
		System.out.println("(3) Diffusion Limited Aggregation");
		System.out.println("(4) Random Wanderer");
		System.out.println("(5) Brownian Aggregation");
		int response = 0;
		Scanner in = new Scanner(System.in);
		while (in.hasNextInt()){
			response = in.nextInt();
			break;
		}
		in.close();
		return response;
	}

}
