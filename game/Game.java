package game;

import base.State;

public abstract class Game {

	public abstract Game get_copy(State state);
	public State[] get_possible_states() {
		return null;
	}

	public double[] update(int p1_action, int p2_action) {
		return update(new int[] { p1_action, p2_action });
	}
	public abstract double[] update(int[] actions);

	public abstract State get_state();
	public void randomize() {}

}

