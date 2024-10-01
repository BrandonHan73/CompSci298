package game;

import base.State;

public class CycleGame extends Game {

	public final static double confront = 1, steal = 1, avoid = 0.5, predict = 0.5;

	private State state;

	public CycleGame() {
		state = new State(new int[][] {
			new int[] { 0, 1 },
			new int[] { 0, 1 }
		});
	}

	@Override
	public State[] get_possible_states() {
		return new State[] { state };
	}

	@Override
	public Game get_copy(State state) {
		return new CycleGame();
	}

	@Override
	public double[] update(int[] actions) {
		int p1_action = actions[0];
		int p2_action = actions[1];

		double[] reward = null;

		if(p1_action == 0 && p2_action == 0) {
			reward = new double[] { -steal, steal }; 
		} else if(p1_action == 0 && p2_action == 1) {
			reward = new double[] { predict, -predict };
		} else if(p1_action == 1 && p2_action == 0) {
			reward = new double[] { confront, -confront };
		} else if(p1_action == 1 && p2_action == 1) {
			reward = new double[] { -avoid, avoid };
		}

		return reward;
	}

	@Override
	public State get_state() {
		return state;
	}

}

